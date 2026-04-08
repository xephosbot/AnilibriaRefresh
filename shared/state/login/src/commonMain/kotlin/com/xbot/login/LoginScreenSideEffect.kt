package com.xbot.login

sealed interface LoginScreenSideEffect {
    data object NavigateBack : LoginScreenSideEffect
    data class ShowErrorMessage(val error: Throwable) : LoginScreenSideEffect
    data object LoginSuccess : LoginScreenSideEffect
}
