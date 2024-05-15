package com.kproject.simpleplayer.presentation.screens.components.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kproject.simpleplayer.R
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerAction
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerState
import com.kproject.simpleplayer.presentation.screens.components.player.RepeatMode
import com.kproject.simpleplayer.presentation.screens.components.player.ResizeMode
import com.kproject.simpleplayer.presentation.screens.components.player.fakePlayerState
import com.kproject.simpleplayer.presentation.theme.PreviewTheme

@Composable
fun SecondaryControlButtons(
    resizeMode: ResizeMode,
    repeatMode: RepeatMode,
    playbackSpeed: Float,
    isLandscapeMode: Boolean,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier

    /*playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier*/
) {
    /*val resizeMode = remember(playerState.resizeMode) { playerState.resizeMode }
    val repeatMode = remember(playerState.repeatMode) { playerState.repeatMode }
    val playbackSpeed = remember(playerState.playbackSpeed) { playerState.playbackSpeed }
    val isLandscapeMode = remember(playerState.isLandscapeMode) { playerState.isLandscapeMode }
*/
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        var showPlaybackSpeedDropdownMenu by remember { mutableStateOf(false) }
        Box(modifier = modifier.padding(end = 12.dp)) {
            PlaybackSpeedDropdownMenu(
                showOptionsMenu = showPlaybackSpeedDropdownMenu,
                onDismiss = { showPlaybackSpeedDropdownMenu = false },
                currentPlaybackSpeed = playbackSpeed,
                onSelected = { playbackSpeed ->
                    onPlayerAction.invoke(
                        PlayerAction.ChangePlaybackSpeed(playbackSpeed)
                    )
                }
            )
        }

        CustomIconButton(
            iconResId = R.drawable.round_slow_motion_video_24,
            onClick = { showPlaybackSpeedDropdownMenu = true },
            modifier = Modifier
        )

        Spacer(Modifier.width(4.dp))

        val repeatModeIconResId = when (repeatMode) {
            RepeatMode.None -> R.drawable.round_repeat_24
            RepeatMode.One -> R.drawable.round_repeat_one_24
            RepeatMode.ModeAll -> R.drawable.round_repeat_on_24
        }
        CustomIconButton(
            iconResId = repeatModeIconResId,
            onClick = {
                val newRepeatMode = RepeatMode.getNewRepeatMode(repeatMode)
                onPlayerAction.invoke(PlayerAction.ChangeRepeatMode(newRepeatMode))
            },
            reduceIconSize = true,
            modifier = Modifier
        )

        Spacer(Modifier.width(4.dp))

        CustomIconButton(
            iconResId = R.drawable.round_screen_rotation_24,
            onClick = {
                onPlayerAction.invoke(PlayerAction.ChangeIsLandscapeMode(!isLandscapeMode))
            },
            reduceIconSize = true,
            modifier = Modifier
        )

        Spacer(Modifier.width(4.dp))

        CustomIconButton(
            iconResId = R.drawable.round_aspect_ratio_24,
            reduceIconSize = true,
            onClick = {
                val newResizeMode = ResizeMode.getNewResizeMode(resizeMode)
                onPlayerAction.invoke(PlayerAction.ChangeResizeMode(newResizeMode))
            }
        )

        Spacer(Modifier.width(8.dp))
    }
}

@Composable
fun SecondaryControlButtons2(
    resizeMode: (() -> ResizeMode),
    repeatMode: (() -> RepeatMode),
    playbackSpeed: (() -> Float),
    isLandscapeMode: (() -> Boolean),
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val resizeModeRem = remember(resizeMode()) { resizeMode() }
    val repeatModeRem = remember(repeatMode()) { repeatMode() }
    val playbackSpeedRem = remember(playbackSpeed()) { playbackSpeed() }
    val isLandscapeModeRem = remember(isLandscapeMode()) { isLandscapeMode() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        var showPlaybackSpeedDropdownMenu by remember { mutableStateOf(false) }
        Box(modifier = modifier.padding(end = 12.dp)) {
            PlaybackSpeedDropdownMenu(
                showOptionsMenu = showPlaybackSpeedDropdownMenu,
                onDismiss = { showPlaybackSpeedDropdownMenu = false },
                currentPlaybackSpeed = playbackSpeed(),
                onSelected = { playbackSpeed ->
                    onPlayerAction.invoke(
                        PlayerAction.ChangePlaybackSpeed(playbackSpeed)
                    )
                }
            )
        }

        CustomIconButton(
            iconResId = R.drawable.round_slow_motion_video_24,
            onClick = { showPlaybackSpeedDropdownMenu = true },
            modifier = Modifier
        )

        Spacer(Modifier.width(4.dp))

        val repeatModeIconResId = when (repeatMode()) {
            RepeatMode.None -> R.drawable.round_repeat_24
            RepeatMode.One -> R.drawable.round_repeat_one_24
            RepeatMode.ModeAll -> R.drawable.round_repeat_on_24
        }
        CustomIconButton(
            iconResId = repeatModeIconResId,
            onClick = {
                val newRepeatMode = RepeatMode.getNewRepeatMode(repeatMode())
                onPlayerAction.invoke(PlayerAction.ChangeRepeatMode(newRepeatMode))
            },
            reduceIconSize = true,
            modifier = Modifier
        )

        Spacer(Modifier.width(4.dp))

        CustomIconButton(
            iconResId = R.drawable.round_screen_rotation_24,
            onClick = {
                onPlayerAction.invoke(PlayerAction.ChangeIsLandscapeMode(!isLandscapeMode()))
            },
            reduceIconSize = true,
            modifier = Modifier
        )

        Spacer(Modifier.width(4.dp))

        CustomIconButton(
            iconResId = R.drawable.round_aspect_ratio_24,
            reduceIconSize = true,
            onClick = {
                val newResizeMode = ResizeMode.getNewResizeMode(resizeMode())
                onPlayerAction.invoke(PlayerAction.ChangeResizeMode(newResizeMode))
            }
        )

        Spacer(Modifier.width(8.dp))
    }
}

@Preview
@Composable
private fun SecondaryControlButtonsPreview() {
    PreviewTheme {
        /*SecondaryControlButtons(
            playerState = fakePlayerState,
            onPlayerAction = {}
        )*/
    }
}