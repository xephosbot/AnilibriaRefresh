package com.xbot.preference.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.xbot.common.lifecycle.dropUnlessResumed
import com.xbot.common.navigation.ExternalUriNavKey
import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.replace
import com.xbot.common.serialization.polymorphic
import com.xbot.preference.PreferenceListPane
import com.xbot.preference.appearance.AppearancePane
import com.xbot.preference.appearance.AppearanceViewModel
import com.xbot.preference.donate.DonatePane
import com.xbot.preference.donate.DonateViewModel
import com.xbot.preference.history.HistoryPane
import com.xbot.preference.history.HistoryViewModel
import com.xbot.preference.language.LanguagePane
import com.xbot.preference.navigation.DiscordRoute
import com.xbot.preference.navigation.GitHubRoute
import com.xbot.preference.navigation.PreferenceAppearanceRoute
import com.xbot.preference.navigation.PreferenceDonateRoute
import com.xbot.preference.navigation.PreferenceHistoryRoute
import com.xbot.preference.navigation.PreferenceLanguageRoute
import com.xbot.preference.navigation.PreferenceOptionRoute
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.preference.navigation.PreferenceTeamRoute
import com.xbot.preference.navigation.YouTubeRoute
import com.xbot.preference.team.TeamPane
import com.xbot.preference.team.TeamViewModel
import com.xbot.preference.ui.PreferenceDetailPlaceholder
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val preferenceFeatureModule = module {
    polymorphic<NavKey> {
        subclass(PreferenceRoute::class)
        subclass(PreferenceHistoryRoute::class)
        subclass(PreferenceTeamRoute::class)
        subclass(PreferenceDonateRoute::class)
        subclass(PreferenceAppearanceRoute::class)
        subclass(PreferenceLanguageRoute::class)
        subclass(GitHubRoute::class)
        subclass(YouTubeRoute::class)
        subclass(DiscordRoute::class)
    }
    navigation<PreferenceRoute>(
        metadata = ListDetailSceneStrategy.listPane(
            sceneKey = PreferenceRoute,
            detailPlaceholder = {
                PreferenceDetailPlaceholder()
            }
        )
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        PreferenceListPane(
            currentDestination = navigator.currentDestination as? PreferenceOptionRoute,
            onDetailClick = { destination ->
                lifecycleOwner.dropUnlessResumed {
                    if (destination is ExternalUriNavKey) {
                        navigator.navigate(destination)
                    } else {
                        navigator.replace<PreferenceOptionRoute>(destination)
                    }
                }.invoke()
            }
        )
    }
    navigation<PreferenceHistoryRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        HistoryPane(
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            }
        )
    }
    navigation<PreferenceTeamRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        TeamPane(
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            }
        )
    }
    navigation<PreferenceDonateRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        DonatePane(
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            }
        )
    }
    navigation<PreferenceAppearanceRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        AppearancePane(
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            }
        )
    }
    navigation<PreferenceLanguageRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        LanguagePane(
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            }
        )
    }
    viewModel<HistoryViewModel>()
    viewModel<TeamViewModel>()
    viewModel<DonateViewModel>()
    viewModel<AppearanceViewModel>()
}
