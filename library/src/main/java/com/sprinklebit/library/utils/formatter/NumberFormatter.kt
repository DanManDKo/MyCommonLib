package com.sprinklebit.library.utils.formatter

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

object NumberFormatter {

    private val integerFormatter: NumberFormat by lazy {
        val format = NumberFormat.getIntegerInstance()
        format.roundingMode = RoundingMode.DOWN
        format
    }

    private val percentFormatter: NumberFormat by lazy {
        val percentInstance = NumberFormat.getPercentInstance()
        percentInstance.minimumFractionDigits = 2
        percentInstance.maximumFractionDigits = 2
        percentInstance
    }

    private val floatFormatter: NumberFormat by lazy {
        val percentInstance = NumberFormat.getNumberInstance()
        percentInstance.minimumFractionDigits = 2
        percentInstance.maximumFractionDigits = 2
        percentInstance
    }

    private val currencyInstance: NumberFormat by lazy {
        DecimalFormat("$#,##0.00")
    }

    private val currencyWithSignInstance: NumberFormat by lazy {
        DecimalFormat("+$#,##0.00;-$#")
    }

    fun formatInteger(obj: Any): String {
        return integerFormatter.format(obj)
    }

    fun formatPrice(obj: Any): String {
        return currencyInstance.format(obj)
    }

    fun formatPriceWithSign(obj: Any): String {
        return currencyWithSignInstance.format(obj)
    }

    fun formatPercent(obj: Number): String {
        return percentFormatter.format(obj.toFloat() / 100f)
    }

    fun formatFloat(obj: Number): String {
        return floatFormatter.format(obj)
    }

    fun formatFloatInShort(obj: Number): String {
        val number = obj.toFloat()
        if (number < 0) {
            return "-" + formatFloatInShort(-number)
        }

        var result = number
        var suffix = ""
        if (result > 900) {
            suffix = "K"
            result /= 1000
        }
        if (result > 900) {
            suffix = "M"
            result /= 1000
        }
        if (result > 900) {
            suffix = "B"
            result /= 1000
        }
        if (result > 900) {
            suffix = "T"
            result /= 1000
        }
        if (result > 900) {
            suffix = "P"
            result /= 1000
        }
        val value: String
        if (result < 1) {
            value = String.format(Locale.getDefault(), "%.2f", result)
        } else if (result < 11) {
            value = String.format(Locale.getDefault(), "%.2f", result)
        } else if (result < 101) {
            value = String.format(Locale.getDefault(), "%.2f", result)
        } else {
            value = String.format(Locale.getDefault(), "%.0f", result)
        }

        return String.format("%s%s", value, suffix)
    }

    @Throws(ParseException::class)
    fun parseInteger(text: String): Number {
        return integerFormatter.parse(text)
    }

    @Throws(ParseException::class)
    fun parsePrice(text: String): Number {
        return currencyInstance.parse(text.replace("$", ""))
    }

    fun newCurrencyInstance(): NumberFormat {
        return currencyInstance.clone() as NumberFormat
    }
}
