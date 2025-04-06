package com.xbot.anilibriarefresh

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.anilibriarefresh.di.uiModule
import com.xbot.anilibriarefresh.di.viewModelModule
import com.xbot.api.di.dataStoreModule
import com.xbot.api.di.networkModule
import com.xbot.data.di.repositoryModule
import com.xbot.domain.di.useCaseModule
import com.xbot.domain.models.Poster
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnilibriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnilibriaApplication)
            modules(
                networkModule,
                dataStoreModule,
                useCaseModule,
                repositoryModule,
                viewModelModule,
                uiModule
            )
        }
        SingletonImageLoader.setSafe { context ->
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
    }
}