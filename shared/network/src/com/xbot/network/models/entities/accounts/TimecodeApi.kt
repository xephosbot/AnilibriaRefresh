package com.xbot.network.models.entities.accounts

import kotlinx.serialization.Serializable

@Serializable
data class TimecodeApi(
    val episodeId: String,
    val time: Float,
    val isWatched: Boolean
)
