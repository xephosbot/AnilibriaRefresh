package com.xbot.search.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.Navigator
import com.xbot.common.navigation.SharedViewModelStoreNavEntryDecorator
import com.xbot.search.SearchFilterPane
import com.xbot.search.SearchResultPane
import com.xbot.search.SearchViewModel
import com.xbot.search.navigation.SearchFiltersRoute
import com.xbot.search.navigation.SearchRoute
import com.xbot.title.navigation.navigateToTitle
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val searchFeatureModule = module {
    navigation<SearchRoute>(
        metadata = SupportingPaneSceneStrategy.mainPane(SearchRoute)
    ) {
        SearchResultPane(
            onBackClick = {
                get<Navigator>().navigateBack()
            },
            onShowFilters = {
                get<Navigator>().navigate(SearchFiltersRoute)
            },
            onReleaseClick = { releaseId ->
                get<Navigator>().navigateToTitle(releaseId)
            }
        )
    }
    navigation<SearchFiltersRoute>(
        metadata = SupportingPaneSceneStrategy.supportingPane(SearchRoute)
            + SharedViewModelStoreNavEntryDecorator.viewModelParent(SearchRoute.toString())
    ) {
        SearchFilterPane(
            showBackButton = true,
            onBackClick = {
                get<Navigator>().navigateBack()
            },
        )
    }
    viewModelOf(::SearchViewModel)
}