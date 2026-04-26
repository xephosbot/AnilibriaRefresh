package com.xbot.network.utils

interface ImageUrlProvider {
    fun getFullUrl(path: String?): String?
}
