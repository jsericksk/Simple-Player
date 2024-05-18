package com.kproject.simpleplayer.presentation.screens.components.exoplayer.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.simpleplayer.presentation.screens.components.exoplayer.playbackSpeedOptions

@Composable
fun PlaybackSpeedDropdownMenu(
    showOptionsMenu: Boolean,
    onDismiss: () -> Unit,
    currentPlaybackSpeed: Float,
    onSelected: (playbackSpeed: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = showOptionsMenu,
        onDismissRequest = onDismiss,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    ) {
        playbackSpeedOptions.forEach { playbackSpeed ->
            PlaybackSpeedDropdownMenuItem(
                title = playbackSpeed.title,
                isSelected = currentPlaybackSpeed == playbackSpeed.speedValue,
                onClick = {
                    onDismiss.invoke()
                    onSelected.invoke(playbackSpeed.speedValue)
                }
            )
        }
    }
}

@Composable
private fun PlaybackSpeedDropdownMenuItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
        text = {
            Text(
                text = title,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null
                )
            }
        },
        contentPadding = PaddingValues(12.dp)
    )
}