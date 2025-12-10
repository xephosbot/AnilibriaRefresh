package com.xbot.title.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.SharedViewModelStoreNavEntryDecorator
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.TitleDetailsPane
import com.xbot.title.TitleEpisodesPane
import com.xbot.title.TitleViewModel
import com.xbot.title.navigation.TitleEpisodesRoute
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.navigation.navigateToTitle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val titleFeatureModule = module {
    navigation<TitleRoute>(
        metadata = SupportingPaneSceneStrategy.mainPane(TitleRoute)
    ) { key ->
        val viewModel = koinViewModel<TitleViewModel> {
            parametersOf(key.aliasOrId)
        }

        TitleDetailsPane(
            viewModel = viewModel,
            onBackClick = {
                get<Navigator>().navigateBack()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                get<Navigator>().navigateToPlayer(releaseId, episodeOrdinal)
            },
            onReleaseClick = { releaseId ->
                get<Navigator>().navigateToTitle(releaseId)
            },
            onEpisodesListClick = {
                get<Navigator>().navigate(TitleEpisodesRoute(key.aliasOrId))
            }
        )
    }
    navigation<TitleEpisodesRoute>(
        metadata = SupportingPaneSceneStrategy.supportingPane(TitleRoute)
            //+ SharedViewModelStoreNavEntryDecorator.viewModelParent(TitleRoute.toString())
    ) { key ->
        val viewModel = koinViewModel<TitleViewModel> {
            parametersOf(key.aliasOrId)
        }

        TitleEpisodesPane(
            viewModel = viewModel,
            showBackButton = true,
            onBackClick = {
                get<Navigator>().navigateBack()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                get<Navigator>().navigateToPlayer(releaseId, episodeOrdinal)
            },
        )
    }
    viewModel { (aliasOrId: String) ->
        TitleViewModel(get(), get(), aliasOrId)
    }
}
