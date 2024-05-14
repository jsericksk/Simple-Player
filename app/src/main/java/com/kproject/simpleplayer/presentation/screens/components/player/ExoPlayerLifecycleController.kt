package com.kproject.simpleplayer.presentation.screens.components.player

import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.ui.PlayerView

private val AndroidSdkVersion = Build.VERSION.SDK_INT

@Composable
fun ExoPlayerLifecycleController(
    playerView: PlayerView,
    onStartOrResume: () -> Unit,
    onPauseOrStop: () -> Unit,
    onDispose: () -> Unit
) {
    ActivityLifecycle(
        onStart = {
            if (AndroidSdkVersion > 23) {
                playerView.onResume()
                onStartOrResume.invoke()
            }
        },
        onResume = {
            if (AndroidSdkVersion <= 23) {
                playerView.onResume()
                onStartOrResume.invoke()
            }
        },
        onPause = {
            if (AndroidSdkVersion <= 23) {
                playerView.onPause()
                onPauseOrStop.invoke()
            }
        },
        onStop = {
            if (AndroidSdkVersion > 23) {
                playerView.onPause()
                onPauseOrStop.invoke()
            }
        },
        onDispose = { onDispose.invoke() }
    )
}

@Composable
fun ActivityLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDispose: () -> Unit = {}
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> onStart.invoke()
                Lifecycle.Event.ON_RESUME -> onResume.invoke()
                Lifecycle.Event.ON_PAUSE -> onPause.invoke()
                Lifecycle.Event.ON_STOP -> onStop.invoke()
                else -> {
                    Log.d("ActivityLifecycle", "Called any")
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            onDispose.invoke()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}