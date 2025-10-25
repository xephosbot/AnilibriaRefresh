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
import com.xbot.common.navigation.NavEntryBuilder
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
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val preferenceFeatureModule = module {
    single<NavEntryBuilder>(named("feature/preference")) {
        { navigator ->
            entry<PreferenceRoute>(
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
                            PreferenceDestination.PROFILE -> navigator.navigate(PreferenceProfileRoute)
                            PreferenceDestination.HISTORY -> navigator.navigate(PreferenceHistoryRoute)
                            PreferenceDestination.TEAM -> navigator.navigate(PreferenceTeamRoute)
                            PreferenceDestination.DONATE -> navigator.navigate(PreferenceDonateRoute)
                            PreferenceDestination.SETTINGS -> navigator.navigate(PreferenceSettingsRoute)
                        }
                    },
                    onOpenUrl = {
                        println("Open URL: $it")
                    }
                )
            }
            entry<PreferenceProfileRoute>(
                metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
            ) {
                ProfileDetailScreen(
                    onNavigateBack = {
                        navigator.navigateBack()
                    }
                )
            }
            entry<PreferenceHistoryRoute>(
                metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
            ) {
                ViewHistoryDetailScreen(
                    onNavigateBack = {
                        navigator.navigateBack()
                    }
                )
            }
            entry<PreferenceTeamRoute>(
                metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
            ) {
                TeamDetailScreen(
                    onNavigateBack = {
                        navigator.navigateBack()
                    }
                )
            }
            entry<PreferenceDonateRoute>(
                metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
            ) {
                SupportDetailScreen(
                    onNavigateBack = {
                        navigator.navigateBack()
                    }
                )
            }
            entry<PreferenceSettingsRoute>(
                metadata = ListDetailSceneStrategy.detailPane(PreferenceRoute)
            ) {
                SettingsDetailScreen(
                    onNavigateBack = {
                        navigator.navigateBack()
                    }
                )
            }
        }
    }
    viewModelOf(::ProfileViewModel)
    viewModelOf(::HistoryViewModel)
}