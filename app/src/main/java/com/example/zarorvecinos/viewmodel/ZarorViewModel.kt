package com.example.zarorvecinos.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.example.zarorvecinos.model.Borrador
import com.example.zarorvecinos.model.ConflictoReserva
import com.example.zarorvecinos.model.Formulario
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.repository.ZarorRepository
import com.example.zarorvecinos.repository.FormularioCodec.codificar
import com.example.zarorvecinos.repository.FormularioCodec.decodificar
import com.example.zarorvecinos.validation.Validacion
import com.example.zarorvecinos.native.NotificacionesManager

class ZarorViewModel(application: Application, private val saved: SavedStateHandle) : AndroidViewModel(application) {
    private val repo = ZarorRepository(application)
    private val avisos = NotificacionesManager(application)
    private val _pantalla = MutableStateFlow(Pantalla(formulario = saved.get<String>("formulario")?.let { runCatching { decodificar(it) }.getOrNull() }))
    val pantalla = _pantalla.asStateFlow()
    private var autoGuardado: Job? = null
    private var ultimoAviso: String? = null
    private fun registrarError(e: Throwable) {
        Log.e("ZarorMVP", "Operación fallida", e)
        _pantalla.update { it.copy(error = if (e is ConflictoReserva) e.message else "No se pudo completar la operación. Revisa los datos o inténtalo nuevamente.") }
    }
    val reservas = repo.reservas.catch { registrarError(it); emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val movimientos = repo.movimientos.catch { registrarError(it); emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val borradores = repo.borradores.catch { registrarError(it); emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun persistirEstado(f: Formulario?) { saved["formulario"] = f?.let(::codificar) }
    fun mensajeConsumido() { _pantalla.update { it.copy(mensaje = null) } }
    fun limpiarError() { _pantalla.update { it.copy(error = null) } }
    private fun trabajo(accion: suspend () -> Unit) {
        if (_pantalla.value.ocupado) return
        _pantalla.update { it.copy(ocupado = true, error = null) }
        viewModelScope.launch {
            try { autoGuardado?.cancelAndJoin(); accion() }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { registrarError(e) }
            finally { _pantalla.update { it.copy(ocupado = false) } }
        }
    }
    fun seccion(valor: String) {
        if (_pantalla.value.formulario != null) { salir(valor); return }
        _pantalla.update { it.copy(seccion = valor, error = null) }
    }
    fun abrir(tipo: String) = trabajo {
        val draft = repo.leer(tipo)
        val form = draft?.let { decodificar(it.contenido) } ?: Formulario(tipo = tipo)
        persistirEstado(form)
        _pantalla.update { it.copy(formulario = form, errores = emptyMap(), revisado = false, resumen = false) }
    }
    fun cambiar(transformar: (Formulario) -> Formulario) {
        val p = _pantalla.value
        if (p.ocupado) return
        val f = p.formulario?.let(transformar) ?: return
        persistirEstado(f)
        _pantalla.update { it.copy(formulario = f, errores = if (it.revisado) Validacion.errores(f) else emptyMap(), error = null, resumen = false) }
        autoGuardado?.cancel()
        autoGuardado = viewModelScope.launch {
            delay(350)
            try {
                if (f.tieneDatos) repo.guardar(Borrador(f.tipo, codificar(f), System.currentTimeMillis()))
                else repo.descartar(f.tipo)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { registrarError(e) }
        }
    }
    fun revisar() {
        val f = _pantalla.value.formulario ?: return
        val errores = Validacion.errores(f)
        _pantalla.update { it.copy(errores = errores, revisado = true, resumen = errores.isEmpty(), error = null) }
    }
    fun cerrarResumen() { if (!_pantalla.value.ocupado) _pantalla.update { it.copy(resumen = false) } }
    fun guardar() {
        val f = _pantalla.value.formulario ?: return
        if (!_pantalla.value.resumen) return
        val errores = Validacion.errores(f)
        if (errores.isNotEmpty()) { _pantalla.update { it.copy(errores = errores, revisado = true, resumen = false) }; return }
        trabajo {
            val iso = requireNotNull(Validacion.fechaIso(f.fecha))
            if (f.tipo == "RESERVA") {
                repo.guardar(Reserva(nombreSolicitante = f.nombre.trim(), espacio = "Sede vecinal", fecha = iso,
                    horaInicio = f.inicio, horaTermino = f.fin, asistentes = f.asistentes.toInt(),
                    observaciones = f.concepto.trim(), uriFoto = f.foto))
            } else {
                repo.guardar(Movimiento(concepto = f.concepto.trim(), tipo = f.movimiento,
                    monto = f.monto.toLong(), fecha = iso, uriFoto = f.foto))
                avisos.publicar((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), "Nuevo movimiento financiero",
                    "Se registró un ${if (f.movimiento == "INGRESO") "ingreso" else "gasto"}. Abre Finanzas para revisarlo.", "FINANZAS")
            }
            persistirEstado(null)
            _pantalla.update { it.copy(formulario = null, resumen = false, errores = emptyMap(),
                seccion = if (f.tipo == "RESERVA") "RESERVAS" else "FINANZAS", mensaje = "Registro guardado correctamente.") }
            ultimoAviso = null
        }
    }
    private suspend fun almacenarBorrador(f: Formulario) {
        if (!f.tieneDatos) { repo.descartar(f.tipo); return }
        val contenido = codificar(f)
        repo.guardar(Borrador(f.tipo, contenido, System.currentTimeMillis()))
        if (ultimoAviso != contenido) {
            avisos.publicar(if (f.tipo == "RESERVA") 101 else 102, "Tienes un formulario en borradores",
                "Tus datos están guardados en este dispositivo. Toca para continuar.", "BORRADORES")
            ultimoAviso = contenido
        }
    }
    fun salir(destino: String? = null) {
        val f = _pantalla.value.formulario ?: return
        trabajo {
            almacenarBorrador(f)
            persistirEstado(null)
            _pantalla.update { it.copy(formulario = null, resumen = false, errores = emptyMap(),
                seccion = destino ?: if (f.tipo == "RESERVA") "RESERVAS" else "FINANZAS",
                mensaje = if (f.tieneDatos) "Borrador guardado." else null) }
        }
    }
    fun segundoPlano() {
        val p = _pantalla.value
        val f = p.formulario ?: return
        if (p.ocupado || p.capturando || !f.tieneDatos) return
        trabajo { almacenarBorrador(f) }
    }
    fun descartar(tipo: String) = trabajo {
        repo.descartar(tipo)
        if (_pantalla.value.formulario?.tipo == tipo) {
            persistirEstado(null)
            _pantalla.update { it.copy(formulario = null, resumen = false, errores = emptyMap()) }
        }
    }
    fun cancelar(id: Int) = trabajo { repo.cancelar(id); _pantalla.update { it.copy(mensaje = "Reserva cancelada. El horario queda disponible.") } }
    fun captura(valor: Boolean) { _pantalla.update { it.copy(capturando = valor) } }
    fun foto(uri: String?) { captura(false); cambiar { it.copy(foto = uri) } }
}
