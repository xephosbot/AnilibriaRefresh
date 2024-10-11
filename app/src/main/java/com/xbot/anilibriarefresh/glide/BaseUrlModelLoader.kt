package com.xbot.anilibriarefresh.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import java.io.InputStream

class BaseUrlModelLoader(
    private val concreteLoader: ModelLoader<GlideUrl, InputStream>,
    private val baseUrl: String
) : ModelLoader<String, InputStream> {

    override fun buildLoadData(
        model: String,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        val fullUrl = if (model.startsWith("/")) {
            GlideUrl("$baseUrl$model")
        } else {
            GlideUrl(model)
        }
        return concreteLoader.buildLoadData(fullUrl, width, height, options)
    }

    override fun handles(model: String): Boolean = model.startsWith("/")

    class Factory(private val baseUrl: String) : ModelLoaderFactory<String, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            val glideUrlLoader = multiFactory.build(GlideUrl::class.java, InputStream::class.java)
            return BaseUrlModelLoader(glideUrlLoader, baseUrl)
        }

        override fun teardown() {}
    }
}