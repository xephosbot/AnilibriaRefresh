package com.xbot.together.di

import com.xbot.api.di.networkModule
import com.xbot.data.di.repositoryModule
import com.xbot.domain.di.useCaseModule

val sharedModule
    get() = listOf(networkModule, repositoryModule, useCaseModule)