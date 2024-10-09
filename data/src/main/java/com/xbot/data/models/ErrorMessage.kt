package com.xbot.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    val error: Error
)

@Serializable
data class Error(
    val code: Int,
    val message: String
)
