package com.kproject.simpleplayer.presentation.screens.player

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.kproject.simpleplayer.presentation.screens.player.model.MediaType
import com.kproject.simpleplayer.presentation.screens.player.model.assetMedia
import com.kproject.simpleplayer.presentation.screens.player.model.remoteMedia

class PlayerViewModel : ViewModel() {

    fun getMediaItemList(mediaType: MediaType): List<MediaItem> {
        val mediaUriList = when (mediaType) {
            MediaType.AssetVideos -> assetMedia
            MediaType.RemoteVideos -> remoteMedia
        }
        val mediaItemList = mutableListOf<MediaItem>()
        mediaUriList.forEachIndexed { index, media ->
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(media.title)
                .build()
            val mediaItem = MediaItem.Builder()
                .setUri(media.uri)
                .setMediaId(index.toString())
                .setMediaMetadata(mediaMetadata)
                .build()
            mediaItemList.add(mediaItem)
        }
        return mediaItemList
    }
}