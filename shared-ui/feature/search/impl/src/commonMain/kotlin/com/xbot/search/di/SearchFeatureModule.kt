package com.xbot.search.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.LocalResultEventBus
import com.xbot.common.navigation.Navigator
import org.koin.dsl.navigation3.navigation.navigation
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.search.SearchFilterPane
import com.xbot.search.SearchFiltersViewModel
import com.xbot.search.SearchResultPane
import com.xbot.search.SearchResultViewModel
import com.xbot.search.navigation.SearchFiltersRoute
import com.xbot.search.navigation.SearchRoute
import com.xbot.title.navigation.navigateToTitle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val searchFeatureModule = module {
    navigation<SearchRoute>(
        metadata = SupportingPaneSceneStrategy.mainPane(SearchRoute)
    ) {
        SearchResultPane(
            onBackClick = {
                get<Navigator>().navigateBack()
            },
            onShowFilters = { filters ->
                get<Navigator>().navigate(SearchFiltersRoute(filters))
            },
            onReleaseClick = { releaseId ->
                get<Navigator>().navigateToTitle(releaseId)
            }
        )
    }
    navigation<SearchFiltersRoute>(
        metadata = SupportingPaneSceneStrategy.supportingPane(SearchRoute)
    ) { key ->
        val resultBus = LocalResultEventBus.current
        val viewModel = koinViewModel<SearchFiltersViewModel> {
            parametersOf(key)
        }

        SearchFilterPane(
            viewModel = viewModel,
            showBackButton = true,
            onApplyFilters = { filters ->
                resultBus.sendResult<CatalogFilters>(result = filters)
                get<Navigator>().navigateBack()
            },
            onBackClick = {
                get<Navigator>().navigateBack()
            },
        )
    }
    viewModelOf(::SearchResultViewModel)
    viewModelOf(::SearchFiltersViewModel)
}