package com.xbot.sharedapp.coil

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import com.xbot.network.Constants

internal class ImageUrlInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val data = chain.request.data
        return if (data is String && data.startsWith(Constants.IMAGE_URL_ALIAS)) {
            chain.withRequest(
                chain.request.newBuilder()
                    .data(data.replaceFirst(Constants.IMAGE_URL_ALIAS, Constants.BASE_URL))
                    .build()
            ).proceed()
        } else {
            chain.proceed()
        }
    }
}
