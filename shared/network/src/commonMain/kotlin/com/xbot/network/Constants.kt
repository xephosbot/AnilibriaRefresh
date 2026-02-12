package com.xbot.network

object Constants {
    internal const val BASE_URL: String = "https://aniliberty.top"
    internal const val FALLBACK_URL: String = "https://api.anilibria.app"
    internal const val BASE_URL_API: String = "https://aniliberty.top/api/v1/"
    internal const val FALLBACK_URL_API: String = "https://api.anilibria.app/api/v1/"
    internal const val IMAGE_URL_ALIAS: String = "https://image"

    fun withImageUrl(route: String) = "$IMAGE_URL_ALIAS$route"
}
