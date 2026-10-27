package com.example.zarorvecinos.native

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.zarorvecinos.MainActivity

class NotificacionesManager(private val context: Context) {
    init {
        if (Build.VERSION.SDK_INT >= 26) {
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel("zaror_mvp", "Finanzas y borradores", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
    }
    fun permitido(): Boolean = (Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) && NotificationManagerCompat.from(context).areNotificationsEnabled()
    fun publicar(id: Int, titulo: String, texto: String, destino: String): Boolean {
        if (!permitido()) return false
        return try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("destino", destino)
            }
            val pending = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(context, "zaror_mvp")
                .setSmallIcon(com.example.zarorvecinos.R.drawable.ic_zaror_notification)
                .setContentTitle(titulo)
                .setContentText(texto).setContentIntent(pending).setAutoCancel(true).build()
            if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) return false
            NotificationManagerCompat.from(context).notify(id, notification)
            true
        } catch (_: SecurityException) { false }
    }
}
