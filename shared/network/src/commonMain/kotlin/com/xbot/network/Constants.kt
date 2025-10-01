package com.xbot.network

object Constants {
    internal const val BASE_URL: String = "https://aniliberty.top"
    internal const val BASE_URL_API: String = "https://aniliberty.top/api/v1/"

    fun withBaseUrl(route: String) = "$BASE_URL$route"
}