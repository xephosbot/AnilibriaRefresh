package com.xbot.search.di

import androidx.navigation.compose.composable
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.search.SearchScreen
import com.xbot.search.SearchViewModel
import com.xbot.search.navigation.SearchRoute
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchFeatureModule = module {
    single<NavEntryBuilder>(named("feature/search")) {
        { navigator ->
            composable<SearchRoute> {
                SearchScreen(
                    onBackClick = {
                        navigator.navigateBack()
                    },
                    onReleaseClick = { releaseId ->
                        //navigator.onReleaseClick(releaseId)
                    }
                )
            }
        }
    }
    viewModelOf(::SearchViewModel)
}