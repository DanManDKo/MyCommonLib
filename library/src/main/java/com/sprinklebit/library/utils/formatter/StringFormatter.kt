package com.sprinklebit.library.utils.formatter

object StringFormatter {

    fun formatComma(vararg strings: String?): String {
        val stringBuilder = StringBuilder()

        val comma = ", "
        for (str in strings) {
            if (!str.isNullOrEmpty()) {
                stringBuilder.append(str)
                stringBuilder.append(comma)
            }
        }
        if (stringBuilder.length >= comma.length) {
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }

        return stringBuilder.toString()
    }

    fun capitalizeFirstCharacter(str: String?): String {

        if (str.isNullOrEmpty()) {
            return ""
        }

        val sb = StringBuilder(str)
        sb.setCharAt(0, Character.toUpperCase(sb[0]))
        return sb.toString()
    }
}
