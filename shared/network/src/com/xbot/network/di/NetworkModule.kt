package com.xbot.network.di

import com.xbot.network.client.AnilibriaClient
import com.xbot.network.client.HttpClientFactory
import com.xbot.network.client.auth.AuthTokenManager
import org.koin.dsl.module

val networkModule = module {
    single {
        AuthTokenManager(dataStore = get())
    }

    single {
        HttpClientFactory(authTokenManager = get())
    }

    single {
        get<HttpClientFactory>().create()
    }

    single { AnilibriaClient(client = get(), authTokenManager = get()) }
}