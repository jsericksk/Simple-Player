package com.kproject.simpleplayer.presentation.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.kproject.simpleplayer.presentation.commom.Utils

@Composable
fun LockScreenOrientation(isLandscapeMode: Boolean) {
    val context = LocalContext.current
    LaunchedEffect(isLandscapeMode) {
        Utils.changeScreenOrientation(
            context = context,
            isLandscapeMode = isLandscapeMode
        )
    }
}