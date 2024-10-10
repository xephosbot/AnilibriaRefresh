package com.xbot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    val code: Int,
    val description: String
)
