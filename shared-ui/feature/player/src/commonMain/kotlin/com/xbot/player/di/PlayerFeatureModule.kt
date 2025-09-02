package com.xbot.player.di

import com.xbot.common.navigation.Destination
import com.xbot.player.PlayerViewModel
import com.xbot.player.navigation.PlayerRoute
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val playerFeatureModule = module {
    includes(playerModule)
    single<PolymorphicModuleBuilder<Destination>.() -> Unit>(named("player")) {
        {
            subclass(PlayerRoute::class)
        }
    }
    viewModelOf(::PlayerViewModel)
}
internal expect val playerModule: Module