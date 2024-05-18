package com.kproject.simpleplayer.presentation.screens.components.exoplayer

import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
    LifecycleObserver(
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
private fun LifecycleObserver(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDispose: () -> Unit = {}
) {
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnDispose by rememberUpdatedState(onDispose)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> currentOnStart()
                Lifecycle.Event.ON_RESUME -> currentOnResume()
                Lifecycle.Event.ON_PAUSE -> currentOnPause()
                Lifecycle.Event.ON_STOP -> currentOnStop()
                else -> {
                    Log.d("ActivityLifecycle", "Called any")
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            currentOnDispose()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}