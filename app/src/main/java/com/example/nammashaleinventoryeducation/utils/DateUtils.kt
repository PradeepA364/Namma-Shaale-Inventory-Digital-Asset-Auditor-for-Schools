package com.example.nammashaleinventoryeducation.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun formatForDisplay(dateStr: String): String {
        return try {
            val inputSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputSdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputSdf.parse(dateStr)
            outputSdf.format(date ?: Date())
        } catch (e: Exception) {
            dateStr
        }
    }

    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
