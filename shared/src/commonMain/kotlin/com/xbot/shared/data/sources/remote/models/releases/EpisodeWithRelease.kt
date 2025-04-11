package com.xbot.shared.data.sources.remote.models.releases


import com.xbot.shared.data.sources.remote.models.shared.PosterApi
import com.xbot.shared.data.sources.remote.models.shared.EpisodeApi
import com.xbot.shared.data.sources.remote.models.shared.ReleaseApi
import kotlinx.serialization.SerialName

data class EpisodeWithRelease(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
    @SerialName("ordinal") val ordinal: Float,
    @SerialName("opening") val opening: EpisodeApi.TimeRange,
    @SerialName("ending") val ending: EpisodeApi.TimeRange,
    @SerialName("preview") val preview: PosterApi,
    @SerialName("hls_480") val hls480: String?,
    @SerialName("hls_720") val hls720: String?,
    @SerialName("hls_1080") val hls1080: String?,
    @SerialName("duration") val duration: Int,
    @SerialName("rutube_id") val rutubeId: String?,
    @SerialName("youtube_id") val youtubeId: String?,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("name_english") val nameEnglish: String?,
    @SerialName("release") val release: ReleaseApi
)
