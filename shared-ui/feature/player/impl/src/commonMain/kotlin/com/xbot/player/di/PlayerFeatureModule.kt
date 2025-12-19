package com.xbot.player.di

import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.polymorphic
import com.xbot.player.PlayerScreen
import com.xbot.player.PlayerViewModel
import com.xbot.player.navigation.PlayerRoute
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val playerFeatureModule = module {
    polymorphic<NavKey> {
        subclass(PlayerRoute::class)
    }
    navigation<PlayerRoute> { key ->
        val viewModel = koinViewModel<PlayerViewModel> {
            parametersOf(key)
        }
        val navigator = LocalNavigator.current
        PlayerScreen(
            viewModel = viewModel,
            onBackClick = {
                navigator.navigateBack()
            },
        )
    }
    viewModelOf(::PlayerViewModel)
}
