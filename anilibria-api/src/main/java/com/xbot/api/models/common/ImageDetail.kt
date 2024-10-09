package com.xbot.api.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDetail(
    @SerialName("url") val url: String,
    @SerialName("raw_base64_file") val rawBase64File: String? = null
)
