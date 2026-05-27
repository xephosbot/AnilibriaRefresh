package com.xbot.login

sealed interface LoginScreenSideEffect {
    data class ShowErrorMessage(val error: Throwable) : LoginScreenSideEffect
    data object LoginSuccess : LoginScreenSideEffect
}
