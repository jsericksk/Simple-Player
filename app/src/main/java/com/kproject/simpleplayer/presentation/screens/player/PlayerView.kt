package com.kproject.simpleplayer.presentation.screens.player

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.ExoPlayerLifecycleController
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.MediaPlayerManager

@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    mediaPlayerState: MediaPlayerManager,
    exoPlayer: ExoPlayer,
    onPlayerViewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val playerView = remember {
        PlayerView(context).apply {
            this.useController = false
            this.player = exoPlayer
        }
    }

    AndroidView(
        factory = {
            playerView
        },
        update = {
            it.resizeMode = mediaPlayerState.playerState.resizeMode.value
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111318))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onPlayerViewClick.invoke() }
    )

    ExoPlayerLifecycleController(
        playerView = playerView,
        onStartOrResume = {
            if (mediaPlayerState.playerState.isPlaying) {
                exoPlayer.play()
            }
        },
        onPauseOrStop = {
            exoPlayer.pause()
        },
        onDispose = {
            mediaPlayerState.releasePlayer()
        }
    )
}