package com.kproject.simpleplayer.presentation.screens.components.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val PLAYER_STATE_KEY = "playerState"

class PlayerStateHolder(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var playerState: PlayerState by mutableStateOf(PlayerState())
        private set

    init {
        savedStateHandle.get<PlayerState>(PLAYER_STATE_KEY)?.let { playerState = it }
    }

    fun onPlayerStateChange(newPlayerState: PlayerState) {
        playerState = newPlayerState
        savedStateHandle[PLAYER_STATE_KEY] = newPlayerState
    }
}