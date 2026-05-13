package com.example.nammashaleinventoryeducation.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageDir = File(context.filesDir, "asset_images")
        if (!imageDir.exists()) imageDir.mkdirs()
        return File(imageDir, "ASSET_${timeStamp}.jpg")
    }

    fun getImageUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun getPlaceholderImageForAsset(name: String, category: String): String? {
        val n = name.lowercase()
        val c = category.lowercase()
        
        return when {
            n.contains("cricket") -> "https://images.unsplash.com/photo-1531415074968-036ba1b575da?auto=format&fit=crop&w=400&q=80"
            n.contains("football") -> "https://images.unsplash.com/photo-1574629810360-7efbbe195018?auto=format&fit=crop&w=400&q=80"
            n.contains("microscope") -> "https://images.unsplash.com/photo-1579154204601-01588f351e67?auto=format&fit=crop&w=400&q=80"
            n.contains("projector") || c.contains("projector") -> "https://images.unsplash.com/photo-1535016120720-40c646bebbdc?auto=format&fit=crop&w=400&q=80"
            n.contains("printer") || c.contains("printer") -> "https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6?auto=format&fit=crop&w=400&q=80"
            n.contains("tv") || n.contains("television") || c.contains("digital") -> "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?auto=format&fit=crop&w=400&q=80"
            n.contains("dell") || n.contains("hp") || n.contains("computer") || c.contains("computer") || c.contains("laptop") -> 
                "https://images.unsplash.com/photo-1593642702821-c8da6771f0c6?auto=format&fit=crop&w=400&q=80"
            n.contains("fan") || c.contains("electrical") -> "https://images.unsplash.com/photo-1591130901021-39659b953244?auto=format&fit=crop&w=400&q=80"
            n.contains("water") || n.contains("purifier") -> "https://images.unsplash.com/photo-1585704032915-c3400ca199e7?auto=format&fit=crop&w=400&q=80"
            c.contains("sport") -> "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?auto=format&fit=crop&w=400&q=80"
            c.contains("lab") -> "https://images.unsplash.com/photo-1532187863486-abf9d3a44462?auto=format&fit=crop&w=400&q=80"
            c.contains("furniture") -> "https://images.unsplash.com/photo-1505691938895-1758d7eaa511?auto=format&fit=crop&w=400&q=80"
            c.contains("book") -> "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=400&q=80"
            c.contains("classroom") -> "https://images.unsplash.com/photo-1509062522246-3755977927d7?auto=format&fit=crop&w=400&q=80"
            else -> null
        }
    }
}
