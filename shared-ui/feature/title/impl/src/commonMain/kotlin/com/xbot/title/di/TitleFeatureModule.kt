package com.xbot.title.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.TitleDetailsPane
import com.xbot.title.TitleDetailsViewModel
import com.xbot.title.TitleEpisodesPane
import com.xbot.title.TitleEpisodesViewModel
import com.xbot.title.navigation.TitleEpisodesRoute
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.navigation.navigateToTitle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val titleFeatureModule = module {
    single<NavEntryBuilder>(named("feature/title")) {
        { navigator ->
            entry<TitleRoute>(
                metadata = SupportingPaneSceneStrategy.mainPane(TitleRoute)
            ) { key ->
                val viewModel = koinViewModel<TitleDetailsViewModel> {
                    parametersOf(key)
                }
                TitleDetailsPane(
                    viewModel = viewModel,
                    onBackClick = {
                        navigator.navigateBack()
                    },
                    onPlayClick = { releaseId, episodeOrdinal ->
                        navigator.navigateToPlayer(releaseId, episodeOrdinal)
                    },
                    onReleaseClick = { releaseId ->
                        navigator.navigateToTitle(releaseId)
                    },
                    onEpisodesListClick = {
                        navigator.navigate(TitleEpisodesRoute(key.aliasOrId))
                    }
                )
            }
            entry<TitleEpisodesRoute>(
                metadata = SupportingPaneSceneStrategy.supportingPane(TitleRoute)
            ) { key ->
                val viewModel = koinViewModel<TitleEpisodesViewModel> {
                    parametersOf(key)
                }
                TitleEpisodesPane(
                    viewModel = viewModel,
                    showBackButton = true,
                    onBackClick = {
                        navigator.navigateBack()
                    },
                    onPlayClick = { releaseId, episodeOrdinal ->
                        navigator.navigateToPlayer(releaseId, episodeOrdinal)
                    },
                )
            }
        }
    }
    viewModelOf(::TitleDetailsViewModel)
    viewModelOf(::TitleEpisodesViewModel)
}