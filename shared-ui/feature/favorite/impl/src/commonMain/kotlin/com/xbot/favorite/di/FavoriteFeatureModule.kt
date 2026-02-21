package com.xbot.favorite.di

import androidx.lifecycle.compose.LocalLifecycleOwner
import com.xbot.common.lifecycle.dropUnlessResumed
import com.xbot.common.navigation.LocalNavigator
import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.polymorphic
import com.xbot.favorite.FavoritePane
import com.xbot.favorite.FavoriteViewModel
import com.xbot.favorite.navigation.FavoriteRoute
import com.xbot.title.navigation.navigateToTitle
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val favoriteFeatureModule = module {
    polymorphic<NavKey> {
        subclass(FavoriteRoute::class)
    }
    navigation<FavoriteRoute> {
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current

        FavoritePane(
            onReleaseClick = { release ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToTitle(release)
                }.invoke()
            }
        )
    }
    viewModel<FavoriteViewModel>()
}
