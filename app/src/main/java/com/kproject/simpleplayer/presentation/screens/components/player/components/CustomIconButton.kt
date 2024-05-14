package com.kproject.simpleplayer.presentation.screens.components.player.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    reduceIconSize: Boolean = false,
    colors: IconButtonColors? = null,
    filledDefaultContainerColor: Boolean = true,
) {
    val defaultContainerColor = if (filledDefaultContainerColor) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Unspecified
    }
    val defaultDisabledContainerColor = if (filledDefaultContainerColor) {
        defaultContainerColor.copy(0.40f)
    } else {
        Color.Unspecified
    }
    val iconColors = colors ?: IconButtonDefaults.iconButtonColors(
        containerColor = defaultContainerColor,
        contentColor = Color.White,
        disabledContainerColor = defaultDisabledContainerColor,
        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.40f)
    )
    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = iconColors,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .size(48.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .padding(if (reduceIconSize) 2.dp else 0.dp)
        )
    }
}