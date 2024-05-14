package com.kproject.simpleplayer.presentation.commom

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.getWindow(): Window? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context.window
        context = context.baseContext
    }
    return null
}