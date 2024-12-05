/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.api.di

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.xbot.api.BuildConfig
import com.xbot.api.auth.AuthService
import com.xbot.api.catalog.CatalogService
import com.xbot.api.franchises.FranchisesService
import com.xbot.api.genres.GenresService
import com.xbot.api.releases.ReleasesService
import com.xbot.api.schedule.ScheduleService
import com.xbot.api.AnilibriaClient
import com.xbot.api.AnilibriaApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.brotli.BrotliInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(logging)
                }
            }
            .addInterceptor(BrotliInterceptor)
            .build()
    }

    single {
        val json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            isLenient = true
        }

        Retrofit.Builder()
            .baseUrl(get<String>(named("baseUrlApi")))
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(get())
            .build()
    }

    single { get<Retrofit>().create(AuthService::class.java) }
    single { get<Retrofit>().create(CatalogService::class.java) }
    single { get<Retrofit>().create(FranchisesService::class.java) }
    single { get<Retrofit>().create(GenresService::class.java) }
    single { get<Retrofit>().create(ReleasesService::class.java) }
    single { get<Retrofit>().create(ScheduleService::class.java) }

    single {
        AnilibriaClient(
            authService = get(),
            catalogService = get(),
            franchisesService = get(),
            genresService = get(),
            releasesService = get(),
            scheduleService = get()
        )
    }

    single(named("baseUrl")) { AnilibriaApi.BASE_URL }
    single(named("baseUrlApi")) { AnilibriaApi.BASE_URL_API }
}
