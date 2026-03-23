package com.xbot.network.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileTorrentsDto(
    @SerialName("passkey") val passkey: String,
    @SerialName("uploaded") val uploaded: Int?,
    @SerialName("downloaded") val downloaded: Int?
)