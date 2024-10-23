package com.xbot.api.di

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.xbot.api.service.AnilibriaClient
import com.xbot.api.service.AnilibriaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.brotli.BrotliInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(BrotliInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            isLenient = true
        }
        return Retrofit.Builder()
            .baseUrl(AnilibriaService.BASE_URL_API)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAnilibriaService(retrofit: Retrofit): AnilibriaService {
        return retrofit.create(AnilibriaService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnilibriaClient(service: AnilibriaService): AnilibriaClient {
        return AnilibriaClient(service)
    }

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return AnilibriaService.BASE_URL
    }
}