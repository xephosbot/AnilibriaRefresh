package com.xbot.anilibriarefresh.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.xbot.api.di.BaseUrl
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.InputStream

@GlideModule
class GlideModule : AppGlideModule() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlideEntryPoint {
        @BaseUrl fun baseUrl(): String
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setIsActiveResourceRetentionAllowed(true)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val entryPoint = EntryPoints.get(context, GlideEntryPoint::class.java)
        val baseUrl = entryPoint.baseUrl()
        registry.replace(
            String::class.java, InputStream::class.java,
            BaseUrlModelLoader.Factory(baseUrl)
        )
    }
}