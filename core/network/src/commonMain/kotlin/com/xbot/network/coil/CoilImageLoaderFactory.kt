package com.xbot.network.coil

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.xbot.network.Constants
import io.ktor.client.HttpClient
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import org.koin.core.annotation.Singleton

@Singleton
internal fun createCoilImageLoader(
    httpClient: HttpClient,
    cacheDir: CoilCacheDir,
): SingletonImageLoader.Factory {
    return SingletonImageLoader.Factory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(createUrlInterceptor())
                add(KtorNetworkFetcherFactory(httpClient = { httpClient }))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.path.resolve(IMAGE_CACHE_DIR))
                    .maxSizeBytes(512L * 1024 * 1024)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}

private fun createUrlInterceptor() = Interceptor { chain ->
    val request = chain.request
    val modifiedRequest = when (val data = request.data) {
        is String -> transformUrlIfNeeded(data, request)
        else -> request
    }
    chain.withRequest(modifiedRequest).proceed()
}

private fun transformUrlIfNeeded(url: String, request: ImageRequest): ImageRequest {
    val currentUrl = Url(url)
    val aliasUrl = Url(Constants.IMAGE_URL_ALIAS)

    return if (currentUrl.host == aliasUrl.host) {
        val fallbackUrl = Url(Constants.BASE_URL)
        val newUrl = currentUrl.replaceBaseUrl(fallbackUrl)
        request.newBuilder().data(newUrl).build()
    } else {
        request
    }
}

private fun Url.replaceBaseUrl(newBaseUrl: Url): String {
    return URLBuilder(this).apply {
        protocol = newBaseUrl.protocol
        host = newBaseUrl.host
        port = newBaseUrl.port
    }.buildString()
}

private const val IMAGE_CACHE_DIR = "image_cache"