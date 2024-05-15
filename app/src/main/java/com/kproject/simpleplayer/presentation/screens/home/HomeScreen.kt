package com.kproject.simpleplayer.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.simpleplayer.R
import com.kproject.simpleplayer.presentation.screens.player.model.MediaType
import com.kproject.simpleplayer.presentation.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayerScreen: (mediaType: MediaType) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            ButtonItem(
                title = stringResource(id = R.string.assets_videos),
                onClick = {
                    onNavigateToPlayerScreen.invoke(MediaType.AssetVideos)
                }
            )
            Spacer(Modifier.height(16.dp))
            ButtonItem(
                title = stringResource(id = R.string.remote_videos),
                onClick = {
                    onNavigateToPlayerScreen.invoke(MediaType.RemoteVideos)
                }
            )
        }
    }
}

@Composable
private fun ButtonItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(18.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PreviewTheme {
        HomeScreen(
            onNavigateToPlayerScreen = {}
        )
    }
}