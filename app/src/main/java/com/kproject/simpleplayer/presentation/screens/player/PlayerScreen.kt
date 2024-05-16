package com.kproject.simpleplayer.presentation.screens.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import com.kproject.simpleplayer.R
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
        val renderersFactory = DefaultRenderersFactory(context)
        renderersFactory.setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        ExoPlayer.Builder(context, renderersFactory).apply {
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
    // Just to restart the delay to hide the main UI if the user performs some action (play/pause, for example)
    var lastPlayerActionTimeMillis by remember { mutableLongStateOf(0L) }

    // Hides the main UI after the time
    LaunchedEffect(
        key1 = playerState.showMainUi,
        key2 = playerState.uiOptions.autoHideButtons,
        key3 = lastPlayerActionTimeMillis
    ) {
        delay(playerState.uiOptions.timeToHideButtons)
        if (playerState.uiOptions.autoHideButtons) {
            mediaPlayerManager.onPlayerAction(PlayerAction.ChangeShowMainUi(false))
        }
    }

    var showPlayerUiOptionsDialog by remember { mutableStateOf(false) }

    PlayerView(
        mediaPlayerState = mediaPlayerManager,
        exoPlayer = exoPlayer,
        onPlayerViewClick = {
            val showMainUi = !playerState.showMainUi
            if (showMainUi) {
                Utils.showSystemBars(context)
            } else {
                Utils.hideSystemBars(context)
            }
            mediaPlayerManager.onPlayerAction(PlayerAction.ChangeShowMainUi(showMainUi))
        }
    ) {
        PlayerLayout(
            playerState = playerState,
            onPlayerAction = { action ->
                mediaPlayerManager.onPlayerAction(action)
                lastPlayerActionTimeMillis = System.currentTimeMillis()
            },
            title = exoPlayer.mediaMetadata.title.toString(),
            showMainUi = playerState.showMainUi,
            onArrowBackIconClick = onNavigateBack,
            onOptionsIconClick = { showPlayerUiOptionsDialog = true }
        )

        playerState.playbackException?.let { playbackException ->
            ErrorAlertDialog(
                playbackException = playbackException,
                onButtonOkClick = onNavigateBack
            )
        }
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

@Composable
private fun ErrorAlertDialog(
    playbackException: PlaybackException,
    onButtonOkClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.player_error)
            )
        },
        text = {
            Text(
                text = stringResource(
                    id = R.string.player_error_message,
                    playbackException.message ?: "Unknown",
                    playbackException.errorCodeName
                ),
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onButtonOkClick) {
                Text(
                    text = stringResource(id = R.string.button_ok)
                )
            }
        }
    )
}