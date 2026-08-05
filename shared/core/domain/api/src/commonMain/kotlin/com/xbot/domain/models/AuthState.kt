package com.xbot.domain.models

import com.xbot.common.error.AppError

sealed class AuthState {
    data class Unauthenticated(val error: AppError?) : AuthState()
    data class Authenticated(val user: User) : AuthState()
}
