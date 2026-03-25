package com.xbot.shared.di

import com.xbot.data.di.DataModule
import com.xbot.domain.di.DomainModule
import com.xbot.home.state.HomeStateModule
import com.xbot.player.state.PlayerStateModule
import com.xbot.title.state.TitleStateModule
import com.xbot.network.di.NetworkModule
import org.koin.core.annotation.KoinApplication

@KoinApplication(
    modules = [NetworkModule::class, DataModule::class, DomainModule::class, HomeStateModule::class, TitleStateModule::class, PlayerStateModule::class]
)
internal class AnilibriaApp
