package com.kproject.simpleplayer.presentation.screens.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kproject.simpleplayer.R
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerLayout
import com.kproject.simpleplayer.presentation.screens.components.player.PlayerUiOptions
import com.kproject.simpleplayer.presentation.theme.PreviewTheme
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PlayerUiOptionsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    uiOptions: PlayerUiOptions,
    onUiOptionsChange: (PlayerUiOptions) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            content = {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    MainContent(
                        uiOptions = uiOptions,
                        onUiOptionsChange = onUiOptionsChange
                    )
                }
            }
        )
    }
}

@Composable
private fun MainContent(
    uiOptions: PlayerUiOptions,
    onUiOptionsChange: (PlayerUiOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.player_ui_options),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(18.dp))
        PlayerLayoutOption(
            currentLayout = uiOptions.playerLayout,
            onLayoutSelected = { playerLayout ->
                onUiOptionsChange.invoke(uiOptions.copy(playerLayout = playerLayout))
            }
        )
        Spacer(Modifier.height(10.dp))
        CheckableOption(
            title = stringResource(id = R.string.player_ui_show_seek_buttons),
            isChecked = uiOptions.showSeekButtons,
            onCheckedChange = { isChecked ->
                onUiOptionsChange.invoke(uiOptions.copy(showSeekButtons = isChecked))
            }
        )
        Spacer(Modifier.height(10.dp))
        CheckableOption(
            title = stringResource(id = R.string.player_ui_auto_hide_buttons),
            isChecked = uiOptions.autoHideButtons,
            onCheckedChange = { isChecked ->
                onUiOptionsChange.invoke(uiOptions.copy(autoHideButtons = isChecked))
            }
        )
        Spacer(Modifier.height(10.dp))
        TimeToHideButtonsOption(
            currentTime = uiOptions.timeToHideButtons,
            onSelected = { time ->
                onUiOptionsChange.invoke(uiOptions.copy(timeToHideButtons = time))
            }
        )
    }
}

@Composable
private fun PlayerLayoutOption(
    currentLayout: PlayerLayout,
    onLayoutSelected: (playerLayout: PlayerLayout) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    MainCardItem(
        onClick = { showDropdownMenu = true },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                MainTextItem(text = stringResource(id = R.string.player_ui_layout))
                Spacer(Modifier.height(6.dp))
                Text(
                    text = currentLayout.name,
                    fontSize = 14.sp,
                )
            }
            val dropdownIcon = if (showDropdownMenu) {
                Icons.Default.KeyboardArrowDown
            } else {
                Icons.Default.KeyboardArrowUp
            }
            Icon(
                imageVector = dropdownIcon,
                contentDescription = null
            )
        }

        PlayerLayoutOptionsDropdownMenu(
            showOptionsMenu = showDropdownMenu,
            onDismiss = { showDropdownMenu = false },
            currentLayout = currentLayout,
            onSelected = onLayoutSelected
        )
    }
}

@Composable
private fun PlayerLayoutOptionsDropdownMenu(
    showOptionsMenu: Boolean,
    onDismiss: () -> Unit,
    currentLayout: PlayerLayout,
    onSelected: (playerLayout: PlayerLayout) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = showOptionsMenu,
        onDismissRequest = onDismiss,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    ) {
        PlayerLayout.entries.forEach { playerLayout ->
            DropdownMenuItem(
                title = playerLayout.name,
                isSelected = currentLayout == playerLayout,
                onClick = {
                    onDismiss.invoke()
                    onSelected.invoke(playerLayout)
                }
            )
        }
    }
}

@Composable
private fun CheckableOption(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    MainCardItem(
        onClick = {
            onCheckedChange.invoke(isChecked)
        },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            MainTextItem(
                text = title,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked ->
                    onCheckedChange.invoke(isChecked)
                }
            )
        }
    }
}

@Composable
private fun TimeToHideButtonsOption(
    currentTime: Long,
    onSelected: (time: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    MainCardItem(
        onClick = { showDropdownMenu = true },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                MainTextItem(text = stringResource(id = R.string.player_ui_time_to_hide_buttons))
                Spacer(Modifier.height(6.dp))
                Text(
                    text = currentTime.milliseconds.toString(),
                    fontSize = 14.sp,
                )
            }
            val dropdownIcon = if (showDropdownMenu) {
                Icons.Default.KeyboardArrowDown
            } else {
                Icons.Default.KeyboardArrowUp
            }
            Icon(
                imageVector = dropdownIcon,
                contentDescription = null
            )
        }

        TimeOptionsDropdownMenu(
            showOptionsMenu = showDropdownMenu,
            onDismiss = { showDropdownMenu = false },
            currentTime = currentTime,
            onSelected = onSelected
        )
    }
}

@Composable
private fun TimeOptionsDropdownMenu(
    showOptionsMenu: Boolean,
    onDismiss: () -> Unit,
    currentTime: Long,
    onSelected: (time: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(3000L, 5000L, 7000L, 10000L, 15000L)
    DropdownMenu(
        expanded = showOptionsMenu,
        onDismissRequest = onDismiss,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    ) {
        options.forEach { time ->
            DropdownMenuItem(
                title = time.toString(),
                isSelected = currentTime == time,
                onClick = {
                    onDismiss.invoke()
                    onSelected.invoke(time)
                }
            )
        }
    }
}

@Composable
private fun MainTextItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun MainCardItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    OutlinedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            content = content
        )
    }
}

@Composable
private fun DropdownMenuItem(
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

@PreviewLightDark
@Composable
private fun UiOptionsDialogPreview() {
    PreviewTheme {
        PlayerUiOptionsDialog(
            showDialog = true,
            onDismiss = {},
            uiOptions = PlayerUiOptions(),
            onUiOptionsChange = {}
        )
    }
}