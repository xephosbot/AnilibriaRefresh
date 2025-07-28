package com.xbot.network.models.entities.media

import com.xbot.network.models.entities.anime.ReleaseApi
import com.xbot.network.models.entities.common.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionApi(
    @SerialName("id") val id: String,
    @SerialName("url") val url: String? = null,
    @SerialName("url_label") val urlLabel: String? = null,
    @SerialName("image") val image: Image,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("is_ad") val isAd: Boolean,
    @SerialName("ad_erid") val adErid: String? = null,
    @SerialName("ad_origin") val adOrigin: String? = null,
    @SerialName("release") val release: ReleaseApi? = null,
    @SerialName("has_overlay") val hasOverlay: Boolean
)