package com.kareimt.anwarresala.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import java.io.File
import androidx.core.net.toUri

object ImageUtils {
    @SuppressLint("DiscouragedApi")
    fun getImageUri(context: Context, imagePath: String?): Uri {
        return when {
            imagePath?.startsWith("drawable/") ?: false -> {
                // Handle drawable resources
                val resourceId = context.resources.getIdentifier(
                    imagePath.substringAfter("drawable/"),
                    "drawable",
                    context.packageName
                )
                "android.resource://${context.packageName}/$resourceId".toUri()
            }
            imagePath?.contains("content://") ?: false -> {
                // Handle content URIs directly
                imagePath.toUri()
            }
            else -> {
                // Handle internal storage files
                try {
                    val file = File(context.filesDir, imagePath ?: "drawable/anwar_resala_logo")
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