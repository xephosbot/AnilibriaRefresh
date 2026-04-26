package com.xbot.sharedapp.coil

import coil3.Uri
import coil3.map.Mapper
import coil3.request.Options
import coil3.toUri
import com.xbot.network.utils.ImageUrlProvider

internal class ImageUrlMapper(
    private val imageUrlProvider: ImageUrlProvider
) : Mapper<String, Uri> {
    override fun map(data: String, options: Options): Uri? {
        return imageUrlProvider.getFullUrl(data)?.toUri()
    }
}
