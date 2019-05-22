package com.sprinklebit.library.utils.extention

import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

fun Fragment.setExtra(key: String, value: Any?) = this.apply {
    arguments = (arguments ?: Bundle()).apply {
        if (value != null) {
            when (value) {
                is Int -> putInt(key, value)
                is Double -> putDouble(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Serializable -> putSerializable(key, value)
            }
        }
    }
}

fun Fragment.getExtraInt(key: String): Int? = this.arguments?.let {
    if (it.containsKey(key)) it.getInt(key) else null
}

fun Fragment.getExtraDouble(key: String): Double? = this.arguments?.let {
    if (it.containsKey(key)) it.getDouble(key) else null
}

fun Fragment.getExtraLong(key: String): Long? = this.arguments?.let {
    if (it.containsKey(key)) it.getLong(key) else null
}

fun Fragment.getExtraFloat(key: String): Float? = this.arguments?.let {
    if (it.containsKey(key)) it.getFloat(key) else null
}

fun Fragment.getExtraString(key: String): String? = this.arguments?.getString(key)

fun Fragment.getExtraBoolean(key: String): Boolean? = this.arguments?.let {
    if (it.containsKey(key)) it.getBoolean(key) else null
}

fun Fragment.getExtraSerializable(key: String): Serializable? =
        this.arguments?.getSerializable(key)