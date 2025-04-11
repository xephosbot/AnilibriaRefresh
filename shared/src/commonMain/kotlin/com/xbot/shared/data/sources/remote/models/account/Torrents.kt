package com.xbot.shared.data.sources.remote.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrents(
    @SerialName("passkey") val passkey: String,
    @SerialName("uploaded") val uploaded: Int?,
    @SerialName("downloaded") val downloaded: Int?
)
