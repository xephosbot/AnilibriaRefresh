package com.xbot.sharedapp.di

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.map.Mapper
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.domain.models.Poster
import com.xbot.sharedapp.getCacheDir
import io.ktor.client.HttpClient
import org.koin.dsl.module

val coilModule = module {
    single<SingletonImageLoader.Factory> {
        val httpClient: HttpClient = get()
        SingletonImageLoader.Factory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    add(KtorNetworkFetcherFactory(httpClient = { httpClient }))
                    add(Mapper<Poster, String> { data, _ -> data.src })
                }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(context)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(getCacheDir(context).resolve("image_cache"))
                        .maxSizeBytes(512L * 1024 * 1024)
                        .build()
                }
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        }
    }
}
