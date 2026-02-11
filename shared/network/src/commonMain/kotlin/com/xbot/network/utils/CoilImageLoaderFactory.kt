package com.xbot.network.utils

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.network.Constants
import io.ktor.client.HttpClient
import io.ktor.http.URLBuilder
import io.ktor.http.Url

internal fun createCoilImageLoader(
    httpClient: HttpClient,
    cacheDirProvider: CoilCacheDirProvider
): SingletonImageLoader.Factory {
    return SingletonImageLoader.Factory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add { chain ->
                    val request = chain.request
                    val data = request.data
                    if (data is String) {
                        val currentUrl = Url(data)
                        if (currentUrl.host == Url(Constants.IMAGE_URL_ALIAS).host) {
                            val baseUrl = Url(Constants.FALLBACK_URL)
                            val newUrl = URLBuilder(currentUrl).apply {
                                protocol = baseUrl.protocol
                                host = baseUrl.host
                                port = baseUrl.port
                            }.buildString()
                            return@add chain.withRequest(request.newBuilder().data(newUrl).build()).proceed()
                        }
                    }
                    chain.proceed()
                }
                add(KtorNetworkFetcherFactory(httpClient = { httpClient }))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDirProvider.getCacheDir().resolve(IMAGE_CACHE_DIR))
                    .maxSizeBytes(512L * 1024 * 1024)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}

private const val IMAGE_CACHE_DIR = "image_cache"