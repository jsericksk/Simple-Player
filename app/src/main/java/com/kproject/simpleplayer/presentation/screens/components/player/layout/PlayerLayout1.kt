package com.kproject.simpleplayer.presentation.screens.components.player.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun BoxScope.PlayerLayout1(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    title: String,
    showMainUi: Boolean,
    onArrowBackIconClick: () -> Unit,
    onOptionsIconClick: () -> Unit,
) {
    if (playerState.isStateBuffering) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 6.dp,
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp),
        )
    }

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
            playerState = playerState,
            onPlayerAction = onPlayerAction,
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(Modifier.height(16.dp))
        ProgressContent(
            playerState = playerState,
            onPlayerAction = onPlayerAction,
            progressTextLayout = ProgressTextLayout.Default
        )
        Spacer(Modifier.height(8.dp))
        MainControlButtons(
            playerState = playerState,
            onPlayerAction = onPlayerAction,
            allButtonColorsFilled = true
        )
        Spacer(Modifier.height(8.dp))
    }
}

@PreviewScreenSizes
@Composable
private fun PlayerLayout1Preview() {
    PreviewTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PlayerLayout1(
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