package com.xbot.home.di

import androidx.navigation.compose.composable
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.home.HomeScreen
import com.xbot.home.HomeViewModel
import com.xbot.home.navigation.HomeRoute
import com.xbot.search.navigation.navigateToSearch
import com.xbot.title.navigation.navigateToTitle
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule = module {
    single<NavEntryBuilder>(named("feature/home")) {
        { navigator ->
            composable<HomeRoute> {
                HomeScreen(
                    onSearchClick = {
                        navigator.navigateToSearch()
                    },
                    onReleaseClick = { releaseId ->
                        navigator.navigateToTitle(releaseId)
                    }
                )
            }
        }
    }
    viewModelOf(::HomeViewModel)
}