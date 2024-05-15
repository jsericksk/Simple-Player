package com.kproject.simpleplayer.presentation.screens.components.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kproject.simpleplayer.R
import com.kproject.simpleplayer.presentation.screens.components.player.PlaybackState
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerAction
import com.kproject.simpleplayer.presentation.theme.PreviewTheme

@Composable
fun MainControlButtons(
    showSeekButtons: Boolean,
    isNextButtonAvailable: Boolean,
    isSeekForwardButtonAvailable: Boolean,
    isSeekBackButtonAvailable: Boolean,
    playbackState: PlaybackState,
    isPlaying: Boolean,
    isStateBuffering: Boolean,
    onPlayerAction: (PlayerAction) -> Unit,
    allButtonColorsFilled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        if (showSeekButtons) {
            CustomIconButton(
                iconResId = R.drawable.round_replay_5_24,
                enabled = isSeekBackButtonAvailable,
                filledDefaultContainerColor = allButtonColorsFilled,
                onClick = {
                    onPlayerAction.invoke(PlayerAction.SeekBack)
                }
            )
        }
        CustomIconButton(
            iconResId = R.drawable.round_skip_previous_24,
            filledDefaultContainerColor = allButtonColorsFilled,
            onClick = {
                onPlayerAction.invoke(PlayerAction.Previous)
            }
        )

        val centerIcon = if (playbackState == PlaybackState.Ended) {
            R.drawable.round_replay_24
        } else if (isPlaying) {
            R.drawable.round_pause_24
        } else {
            R.drawable.round_play_arrow_24
        }
        Box {
            CustomIconButton(
                iconResId = centerIcon,
                filledDefaultContainerColor = true,
                onClick = {
                    onPlayerAction.invoke(PlayerAction.PlayOrPause)
                }
            )
            if (isStateBuffering) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .matchParentSize(),
                )
            }
        }
        CustomIconButton(
            iconResId = R.drawable.round_skip_next_24,
            enabled = isNextButtonAvailable,
            filledDefaultContainerColor = allButtonColorsFilled,
            onClick = {
                onPlayerAction.invoke(PlayerAction.Next)
            }
        )
        if (showSeekButtons) {
            CustomIconButton(
                iconResId = R.drawable.round_forward_5_24,
                enabled = isSeekForwardButtonAvailable,
                filledDefaultContainerColor = allButtonColorsFilled,
                onClick = {
                    onPlayerAction.invoke(PlayerAction.SeekForward)
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainControlButtonsPreview() {
    PreviewTheme {
        Column {
            MainControlButtons(
                showSeekButtons = true,
                isNextButtonAvailable = true,
                isSeekForwardButtonAvailable = true,
                isSeekBackButtonAvailable = true,
                playbackState = PlaybackState.Ready,
                isPlaying = true,
                isStateBuffering = false,
                onPlayerAction = {},
                allButtonColorsFilled = true
            )
            Spacer(Modifier.height(12.dp))
            MainControlButtons(
                showSeekButtons = false,
                isNextButtonAvailable = true,
                isSeekForwardButtonAvailable = true,
                isSeekBackButtonAvailable = true,
                playbackState = PlaybackState.Ready,
                isPlaying = true,
                isStateBuffering = false,
                onPlayerAction = {},
                allButtonColorsFilled = true
            )
            Spacer(Modifier.height(12.dp))
            MainControlButtons(
                showSeekButtons = true,
                isNextButtonAvailable = false,
                isSeekForwardButtonAvailable = true,
                isSeekBackButtonAvailable = true,
                playbackState = PlaybackState.Ready,
                isPlaying = true,
                isStateBuffering = false,
                onPlayerAction = {},
                allButtonColorsFilled = false
            )
        }
    }
}