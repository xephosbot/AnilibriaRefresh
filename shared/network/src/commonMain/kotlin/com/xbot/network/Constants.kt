package com.xbot.network

object Constants {
    const val BASE_URL: String = "https://aniliberty.top"
    const val FALLBACK_URL: String = "https://api.anilibria.app"
    internal const val BASE_URL_API: String = "https://aniliberty.top/api/v1/"
    internal const val FALLBACK_URL_API: String = "https://api.anilibria.app/api/v1/"

    fun withBaseUrl(route: String) = "$BASE_URL$route"
}
