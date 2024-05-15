package com.kproject.simpleplayer.presentation.screens.components.player.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerAction
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerState
import com.kproject.simpleplayer.presentation.screens.components.player.components.MainControlButtons
import com.kproject.simpleplayer.presentation.screens.components.player.components.ProgressContent
import com.kproject.simpleplayer.presentation.screens.components.player.components.ProgressTextLayout
import com.kproject.simpleplayer.presentation.screens.components.player.components.SecondaryControlButtons
import com.kproject.simpleplayer.presentation.screens.components.player.components.TopBarTitle
import com.kproject.simpleplayer.presentation.screens.components.player.fakePlayerState
import com.kproject.simpleplayer.presentation.theme.PreviewTheme

@Composable
fun BoxScope.PlayerLayout2(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    title: String,
    showMainUi: Boolean,
    onArrowBackIconClick: () -> Unit,
    onOptionsIconClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = showMainUi,
        modifier = Modifier.align(Alignment.TopCenter)
    ) {
        TopBarTitle(
            title = title,
            onNavigateBack = onArrowBackIconClick,
            onOptionsIconClick = onOptionsIconClick
        )
    }

    AnimatedVisibility(
        visible = showMainUi,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier = Modifier.align(Alignment.Center)
    ) {
        MainControlButtons(
            showSeekButtons = playerState.uiOptions.showSeekButtons,
            isNextButtonAvailable = playerState.isNextButtonAvailable,
            isSeekForwardButtonAvailable = playerState.isSeekForwardButtonAvailable,
            isSeekBackButtonAvailable = playerState.isSeekBackButtonAvailable,
            playbackState = playerState.playbackState,
            isPlaying = playerState.isPlaying,
            isStateBuffering = playerState.isStateBuffering,
            onPlayerAction = onPlayerAction,
            allButtonColorsFilled = false
        )
    }

    AnimatedVisibility(
        visible = showMainUi,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        BottomContent(
            playerState = playerState,
            onPlayerAction = onPlayerAction,
        )
    }
}

@Composable
private fun BottomContent(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.safeDrawingPadding()) {
        SecondaryControlButtons(
            resizeMode = playerState.resizeMode,
            repeatMode = playerState.repeatMode,
            playbackSpeed = playerState.playbackSpeed,
            isLandscapeMode = playerState.isLandscapeMode,
            onPlayerAction = onPlayerAction,
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(Modifier.height(16.dp))
        ProgressContent(
            currentPlaybackPosition = playerState.currentPlaybackPosition,
            currentBufferedPercentage = playerState.currentBufferedPercentage,
            videoDuration = playerState.videoDuration,
            onPlayerAction = onPlayerAction,
            progressTextLayout = ProgressTextLayout.YouTube
        )
        Spacer(Modifier.height(12.dp))
    }
}

@PreviewScreenSizes
@Composable
private fun PlayerLayout2Preview() {
    PreviewTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PlayerLayout2(
                playerState = fakePlayerState,
                onPlayerAction = {},
                title = "Video",
                showMainUi = true,
                onArrowBackIconClick = {},
                onOptionsIconClick = {}
            )
        }
    }
}