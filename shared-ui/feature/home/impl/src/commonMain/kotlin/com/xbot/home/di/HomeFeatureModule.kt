package com.xbot.home.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.home.FeedPane
import com.xbot.home.FeedViewModel
import com.xbot.home.SchedulePane
import com.xbot.home.ScheduleViewModel
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.ScheduleRoute
import com.xbot.search.navigation.navigateToSearch
import com.xbot.title.navigation.navigateToTitle
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val homeFeatureModule = module {
    single<NavEntryBuilder>(named("feature/home")) {
        { navigator ->
            entry<HomeRoute>(
                metadata = SupportingPaneSceneStrategy.mainPane(HomeRoute)
            ) {
                FeedPane(
                    onScheduleClick = {
                        navigator.navigate(ScheduleRoute)
                    },
                    onSearchClick = {
                        navigator.navigateToSearch()
                    },
                    onReleaseClick = { releaseId ->
                        navigator.navigateToTitle(releaseId)
                    }
                )
            }
            entry<ScheduleRoute>(
                metadata = SupportingPaneSceneStrategy.supportingPane(HomeRoute)
            ) {
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
        }
    }
    viewModelOf(::FeedViewModel)
    viewModelOf(::ScheduleViewModel)
}