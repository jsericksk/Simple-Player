package com.kproject.simpleplayer.presentation.screens.player.model

data class Media(
    val uri: String,
    val title: String,
)

val mediaList = listOf(
    Media(
        uri = "https://storage.googleapis.com/downloads.webmproject.org/av1/exoplayer/bbb-av1-480p.mp4",
        title = "MP4: Big Buck Bunny"
    ),
    Media(
        uri = "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv",
        title = "MKV (1280x720): Android Screens"
    ),
    Media(
        uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8",
        title = "HLS (adaptive): Apple 16x9 basic stream"
    )
)