package com.kproject.simpleplayer.presentation.screens.components.exoplayer.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kproject.simpleplayer.R
import com.kproject.simpleplayer.presentation.theme.PreviewTheme

@Composable
fun CustomIconButton(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    reducedIconSize: Boolean = false
) {
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.40f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.40f)
        ),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .size(54.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .padding(if (reducedIconSize) 2.dp else 0.dp)
        )
    }
}

@Preview
@Composable
private fun CustomIconButtonPreview() {
    PreviewTheme {
        Column {
            CustomIconButton(
                iconResId = R.drawable.round_play_arrow_24,
                onClick = {}
            )
            Spacer(Modifier.height(24.dp))
            CustomIconButton(
                iconResId = R.drawable.round_aspect_ratio_24,
                onClick = {},
                reducedIconSize = true
            )
            Spacer(Modifier.height(24.dp))
            CustomIconButton(
                iconResId = R.drawable.round_skip_next_24,
                onClick = {},
                enabled = false
            )
        }
    }
}