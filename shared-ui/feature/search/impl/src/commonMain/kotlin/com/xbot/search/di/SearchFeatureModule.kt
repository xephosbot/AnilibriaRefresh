package com.xbot.search.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import com.xbot.common.navigation.LocalResultEventBus
import com.xbot.common.navigation.NavEntryBuilder
import com.xbot.domain.models.filters.CatalogFilters
import com.xbot.search.SearchFilterPane
import com.xbot.search.SearchFiltersViewModel
import com.xbot.search.SearchResultPane
import com.xbot.search.SearchResultViewModel
import com.xbot.search.navigation.SearchFiltersRoute
import com.xbot.search.navigation.SearchRoute
import com.xbot.title.navigation.navigateToTitle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val searchFeatureModule = module {
    single<NavEntryBuilder>(named("feature/search")) {
        { navigator ->
            entry<SearchRoute>(
                metadata = SupportingPaneSceneStrategy.mainPane(SearchRoute)
            ) {
                SearchResultPane(
                    onBackClick = {
                        navigator.navigateBack()
                    },
                    onShowFilters = { filters ->
                        navigator.navigate(SearchFiltersRoute(filters))
                    },
                    onReleaseClick = { releaseId ->
                        navigator.navigateToTitle(releaseId)
                    }
                )
            }
            entry<SearchFiltersRoute>(
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
                        navigator.navigateBack()
                    },
                    onBackClick = {
                        navigator.navigateBack()
                    },
                )
            }
        }
    }
    viewModelOf(::SearchResultViewModel)
    viewModelOf(::SearchFiltersViewModel)
}