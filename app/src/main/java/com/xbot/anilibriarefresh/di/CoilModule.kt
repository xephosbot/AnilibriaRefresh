package com.xbot.anilibriarefresh.di

import android.content.Context
import coil3.ImageLoader
import coil3.map.Mapper
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.anilibriarefresh.models.Poster
import com.xbot.api.di.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        @BaseUrl baseUrl: String
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(Mapper<Poster, String> { data, _ -> "$baseUrl${data.src}" })
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}