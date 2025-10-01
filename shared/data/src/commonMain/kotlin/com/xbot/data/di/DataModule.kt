package com.xbot.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(platformModule, repositoryModule)
}