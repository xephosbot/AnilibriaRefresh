package com.xbot.title.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import com.xbot.common.serialization.polymorphic
import com.xbot.navigation.NavKey
import com.xbot.title.component.calculateSectionPaneScaffoldDirective
import com.xbot.title.navigation.TitleRoute
import com.xbot.title.screen.details2.TitleDetailsPreviewDataV2
import com.xbot.title.screen.details2.TitleDetailsPaneContentV3
import kotlinx.serialization.modules.subclass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
val titleFeatureModule = module {
    polymorphic<NavKey> {
        subclass(TitleRoute::class)
    }
    navigation<TitleRoute> { key ->
/*        val viewModel = koinViewModel<TitleViewModel> {
            parametersOf(key.aliasOrId, key.release)
        }
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        TitleDetailsPane(
            viewModel = viewModel,
            onBackClick = lifecycleOwner.dropUnlessResumed {
                navigator.navigateBack()
            },
            onPlayClick = { releaseId, episodeOrdinal ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToPlayer(releaseId, episodeOrdinal)
                }.invoke()
            },
            onReleaseClick = { release ->
                lifecycleOwner.dropUnlessResumed {
                    navigator.navigateToTitle(release)
                }.invoke()
            }
        )*/

        TitleDetailsPaneContentV3(
            state = TitleDetailsPreviewDataV2.geoBlocked,
            directive = calculateSectionPaneScaffoldDirective(currentWindowAdaptiveInfoV2())
        )
    }
}
