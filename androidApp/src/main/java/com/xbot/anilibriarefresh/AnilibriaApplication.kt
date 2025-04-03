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
import com.xbot.domain.models.Poster
import com.xbot.together.di.initKoin
import org.koin.android.ext.koin.androidContext

class AnilibriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AnilibriaApplication)
            modules(
                viewModelModule,
                uiModule,
            )
        }
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(applicationContext)
                .crossfade(true)
                .components {
                    add(Mapper<Poster, String> { data, _ -> data.src })
                }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(applicationContext)
                        .build()
                }
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        }
    }
}
