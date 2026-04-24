package com.xbot.sharedapp.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module
@Configuration
@ComponentScan("com.xbot.sharedapp")
class AppModule

@KoinApplication
internal object AnilibriaApp
