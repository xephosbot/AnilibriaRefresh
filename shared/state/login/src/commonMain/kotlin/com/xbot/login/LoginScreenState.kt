package com.xbot.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
