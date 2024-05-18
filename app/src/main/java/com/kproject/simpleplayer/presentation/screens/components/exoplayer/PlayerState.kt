package com.kproject.simpleplayer.presentation.screens.components.exoplayer

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.media3.common.Player
import androidx.media3.common.util.RepeatModeUtil
import androidx.media3.ui.AspectRatioFrameLayout
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerState(
    val showMainUi: Boolean = true,

    val playWhenReady: Boolean = true,
    val currentMediaItemIndex: Int = 0,
    val currentPlaybackPosition: Long = 0L,
    val bufferedPercentage: Int = 0,
    val videoDuration: Long = 0L,
    val playbackState: PlaybackState = PlaybackState.Idle,
    val seekIncrementMs: Long = 5000L,

    val resizeMode: ResizeMode = ResizeMode.Fit,
    val repeatMode: RepeatMode = RepeatMode.None,
    val isPlaying: Boolean = true,
    val isNextButtonAvailable: Boolean = true,
    val isSeekForwardButtonAvailable: Boolean = true,
    val isSeekBackButtonAvailable: Boolean = true,
    val playbackSpeed: Float = playbackSpeedNormal.speedValue,
    val isLandscapeMode: Boolean = false,
) : Parcelable {
    val isStateBuffering: Boolean
        get() = playbackState == PlaybackState.Buffering
}

sealed class PlayerAction {
    data object PlayOrPause : PlayerAction()
    data object Next : PlayerAction()
    data object Previous : PlayerAction()
    data object SeekForward : PlayerAction()
    data object SeekBack : PlayerAction()
    data class SeekTo(val positionMs: Long) : PlayerAction()

    data class ChangeShowMainUi(val showMainUi: Boolean) : PlayerAction()
    data class ChangeCurrentPlaybackPosition(val currentPlaybackPosition: Long) : PlayerAction()
    data class ChangeBufferedPercentage(val bufferedPercentage: Int) : PlayerAction()
    data class ChangeResizeMode(val resizeMode: ResizeMode) : PlayerAction()
    data class ChangeRepeatMode(val repeatMode: RepeatMode) : PlayerAction()
    data class ChangePlaybackSpeed(val playbackSpeed: Float) : PlayerAction()
    data class ChangeIsLandscapeMode(val isLandscapeMode: Boolean) : PlayerAction()
}

enum class PlaybackState(val value: Int) {
    Idle(Player.STATE_IDLE),
    Buffering(Player.STATE_BUFFERING),
    Ready(Player.STATE_READY),
    Ended(Player.STATE_ENDED);

    companion object {
        fun Int.toPlaybackState(): PlaybackState {
            return when (this) {
                Player.STATE_IDLE -> Idle
                Player.STATE_BUFFERING -> Buffering
                Player.STATE_READY -> Ready
                Player.STATE_ENDED -> Ended
                else -> Idle
            }
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
enum class ResizeMode(val value: Int) {
    Fit(AspectRatioFrameLayout.RESIZE_MODE_FIT),
    Fill(AspectRatioFrameLayout.RESIZE_MODE_FILL),
    Zoom(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

    companion object {
        fun getNewResizeMode(currentResizeMode: ResizeMode): ResizeMode {
            return when (currentResizeMode) {
                Fit -> Fill
                Fill -> Zoom
                Zoom -> Fit
            }
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
enum class RepeatMode(val value: Int) {
    None(RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE),
    One(RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE),
    ModeAll(RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL);

    companion object {
        fun getNewRepeatMode(currentRepeatMode: RepeatMode): RepeatMode {
            return when (currentRepeatMode) {
                None -> One
                One -> ModeAll
                ModeAll -> None
            }
        }
    }
}

data class PlaybackSpeed(
    val speedValue: Float,
    val title: String
)

val playbackSpeedNormal = PlaybackSpeed(1.0f, "Normal")
val playbackSpeedOptions = listOf(
    PlaybackSpeed(0.25f, "0.25x"),
    PlaybackSpeed(0.5f, "0.5x"),
    PlaybackSpeed(0.75f, "0.75x"),
    playbackSpeedNormal,
    PlaybackSpeed(1.25f, "1.25x"),
    PlaybackSpeed(1.5f, "1.5x"),
    PlaybackSpeed(1.75f, "1.75x"),
    PlaybackSpeed(2.0f, "2x")
)

@Parcelize
data class VideoSize(
    val width: Int,
    val height: Int
) : Parcelable