package com.xbot.player

import com.xbot.common.AsyncResult
import com.xbot.domain.models.DomainError
import com.xbot.domain.models.Episode
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlayerScreenState(
    @Transient val episodes: AsyncResult<DomainError, List<Episode>> = AsyncResult.Loading,
    @Transient val currentEpisode: Episode? = null,
    val quality: VideoQuality = VideoQuality.FHD,
) {
    val availableQualities: List<VideoQuality>
        get() = currentEpisode?.availableQualities ?: emptyList()

    val videoUri: String?
        get() = currentEpisode?.getVideoUri(quality)
}

enum class VideoQuality(val title: String) {
    SD("480p"),
    HD("720p"),
    FHD("1080p")
}

internal val Episode.availableQualities: List<VideoQuality>
    get() = buildList {
        if (!hls480.isNullOrEmpty()) add(VideoQuality.SD)
        if (!hls720.isNullOrEmpty()) add(VideoQuality.HD)
        if (!hls1080.isNullOrEmpty()) add(VideoQuality.FHD)
    }

internal fun Episode.getVideoUri(quality: VideoQuality): String? {
    return when (quality) {
        VideoQuality.SD -> hls480
        VideoQuality.HD -> hls720
        VideoQuality.FHD -> hls1080
    }
}
