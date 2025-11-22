package com.xbot.player.di

import com.xbot.common.navigation.Navigator
import org.koin.dsl.navigation3.navigation.navigation
import com.xbot.player.PlayerScreen
import com.xbot.player.PlayerViewModel
import com.xbot.player.navigation.PlayerRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val playerFeatureModule = module {
    navigation<PlayerRoute> { key ->
        val viewModel = koinViewModel<PlayerViewModel> {
            parametersOf(key)
        }
        PlayerScreen(
            viewModel = viewModel,
            onBackClick = {
                get<Navigator>().navigateBack()
            },
        )
    }
    viewModelOf(::PlayerViewModel)
}
