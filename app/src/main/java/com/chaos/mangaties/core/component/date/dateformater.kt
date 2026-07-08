package com.chaos.mangaties.core.component.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    fun format(dateString: String): String {
        return try {
            val date = inputFormat.parse(dateString.substring(0, 19))
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateString.substring(0, 10)
        }
    }
}