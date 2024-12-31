package com.xbot.api.di

import com.xbot.api.AnilibriaApi
import com.xbot.api.client.AnilibriaClient
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val defaultJson = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}

val networkModule = module {
    single { AnilibriaClient() }
    single(named("baseUrl")) { AnilibriaApi.BASE_URL }
    single(named("baseUrlApi")) { AnilibriaApi.BASE_URL_API }
}
