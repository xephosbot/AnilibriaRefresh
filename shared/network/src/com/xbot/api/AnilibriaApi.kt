package com.xbot.api

object AnilibriaApi {
    internal const val BASE_URL: String = "https://anilibria.top"
    internal const val BASE_URL_API: String = "https://anilibria.top/api/v1/"

    fun withBaseUrl(route: String) = "$BASE_URL$route"
}