package com.xbot.api.models.torrents

import com.xbot.api.models.common.Quality
import com.xbot.api.models.media.EpisodeRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrent(
    @SerialName("torrent_id") val torrentId: Int,
    @SerialName("episodes") val episodes: EpisodeRange,
    @SerialName("quality") val quality: Quality,
    @SerialName("leechers") val leechers: Int,
    @SerialName("seeders") val seeders: Int,
    @SerialName("downloads") val downloads: Int,
    @SerialName("total_size") val totalSize: Long,
    @SerialName("size_string") val sizeString: String,
    @SerialName("url") val url: String,
    @SerialName("magnet") val magnet: String,
    @SerialName("uploaded_timestamp") val uploadedTimestamp: Long,
    @SerialName("hash") val hash: String,
    @SerialName("metadata") val metadata: String?,
    @SerialName("raw_base64_file") val rawBase64File: String?
)