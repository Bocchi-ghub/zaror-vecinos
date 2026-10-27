package com.example.zarorvecinos.native

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

object FotoManager {
    fun nueva(context: Context): Uri {
        val dir = File(context.filesDir, "comprobantes").apply { mkdirs() }
        val file = File(dir, "${UUID.randomUUID()}.jpg").apply { createNewFile() }
        return FileProvider.getUriForFile(context, "${context.packageName}.mvp.files", file)
    }
    fun eliminarTemporal(context: Context, uri: String?) {
        if (uri != null) runCatching { context.contentResolver.delete(Uri.parse(uri), null, null) }
    }
}
