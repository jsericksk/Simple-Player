package com.kproject.simpleplayer.presentation.screens.components.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.simpleplayer.presentation.commom.Utils
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerAction
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerState
import com.kproject.simpleplayer.presentation.screens.components.player.fakePlayerState
import com.kproject.simpleplayer.presentation.theme.PreviewTheme
import dev.vivvvek.seeker.Seeker
import dev.vivvvek.seeker.SeekerDefaults

private val horizontalPadding = 10.dp
private val textColor = Color.White
private val textFontSize = 14.sp

@Composable
fun ProgressContent(
    playerState: PlayerState,
    onPlayerAction: (PlayerAction) -> Unit,
    progressTextLayout: ProgressTextLayout,
    modifier: Modifier = Modifier
) {
    var isChangingPosition by remember { mutableStateOf(false) }
    var temporaryPlaybackPosition by remember { mutableLongStateOf(0L) }
    val seekerValue = remember(playerState.currentPlaybackPosition, isChangingPosition) {
        if (isChangingPosition) temporaryPlaybackPosition else playerState.currentPlaybackPosition
    }

    val currentVideoTime = remember(seekerValue) {
        Utils.formatVideoDuration(seekerValue)
    }
    val videoDuration = remember(playerState.videoDuration) {
        Utils.formatVideoDuration(playerState.videoDuration)
    }

    MainContent(
        progressTextLayout = progressTextLayout,
        currentVideoTime = currentVideoTime,
        videoDuration = videoDuration,
        modifier = modifier
    ) { progressModifier ->
        Seeker(
            value = seekerValue.toFloat(),
            readAheadValue = playerState.currentBufferedPercentage,
            onValueChange = { value ->
                temporaryPlaybackPosition = value.toLong()
                isChangingPosition = true
                onPlayerAction.invoke(
                    PlayerAction.ChangeCurrentPlaybackPosition(temporaryPlaybackPosition)
                )
            },
            onValueChangeFinished = {
                isChangingPosition = false
                onPlayerAction.invoke(PlayerAction.SeekTo(temporaryPlaybackPosition))
                temporaryPlaybackPosition = 0
            },
            range = 0f..playerState.videoDuration.toFloat(),
            colors = SeekerDefaults.seekerColors(
                progressColor = MaterialTheme.colorScheme.primaryContainer,
                thumbColor = MaterialTheme.colorScheme.primaryContainer,
                readAheadColor = MaterialTheme.colorScheme.primaryContainer.copy(0.8f)
            ),
            dimensions = SeekerDefaults.seekerDimensions(
                trackHeight = 6.dp,
                thumbRadius = 11.dp
            ),
            modifier = progressModifier
        )
    }
}

@Composable
private fun MainContent(
    progressTextLayout: ProgressTextLayout,
    currentVideoTime: String,
    videoDuration: String,
    modifier: Modifier = Modifier,
    progressContent: @Composable (modifier: Modifier) -> Unit,
) {
    when (progressTextLayout) {
        ProgressTextLayout.Default -> {
            DefaultLayout(
                currentVideoTime = currentVideoTime,
                videoDuration = videoDuration,
                progressContent = progressContent,
                modifier = modifier
            )
        }
        ProgressTextLayout.YouTube -> {
            YouTubeLayout(
                currentVideoTime = currentVideoTime,
                videoDuration = videoDuration,
                progressContent = progressContent,
                modifier = modifier
            )
        }
        ProgressTextLayout.ExoPlayer -> {
            ExoPlayerLayout(
                currentVideoTime = currentVideoTime,
                videoDuration = videoDuration,
                progressContent = progressContent,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DefaultLayout(
    currentVideoTime: String,
    videoDuration: String,
    modifier: Modifier = Modifier,
    progressContent: @Composable (modifier: Modifier) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) {
        Text(
            text = currentVideoTime,
            color = textColor,
            fontSize = textFontSize,
        )
        Spacer(Modifier.width(8.dp))
        progressContent(Modifier.weight(1f))
        Spacer(Modifier.width(8.dp))
        Text(
            text = videoDuration,
            fontSize = textFontSize,
            color = textColor
        )
    }
}

@Composable
private fun YouTubeLayout(
    currentVideoTime: String,
    videoDuration: String,
    modifier: Modifier = Modifier,
    progressContent: @Composable (Modifier) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) {
        Row(modifier = Modifier.padding(start = horizontalPadding + 4.dp)) {
            Text(
                text = currentVideoTime,
                color = textColor,
                fontSize = textFontSize,
            )
            Text(
                text = "/",
                color = textColor,
                fontSize = textFontSize,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = videoDuration,
                color = textColor,
                fontSize = textFontSize,
            )
        }
        progressContent(Modifier)
    }
}

@Composable
private fun ExoPlayerLayout(
    currentVideoTime: String,
    videoDuration: String,
    modifier: Modifier = Modifier,
    progressContent: @Composable (modifier: Modifier) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    ) {
        progressContent(Modifier)
        Row(modifier = Modifier.padding(start = horizontalPadding + 4.dp)) {
            Text(
                text = currentVideoTime,
                fontSize = textFontSize,
                color = textColor
            )
            Text(
                text = "•",
                color = textColor,
                fontSize = textFontSize,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = videoDuration,
                color = textColor,
                fontSize = textFontSize,
            )
        }
    }
}

enum class ProgressTextLayout {
    Default,
    YouTube,
    ExoPlayer
}

@Preview
@Composable
private fun ProgressContentPreview() {
    PreviewTheme {
        Column {
            ProgressContent(
                playerState = fakePlayerState,
                onPlayerAction = {},
                progressTextLayout = ProgressTextLayout.Default
            )
            Spacer(Modifier.height(20.dp))
            ProgressContent(
                playerState = fakePlayerState,
                onPlayerAction = {},
                progressTextLayout = ProgressTextLayout.YouTube
            )
            Spacer(Modifier.height(20.dp))
            ProgressContent(
                playerState = fakePlayerState,
                onPlayerAction = {},
                progressTextLayout = ProgressTextLayout.ExoPlayer
            )
        }
    }
}