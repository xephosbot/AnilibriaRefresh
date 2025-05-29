package com.xbot.network.di

import com.xbot.network.AnilibriaApi
import com.xbot.network.client.AnilibriaClient
import com.xbot.network.client.HttpClientFactory
import com.xbot.network.client.auth.AuthConfig
import com.xbot.network.client.auth.AuthTokenManager
import org.koin.dsl.module

val networkModule = module {
    single {
        AuthTokenManager(dataStore = get())
    }

    single {
        AuthConfig.createDefault(AnilibriaApi.BASE_URL_API)
    }

    single {
        HttpClientFactory(
            authTokenManager = get(),
            authConfig = get()
        )
    }

    single {
        HttpClientFactory(
            authTokenManager = get(),
            authConfig = get()
        )
    }

    single {
        get<HttpClientFactory>().create()
    }

    single { AnilibriaClient(client = get()) }
}