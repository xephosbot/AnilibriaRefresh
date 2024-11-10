package com.xbot.anilibriarefresh.di

import android.content.Context
import coil3.ImageLoader
import coil3.map.Mapper
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.anilibriarefresh.models.Poster
import com.xbot.api.di.BaseUrl
import com.xbot.api.service.AnilibriaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        @BaseUrl baseUrl: String,
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

/*
 * Created by AnyGogin31 on 10.11.2024
 */

val coilModule = module {
    single { (context: Context, baseUrl: String) ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(Mapper<Poster, String> { data, _ -> "$baseUrl${data.src}" })
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}
