package com.xbot.api.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrents(
    @SerialName("passkey") var passkey: String,
    @SerialName("uploaded") var uploaded: Int,
    @SerialName("downloaded") var downloaded: Int
)
