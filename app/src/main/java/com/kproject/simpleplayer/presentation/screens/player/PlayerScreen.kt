package com.kproject.simpleplayer.presentation.screens.player

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.kproject.simpleplayer.presentation.commom.Utils
import com.kproject.simpleplayer.presentation.screens.components.LockScreenOrientation
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.PlayerAction
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.PlayerState
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.SeekBackIncrement
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.SeekForwardIncrement
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.components.MainControlButtons
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.components.ProgressContent
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.components.SecondaryControlButtons
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.rememberMediaPlayerManager
import com.kproject.simpleplayer.presentation.screens.player.model.mediaList
import com.kproject.simpleplayer.presentation.theme.PreviewTheme
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen() {
    val mediaItems = remember { getMediaItemList() }
    val context = LocalContext.current
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
    // Just to restart the delay to hide the main UI if the user performs some action (play/pause, for example)
    var lastPlayerActionTimeMillis by remember { mutableLongStateOf(0L) }

    // Hides the main UI after the time
    LaunchedEffect(
        key1 = playerState.showMainUi,
        key2 = lastPlayerActionTimeMillis
    ) {
        delay(5000L)
        mediaPlayerManager.onPlayerAction(PlayerAction.ChangeShowMainUi(false))
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
        )

        PlayerLayout(
            playerState = playerState,
            onPlayerAction = { action ->
                mediaPlayerManager.onPlayerAction(action)
                lastPlayerActionTimeMillis = System.currentTimeMillis()
            },
            title = exoPlayer.mediaMetadata.title.toString(),
            showMainUi = playerState.showMainUi
        )
    }

    LockScreenOrientation(isLandscapeMode = playerState.isLandscapeMode)
}

@Composable
private fun PlayerLayout(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    title: String,
    showMainUi: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
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
            TopBarTitle(title = title)
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
}

@kotlin.OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun TopBarTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.6f)
        ),
        modifier = modifier
    )
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
            currentBufferedPercentage = playerState.bufferedPercentage,
            videoDuration = playerState.videoDuration,
            onPlayerAction = onPlayerAction,
        )
        Spacer(Modifier.height(8.dp))
        MainControlButtons(
            isNextButtonAvailable = playerState.isNextButtonAvailable,
            isSeekForwardButtonAvailable = playerState.isSeekForwardButtonAvailable,
            isSeekBackButtonAvailable = playerState.isSeekBackButtonAvailable,
            playbackState = playerState.playbackState,
            isPlaying = playerState.isPlaying,
            onPlayerAction = onPlayerAction,
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        PlayerLayout(
            playerState = PlayerState(
                currentPlaybackPosition = 3000,
                videoDuration = 10000,
                bufferedPercentage = 60
            ),
            onPlayerAction = {},
            title = "Video Title",
            showMainUi = true
        )
    }
}

private fun getMediaItemList(): List<MediaItem> {
    val mediaItemList = mutableListOf<MediaItem>()
    mediaList.forEachIndexed { index, media ->
        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(media.title)
            .build()
        val mediaItem = MediaItem.Builder()
            .setUri(media.uri)
            .setMediaId(index.toString())
            .setMediaMetadata(mediaMetadata)
            .build()
        mediaItemList.add(mediaItem)
    }
    return mediaItemList
}