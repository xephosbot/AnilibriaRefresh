package com.xbot.di

import com.xbot.data.di.DataModule
import com.xbot.domain.di.DomainModule
import com.xbot.network.di.NetworkModule
import org.koin.core.annotation.KoinApplication

@KoinApplication(modules = [NetworkModule::class, DataModule::class, DomainModule::class])
internal class AnilibriaApp
