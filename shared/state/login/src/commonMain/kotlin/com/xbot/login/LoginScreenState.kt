package com.xbot.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
