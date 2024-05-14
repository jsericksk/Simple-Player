package com.kproject.simpleplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import com.kproject.simpleplayer.presentation.screens.home.HomeScreen
import com.kproject.simpleplayer.presentation.theme.SimplePlayerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimplePlayerTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Navigator(screen = HomeScreen()) { navigator ->
                        SlideTransition(
                            navigator = navigator,
                            orientation = SlideOrientation.Horizontal
                        )
                    }
                }
            }
        }
    }
}