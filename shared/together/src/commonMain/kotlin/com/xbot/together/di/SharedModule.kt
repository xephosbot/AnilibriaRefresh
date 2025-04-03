package com.xbot.together.di

import com.xbot.api.di.networkModule
import com.xbot.api.di.dataStoreModule
import com.xbot.data.di.repositoryModule
import com.xbot.domain.di.useCaseModule

val sharedModule
    get() = listOf(dataStoreModule, networkModule, repositoryModule, useCaseModule)