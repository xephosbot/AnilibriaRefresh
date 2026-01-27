package com.xbot.data.di

import com.xbot.data.datasource.createDataStore
import com.xbot.data.repository.DefaultTokenStorage
import com.xbot.network.client.SessionStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore() }
    singleOf(::DefaultTokenStorage) { bind<SessionStorage>() }
}
