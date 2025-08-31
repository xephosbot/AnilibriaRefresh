package com.xbot.network.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class TimecodeApi(
    val episodeId: String,
    val time: Float,
    val isWatched: Boolean
)
