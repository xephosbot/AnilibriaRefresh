package com.xbot.title.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.polymorphic
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.TitleDetailsPane
import com.xbot.title.TitleViewModel
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.navigation.navigateToTitle
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val titleFeatureModule = module {
    polymorphic<NavKey> {
        subclass(TitleRoute::class)
    }
    navigation<TitleRoute> { key ->
        val viewModel = koinViewModel<TitleViewModel> {
            parametersOf(key.aliasOrId)
        }
        val navigator = LocalNavigator.current
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
        )
    }
    viewModel { (aliasOrId: String) ->
        TitleViewModel(get(), get(), aliasOrId)
    }
}
