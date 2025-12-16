package com.xbot.login.di

import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.scene.DialogSceneStrategy
import com.xbot.common.navigation.Navigator
import com.xbot.login.LoginScreen
import com.xbot.login.LoginViewModel
import com.xbot.login.navigation.LoginRoute
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val loginFeatureModule = module {
    navigation<LoginRoute>(
        metadata = DialogSceneStrategy.dialog(
            DialogProperties()
        )
    ) {
        LoginScreen(
            onNavigateBack = {
                get<Navigator>().navigateBack()
            }
        )
    }
    viewModelOf(::LoginViewModel)
}
