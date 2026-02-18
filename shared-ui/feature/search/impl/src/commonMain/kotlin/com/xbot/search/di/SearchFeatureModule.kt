package com.xbot.search.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.xbot.common.lifecycle.dropUnlessResumed
import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.navigation.SharedViewModelStoreNavEntryDecorator
import com.xbot.common.serialization.polymorphic
import com.xbot.search.SearchFilterPane
import com.xbot.search.SearchResultPane
import com.xbot.search.SearchViewModel
import com.xbot.search.navigation.SearchFiltersRoute
import com.xbot.search.navigation.SearchRoute
import com.xbot.search.navigation.navigateToSearchFilters
import com.xbot.title.navigation.navigateToTitle
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val searchFeatureModule = module {
    polymorphic<NavKey> {
        subclass(SearchRoute::class)
        subclass(SearchFiltersRoute::class)
    }
    navigation<SearchRoute>(
        metadata = SupportingPaneSceneStrategy.mainPane(SearchRoute)
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        SearchResultPane(
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            },
            onFiltersClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateToSearchFilters()
            },
            onReleaseClick = { release ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToTitle(release)
                }.invoke()
            }
        )
    }
    navigation<SearchFiltersRoute>(
        metadata = SupportingPaneSceneStrategy.supportingPane(SearchRoute)
            + SharedViewModelStoreNavEntryDecorator.viewModelParent(SearchRoute.toString())
    ) {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        SearchFilterPane(
            showBackButton = true,
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            },
        )
    }
    viewModelOf(::SearchViewModel)
}
