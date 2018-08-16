package com.sprinklebit.library.utils.formatter

import com.sprinklebit.library.utils.LazyWeakReference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private val shortDate = LazyWeakReference {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    private val chartDate = LazyWeakReference {
        SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
    }

    private val mediumDate = LazyWeakReference {
        DateFormat.getDateInstance(DateFormat.MEDIUM)
    }

    private val shortChartDate = LazyWeakReference {
        SimpleDateFormat("MMM d", Locale.getDefault())
    }

    @JvmStatic
    fun formatShortTime(date: Date): String {
        return shortDate.get().format(date)
    }

    @JvmStatic
    fun formatChartDate(date: Date): String {
        return chartDate.get().format(date)
    }

    @JvmStatic
    fun formatShortChartDate(date: Date): String {
        return shortChartDate.get().format(date)
    }

    @JvmStatic
    fun formatMediumDate(milliseconds: Long): String {
        val date = Date(milliseconds)
        return mediumDate.get().format(date)
    }
}
