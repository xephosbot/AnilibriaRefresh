package com.xbot.data.di

import com.xbot.data.datasource.createDataStore
import com.xbot.data.repository.IosSessionStorage
import com.xbot.network.client.SessionStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val platformModule = module {
    single { createDataStore() }
    singleOf(::IosSessionStorage) { bind<SessionStorage>() }
}
