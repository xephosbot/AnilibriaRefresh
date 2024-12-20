package com.xbot.api.di

import com.xbot.api.AnilibriaApi
import com.xbot.api.client.AnilibriaClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single { AnilibriaClient() }
    single(named("baseUrl")) { AnilibriaApi.BASE_URL }
    single(named("baseUrlApi")) { AnilibriaApi.BASE_URL_API }
}
