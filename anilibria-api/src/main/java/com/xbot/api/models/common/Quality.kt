package com.xbot.api.models.common

import kotlinx.serialization.Serializable

@Serializable
data class Quality(
    val string: String,
    val type: String,
    val resolution: String,
    val encoder: String,
    val lq_audio: String?
)