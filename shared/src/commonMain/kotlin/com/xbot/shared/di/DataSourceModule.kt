package com.xbot.shared.di

import com.xbot.shared.data.sources.local.TokenDataStore
import com.xbot.shared.data.sources.remote.AnilibriaClient
import com.xbot.shared.data.sources.remote.TokenProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val localDataSourceModule: Module

val remoteDataSourceModule = module {
    singleOf(::TokenDataStore) { bind<TokenProvider>() }
    singleOf(::AnilibriaClient)
}