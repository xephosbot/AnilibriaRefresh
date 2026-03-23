package com.xbot.login.navigation

import com.xbot.common.navigation.ExternalUriNavKey
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.Navigator
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LoginRoute(val returnTo: NavKey? = null) : NavKey

@Serializable
data object RegistrationRoute : ExternalUriNavKey {
    @Transient
    override val uri: String = "https://aniliberty.top/app/auth/registration/newRegistration"
}

fun Navigator.navigateToLogin(returnTo: NavKey? = null) {
    navigate(LoginRoute(returnTo))
}

fun Navigator.navigateToRegistration() {
    navigate(RegistrationRoute)
}
