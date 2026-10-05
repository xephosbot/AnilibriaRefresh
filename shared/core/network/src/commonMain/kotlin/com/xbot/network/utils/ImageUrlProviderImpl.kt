package com.xbot.network.utils

import com.xbot.network.Constants
import org.koin.core.annotation.Singleton

@Singleton
internal class ImageUrlProviderImpl : ImageUrlProvider {
    override fun getFullUrl(path: String?): String? {
        if (path == null) return null
        if (path.startsWith("http")) return path
        val normalizedPath = if (path.startsWith("/")) path else "/$path"
        return "${Constants.BASE_URL}$normalizedPath"
    }
}
