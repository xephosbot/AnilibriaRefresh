package com.xbot.network

object Constants {
    const val BASE_URL: String = "https://aniliberty.top"
    const val FALLBACK_URL: String = "https://api.anilibria.app"
    const val BASE_URL_API: String = "https://aniliberty.top/api/v1/"
    const val FALLBACK_URL_API: String = "https://api.anilibria.app/api/v1/"
    const val IMAGE_URL_ALIAS: String = "https://image"

    fun withImageUrl(route: String) = "$IMAGE_URL_ALIAS$route"
}
