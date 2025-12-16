package com.xbot.login.navigation

import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute : NavKey

fun Navigator.navigateToLogin() {
    navigate(LoginRoute)
}
