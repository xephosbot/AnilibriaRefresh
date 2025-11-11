package com.xbot.home.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.navigation
import com.xbot.home.FeedPane
import com.xbot.home.FeedViewModel
import com.xbot.home.SchedulePane
import com.xbot.home.ScheduleViewModel
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.ScheduleRoute
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.title.navigation.navigateToTitle
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val homeFeatureModule = module {
    navigation<HomeRoute>(
        metadata = SupportingPaneSceneStrategy.mainPane(HomeRoute)
    ) {
        FeedPane(
            onScheduleClick = {
                get<Navigator>().navigate(ScheduleRoute)
            },
            onReleaseClick = { releaseId ->
                get<Navigator>().navigateToTitle(releaseId)
            },
            onEpisodeClick = { releaseId, episodeOrdinal ->
                get<Navigator>().navigateToPlayer(releaseId, episodeOrdinal)
            },
        )
    }
    navigation<ScheduleRoute>(
        metadata = SupportingPaneSceneStrategy.supportingPane(HomeRoute)
    ) {
        SchedulePane(
            showBackButton = true,
            onReleaseClick = { releaseId ->
                get<Navigator>().navigateToTitle(releaseId)
            },
            onBackClick = {
                get<Navigator>().navigateBack()
            }
        )
    }
    viewModelOf(::FeedViewModel)
    viewModelOf(::ScheduleViewModel)
}