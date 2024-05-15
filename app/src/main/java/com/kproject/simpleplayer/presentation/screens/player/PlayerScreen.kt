package com.kproject.simpleplayer.presentation.screens.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.kproject.simpleplayer.presentation.commom.Utils
import com.kproject.simpleplayer.presentation.screens.components.LockScreenOrientation
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerAction
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerLayout
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerState
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerView
import com.kproject.simpleplayer.presentation.screens.components.player.SeekBackIncrement
import com.kproject.simpleplayer.presentation.screens.components.player.SeekForwardIncrement
import com.kproject.simpleplayer.presentation.screens.components.player.layout.PlayerLayout1
import com.kproject.simpleplayer.presentation.screens.components.player.layout.PlayerLayout2
import com.kproject.simpleplayer.presentation.screens.components.player.layout.PlayerLayout3
import com.kproject.simpleplayer.presentation.screens.components.player.rememberMediaPlayerManager
import com.kproject.simpleplayer.presentation.screens.player.components.PlayerUiOptionsDialog
import com.kproject.simpleplayer.presentation.screens.player.model.MediaType
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    mediaType: MediaType,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val playerViewModel: PlayerViewModel = viewModel()
    val mediaItems = remember { playerViewModel.getMediaItemList(mediaType = mediaType) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).apply {
            setSeekForwardIncrementMs(SeekForwardIncrement)
            setSeekBackIncrementMs(SeekBackIncrement)
        }.build().apply {
            setMediaItems(mediaItems)
            prepare()
            playWhenReady = true
        }
    }

    val mediaPlayerManager = rememberMediaPlayerManager(player = exoPlayer)
    val playerState = mediaPlayerManager.playerState
    var showMainUi by rememberSaveable { mutableStateOf(true) }
    // Just to restart the delay to hide the main UI if the user performs some action (play/pause, for example)
    var lastPlayerActionTimeMillis by remember { mutableLongStateOf(0L) }

    // Hides the main UI after the time
    LaunchedEffect(
        key1 = showMainUi,
        key2 = playerState.uiOptions.autoHideButtons,
        key3 = lastPlayerActionTimeMillis
    ) {
        delay(playerState.uiOptions.timeToHideButtons)
        if (playerState.uiOptions.autoHideButtons) {
            showMainUi = false
        }
    }

    var showPlayerUiOptionsDialog by remember { mutableStateOf(false) }

    PlayerView(
        mediaPlayerState = mediaPlayerManager,
        exoPlayer = exoPlayer,
        onPlayerViewClick = {
            showMainUi = !showMainUi
            if (showMainUi) {
                Utils.showSystemBars(context)
            } else {
                Utils.hideSystemBars(context)
            }
        }
    ) {
        PlayerLayout(
            playerState = playerState,
            onPlayerAction = { action ->
                mediaPlayerManager.onPlayerAction(action)
                lastPlayerActionTimeMillis = System.currentTimeMillis()
            },
            title = exoPlayer.mediaMetadata.title.toString(),
            showMainUi = showMainUi,
            onArrowBackIconClick = onNavigateBack,
            onOptionsIconClick = { showPlayerUiOptionsDialog = true }
        )
    }

    PlayerUiOptionsDialog(
        showDialog = showPlayerUiOptionsDialog,
        onDismiss = { showPlayerUiOptionsDialog = false },
        uiOptions = playerState.uiOptions,
        onUiOptionsChange = { uiOptions ->
            mediaPlayerManager.onPlayerAction(PlayerAction.ChangeUiOptions(uiOptions))
        }
    )

    LockScreenOrientation(isLandscapeMode = playerState.isLandscapeMode)
}

@Composable
private fun BoxScope.PlayerLayout(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    title: String,
    showMainUi: Boolean,
    onArrowBackIconClick: () -> Unit,
    onOptionsIconClick: () -> Unit,
) {
    when (playerState.uiOptions.playerLayout) {
        PlayerLayout.Layout1 -> {
            PlayerLayout1(
                playerState = playerState,
                onPlayerAction = onPlayerAction,
                title = title,
                showMainUi = showMainUi,
                onArrowBackIconClick = onArrowBackIconClick,
                onOptionsIconClick = onOptionsIconClick
            )
        }
        PlayerLayout.Layout2 -> {
            PlayerLayout2(
                playerState = playerState,
                onPlayerAction = onPlayerAction,
                title = title,
                showMainUi = showMainUi,
                onArrowBackIconClick = onArrowBackIconClick,
                onOptionsIconClick = onOptionsIconClick
            )
        }
        PlayerLayout.Layout3 -> {
            PlayerLayout3(
                playerState = playerState,
                onPlayerAction = onPlayerAction,
                title = title,
                showMainUi = showMainUi,
                onArrowBackIconClick = onArrowBackIconClick,
                onOptionsIconClick = onOptionsIconClick
            )
        }
    }
}