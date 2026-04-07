package com.xbot.login

sealed interface LoginScreenAction {
    data class Login(val username: String, val password: String) : LoginScreenAction
    data object Logout : LoginScreenAction
}
