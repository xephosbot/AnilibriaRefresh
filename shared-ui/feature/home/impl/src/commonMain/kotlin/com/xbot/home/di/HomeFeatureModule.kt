package com.xbot.home.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.SharedViewModelStoreNavEntryDecorator
import com.xbot.common.serialization.polymorphic
import com.xbot.home.FeedPane
import com.xbot.home.HomeViewModel
import com.xbot.home.SchedulePane
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.ScheduleRoute
import com.xbot.login.navigation.navigateToLogin
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.navigation.navigateToTitle
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val homeFeatureModule = module {
    polymorphic<NavKey> {
        subclass(HomeRoute::class)
        subclass(ScheduleRoute::class)
    }
    navigation<HomeRoute>(
        metadata = SupportingPaneSceneStrategy.mainPane(HomeRoute)
    ) {
        val navigator = LocalNavigator.current
        FeedPane(
            onScheduleClick = {
                navigator.navigate(ScheduleRoute)
            },
            onReleaseClick = { releaseId ->
                navigator.navigateToTitle(releaseId)
            },
            onEpisodeClick = { releaseId, episodeOrdinal ->
                navigator.navigateToPlayer(releaseId, episodeOrdinal)
            },
            onProfileClick = {
                navigator.navigateToLogin()
            }
        )
    }
    navigation<ScheduleRoute>(
        metadata = SupportingPaneSceneStrategy.supportingPane(HomeRoute)
            + SharedViewModelStoreNavEntryDecorator.viewModelParent(HomeRoute.toString())
    ) {
        val navigator = LocalNavigator.current
        SchedulePane(
            showBackButton = true,
            onReleaseClick = { releaseId ->
                navigator.navigateToTitle(releaseId)
            },
            onBackClick = {
                navigator.navigateBack()
            }
        )
    }
    viewModelOf(::HomeViewModel)
}
