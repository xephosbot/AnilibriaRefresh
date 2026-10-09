package com.xbot.title.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.xbot.common.lifecycle.dropUnlessResumed
import com.xbot.common.serialization.polymorphic
import com.xbot.navigation.LocalNavigator
import com.xbot.navigation.NavKey
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.TitleViewModel
import com.xbot.title.component.calculateSectionPaneScaffoldDirective
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.navigation.navigateToTitle
import com.xbot.title.navigation.navigateToTitleLink
import com.xbot.title.screen.details.TitleDetailsPane
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
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
            parametersOf(key.aliasOrId, key.release)
        }
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        TitleDetailsPane(
            viewModel = viewModel,
            directive = calculateSectionPaneScaffoldDirective(currentWindowAdaptiveInfoV2()),
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToPlayer(releaseId, episodeOrdinal)
                }.invoke()
            },
            onReleaseClick = { release ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToTitle(release)
                }.invoke()
            },
            onGenreClick = {},
            onMemberClick = {},
            onFranchiseAllClick = {},
            onUrlClick = { url ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToTitleLink(url)
                }.invoke()
            }
        )
    }
}
