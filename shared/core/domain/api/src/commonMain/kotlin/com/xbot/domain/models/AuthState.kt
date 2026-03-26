package com.xbot.domain.models

sealed class AuthState {
    data class Unauthenticated(val error: DomainError?) : AuthState()
    data class Authenticated(val user: User) : AuthState()
}
