package com.xbot.together.di

import com.xbot.api.di.networkModule
import com.xbot.data.di.dataSourceModule
import com.xbot.data.di.repositoryModule

val sharedModule
    get() = listOf(networkModule, dataSourceModule, repositoryModule)