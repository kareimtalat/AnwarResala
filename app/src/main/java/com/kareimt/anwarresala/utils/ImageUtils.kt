package com.kareimt.anwarresala.utils

import android.content.Context
import android.net.Uri
import java.io.File
import androidx.core.net.toUri

object ImageUtils {
    fun getImageUri(context: Context, imagePath: String): Uri {
        return when {
            imagePath.startsWith("drawable/") -> {
                // Handle drawable resources
                val resourceId = context.resources.getIdentifier(
                    imagePath.substringAfter("drawable/"),
                    "drawable",
                    context.packageName
                )
                "android.resource://${context.packageName}/$resourceId".toUri()
            }
            imagePath.contains("content://") -> {
                // Handle content URIs directly
                imagePath.toUri()
            }
            else -> {
                // Handle internal storage files
                try {
                    val file = File(context.filesDir, imagePath)
                    Uri.fromFile(file)
                } catch (e: Exception) {
                    // Fallback to default image
                    val resourceId = context.resources.getIdentifier(
                        "anwar_resala_logo",
                        "drawable",
                        context.packageName
                    )
                    "android.resource://${context.packageName}/$resourceId".toUri()
                }
            }
        }
    }
}