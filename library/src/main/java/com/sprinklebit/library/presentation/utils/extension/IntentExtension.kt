package com.sprinklebit.library.presentation.utils.extension

import android.content.Intent
import java.io.Serializable

fun Intent.getExtraInt(key: String): Int? {
    if (!this.hasExtra(key)) {
        return null
    }
    return this.getIntExtra(key, -1)
}

fun Intent.getExtraFloat(key: String): Float? {
    if (!this.hasExtra(key)) {
        return null
    }
    return this.getFloatExtra(key, 0.0f)
}

fun Intent.getExtraDouble(key: String): Double? {
    if (!this.hasExtra(key)) {
        return null
    }
    return this.getDoubleExtra(key, 0.0)
}

fun Intent.getExtraBoolean(key: String): Boolean? {
    if (!this.hasExtra(key)) {
        return null
    }
    return this.getBooleanExtra(key, false)
}

fun Intent.getExtraSerializable(key: String): Serializable? {
    if (!this.hasExtra(key)) {
        return null
    }
    return this.getSerializableExtra(key)
}