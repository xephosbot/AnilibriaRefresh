package com.xbot.preference.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import com.xbot.common.navigation.ExternalLinkNavKey
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.replace
import com.xbot.preference.PreferenceListPane
import com.xbot.preference.donate.DonateDetailScreen
import com.xbot.preference.donate.DonateViewModel
import com.xbot.preference.history.HistoryDetailScreen
import com.xbot.preference.history.HistoryViewModel
import com.xbot.preference.navigation.PreferenceDonateRoute
import com.xbot.preference.navigation.PreferenceHistoryRoute
import com.xbot.preference.navigation.PreferenceOptionRoute
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.preference.navigation.PreferenceSettingsRoute
import com.xbot.preference.navigation.PreferenceTeamRoute
import com.xbot.preference.settings.SettingsDetailScreen
import com.xbot.preference.settings.SettingsViewModel
import com.xbot.preference.team.TeamDetailScreen
import com.xbot.preference.team.TeamViewModel
import com.xbot.preference.ui.PreferenceDetailPlaceholder
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val preferenceFeatureModule = module {
    navigation<PreferenceRoute>(
        metadata = ListDetailSceneStrategy.listPane(
            sceneKey = PreferenceRoute,
            detailPlaceholder = {
                PreferenceDetailPlaceholder()
            }
        )
    ) {
        val navigator = get<Navigator>()
        PreferenceListPane(
            currentDestination = navigator.currentDestination as? PreferenceOptionRoute,
            onNavigateToDetail = { destination ->
                if (destination is ExternalLinkNavKey) {
                    navigator.navigate(destination)
                    return@PreferenceListPane
                }

                navigator.replace<PreferenceOptionRoute>(destination)
            }
        )
    }
    navigation<PreferenceHistoryRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        HistoryDetailScreen(
            onNavigateBack = {
                get<Navigator>().navigateBack()
            }
        )
    }
    navigation<PreferenceTeamRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        TeamDetailScreen(
            onNavigateBack = {
                get<Navigator>().navigateBack()
            }
        )
    }
    navigation<PreferenceDonateRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        DonateDetailScreen(
            onNavigateBack = {
                get<Navigator>().navigateBack()
            }
        )
    }
    navigation<PreferenceSettingsRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        SettingsDetailScreen(
            onNavigateBack = {
                get<Navigator>().navigateBack()
            }
        )
    }
    viewModelOf(::HistoryViewModel)
    viewModelOf(::TeamViewModel)
    viewModelOf(::DonateViewModel)
    viewModelOf(::SettingsViewModel)
}
