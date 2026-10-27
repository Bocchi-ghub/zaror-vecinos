package com.example.zarorvecinos.repository

import org.json.JSONObject
import com.example.zarorvecinos.model.Formulario

object FormularioCodec {
    fun codificar(f: Formulario) = JSONObject().apply {
        put("tipo", f.tipo); put("id", f.id); put("nombre", f.nombre); put("concepto", f.concepto)
        put("fecha", f.fecha); put("inicio", f.inicio); put("fin", f.fin)
        put("asistentes", f.asistentes); put("monto", f.monto); put("movimiento", f.movimiento)
        put("foto", f.foto ?: JSONObject.NULL)
    }.toString()
    fun decodificar(texto: String): Formulario {
        val j = JSONObject(texto)
        return Formulario(j.getString("tipo"), j.optInt("id"), j.optString("nombre"), j.optString("concepto"),
            j.optString("fecha"), j.optString("inicio"), j.optString("fin"), j.optString("asistentes"),
            j.optString("monto"), j.optString("movimiento", "INGRESO"), if (j.isNull("foto")) null else j.getString("foto"))
    }
}
