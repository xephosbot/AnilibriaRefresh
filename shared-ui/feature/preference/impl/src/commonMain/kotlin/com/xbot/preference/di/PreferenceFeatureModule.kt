package com.xbot.preference.di

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xbot.common.navigation.Navigator
import org.koin.dsl.navigation3.navigation.navigation
import com.xbot.preference.PreferenceDestination
import com.xbot.preference.PreferenceListPane
import com.xbot.preference.ProfileScreenState
import com.xbot.preference.ProfileViewModel
import com.xbot.preference.donate.SupportDetailScreen
import com.xbot.preference.history.HistoryViewModel
import com.xbot.preference.history.ViewHistoryDetailScreen
import com.xbot.preference.navigation.PreferenceDonateRoute
import com.xbot.preference.navigation.PreferenceHistoryRoute
import com.xbot.preference.navigation.PreferenceProfileRoute
import com.xbot.preference.navigation.PreferenceRoute
import com.xbot.preference.navigation.PreferenceSettingsRoute
import com.xbot.preference.navigation.PreferenceTeamRoute
import com.xbot.preference.profile.ProfileDetailScreen
import com.xbot.preference.settings.SettingsDetailScreen
import com.xbot.preference.team.TeamDetailScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val preferenceFeatureModule = module {
    navigation<PreferenceRoute>(
        metadata = ListDetailSceneStrategy.listPane(
            sceneKey = PreferenceRoute,
            detailPlaceholder = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Выберите элемент из списка")
                }
            }
        )
    ) {
        PreferenceListPane(
            state = ProfileScreenState.Loading,
            isExpandedLayout = true,
            currentDestination = null,
            onNavigateToDetail = { destination ->
                when (destination) {
                    PreferenceDestination.PROFILE -> get<Navigator>().navigate(PreferenceProfileRoute)
                    PreferenceDestination.HISTORY -> get<Navigator>().navigate(PreferenceHistoryRoute)
                    PreferenceDestination.TEAM -> get<Navigator>().navigate(PreferenceTeamRoute)
                    PreferenceDestination.DONATE -> get<Navigator>().navigate(PreferenceDonateRoute)
                    PreferenceDestination.SETTINGS -> get<Navigator>().navigate(PreferenceSettingsRoute)
                }
            },
            onOpenUrl = {
                println("Open URL: $it")
            }
        )
    }
    navigation<PreferenceProfileRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        ProfileDetailScreen(
            onNavigateBack = {
                get<Navigator>().navigateBack()
            }
        )
    }
    navigation<PreferenceHistoryRoute>(
        metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
    ) {
        ViewHistoryDetailScreen(
            onBackClick = {
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
        SupportDetailScreen(
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
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HistoryViewModel)
}