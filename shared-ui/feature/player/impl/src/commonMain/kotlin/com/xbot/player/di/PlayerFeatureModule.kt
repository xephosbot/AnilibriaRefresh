package com.xbot.player.di

import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.player.PlayerScreen
import com.xbot.player.PlayerViewModel
import com.xbot.player.navigation.PlayerRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val playerFeatureModule = module {
    single<NavEntryBuilder>(named("feature/player")) {
        { navigator ->
            entry<PlayerRoute> { key ->
                val viewModel = koinViewModel<PlayerViewModel> {
                    parametersOf(key)
                }
                PlayerScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navigator.navigateBack()
                    },
                )
            }
        }
    }
    includes(playerModule)
    viewModelOf(::PlayerViewModel)
}
internal expect val playerModule: Module