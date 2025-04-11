package com.xbot.shared

import androidx.compose.ui.window.ComposeUIViewController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.shared.di.localDataSourceModule
import com.xbot.shared.di.remoteDataSourceModule
import com.xbot.shared.di.repositoryModule
import com.xbot.shared.di.uiModule
import com.xbot.shared.di.useCaseModule
import com.xbot.shared.di.viewModelModule
import com.xbot.shared.domain.models.Poster
import com.xbot.shared.ui.AnilibriaApp
import com.xbot.shared.ui.designsystem.theme.AnilibriaTheme
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
    }
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(Mapper<Poster, String> { data, _ -> data.src })
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
    KoinApplication(
        application = {
            modules(
                localDataSourceModule,
                remoteDataSourceModule,
                useCaseModule,
                repositoryModule,
                viewModelModule,
                uiModule
            )
        }
    ) {
        AnilibriaTheme {
            AnilibriaApp()
        }
    }
}