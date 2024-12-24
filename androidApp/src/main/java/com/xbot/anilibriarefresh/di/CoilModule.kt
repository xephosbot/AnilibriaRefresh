/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.anilibriarefresh.di

import coil3.ImageLoader
import coil3.map.Mapper
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.xbot.domain.models.Poster
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coilModule = module {
    single {
        val context = androidContext()
        val baseUrl = get<String>(qualifier = named("baseUrl"))

        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(Mapper<Poster, String> { data, _ -> "$baseUrl${data.src}" })
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}
