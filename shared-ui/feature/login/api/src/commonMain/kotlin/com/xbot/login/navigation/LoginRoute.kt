package com.xbot.login.navigation

import com.xbot.common.navigation.ExternalLinkNavKey
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LoginRoute(val returnTo: NavKey? = null) : NavKey

@Serializable
data object RegistrationRoute : ExternalLinkNavKey {
    @Transient
    override val url: String = "https://aniliberty.top/app/auth/registration/newRegistration"
}

fun Navigator.navigateToLogin(returnTo: NavKey? = null) {
    navigate(LoginRoute(returnTo))
}

fun Navigator.navigateToRegistration() {
    navigate(RegistrationRoute)
}
