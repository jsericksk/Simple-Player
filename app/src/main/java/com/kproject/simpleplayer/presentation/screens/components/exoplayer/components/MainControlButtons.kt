package com.kproject.simpleplayer.presentation.screens.components.exoplayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kproject.simpleplayer.R
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.PlaybackState
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.PlayerAction
import com.kproject.simpleplayer.presentation.theme.PreviewTheme

@Composable
fun MainControlButtons(
    isNextButtonAvailable: Boolean,
    isSeekForwardButtonAvailable: Boolean,
    isSeekBackButtonAvailable: Boolean,
    playbackState: PlaybackState,
    isPlaying: Boolean,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        CustomIconButton(
            iconResId = R.drawable.round_replay_5_24,
            enabled = isSeekBackButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.SeekBack)
            }
        )
        CustomIconButton(
            iconResId = R.drawable.round_skip_previous_24,
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
        CustomIconButton(
            iconResId = centerIcon,
            onClick = {
                onPlayerAction.invoke(PlayerAction.PlayOrPause)
            }
        )

        CustomIconButton(
            iconResId = R.drawable.round_skip_next_24,
            enabled = isNextButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.Next)
            }
        )
        CustomIconButton(
            iconResId = R.drawable.round_forward_5_24,
            enabled = isSeekForwardButtonAvailable,
            onClick = {
                onPlayerAction.invoke(PlayerAction.SeekForward)
            }
        )
    }
}

@Preview
@Composable
private fun MainControlButtonsPreview() {
    PreviewTheme {
        Column {
            MainControlButtons(
                isNextButtonAvailable = true,
                isSeekForwardButtonAvailable = true,
                isSeekBackButtonAvailable = true,
                playbackState = PlaybackState.Ready,
                isPlaying = true,
                onPlayerAction = {},
            )
        }
    }
}