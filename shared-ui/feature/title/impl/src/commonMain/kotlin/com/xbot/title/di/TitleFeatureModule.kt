package com.xbot.title.di

import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.TitleScreen
import com.xbot.title.TitleViewModel
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.navigation.navigateToTitle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val titleFeatureModule = module {
    single<NavEntryBuilder>(named("feature/title")) {
        { navigator ->
            entry<TitleRoute> { key ->
                val viewModel = koinViewModel<TitleViewModel> {
                    parametersOf(key)
                }
                TitleScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navigator.navigateBack()
                    },
                    onPlayClick = { releaseId, episodeOrdinal ->
                        navigator.navigateToPlayer(releaseId, episodeOrdinal)
                    },
                    onReleaseClick = { releaseId ->
                        navigator.navigateToTitle(releaseId)
                    }
                )
            }
        }
    }
    viewModelOf(::TitleViewModel)
}