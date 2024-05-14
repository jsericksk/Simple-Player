package com.kproject.simpleplayer.presentation.commom

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.util.Locale

object Utils {

    fun formatVideoDuration(durationInMs: Long): String {
        val seconds = durationInMs / 1000
        val second = seconds % 60
        val minute = seconds / 60 % 60
        val hour = seconds / (60 * 60) % 24
        return if (hour > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minute, second)
        }
    }

    fun showSystemBars(context: Context) {
        context.getWindow()?.let { window ->
            val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
            insetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    fun hideSystemBars(context: Context) {
        context.getWindow()?.let { window ->
            val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
            insetsControllerCompat.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            insetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    fun changeScreenOrientation(
        context: Context,
        changeToLandscape: Boolean
    ) {
        context.findActivity()?.let { activity ->
            val orientation = if (changeToLandscape) {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            activity.requestedOrientation = orientation
        }
    }
}