package com.xbot.login.di

import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.scene.DialogSceneStrategy
import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.polymorphic
import com.xbot.login.LoginScreen
import com.xbot.login.LoginViewModel
import com.xbot.login.navigation.LoginRoute
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val loginFeatureModule = module {
    polymorphic<NavKey> {
        subclass(LoginRoute::class)
    }
    navigation<LoginRoute>(
        metadata = DialogSceneStrategy.dialog(
            DialogProperties()
        )
    ) {
        val navigator = LocalNavigator.current
        LoginScreen(
            onNavigateBack = {
                navigator.navigateBack()
            }
        )
    }
    viewModelOf(::LoginViewModel)
}
