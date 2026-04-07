package com.xbot.login

sealed interface LoginScreenAction {
    data class UpdateUsername(val username: String) : LoginScreenAction
    data class UpdatePassword(val password: String) : LoginScreenAction
    data object Login : LoginScreenAction
    data object Logout : LoginScreenAction
}
