package com.xbot.sharedapp.di

import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.PolymorphicSerializerConfig
import com.xbot.data.di.dataModule
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.domainModule
import com.xbot.favorite.di.favoriteFeatureModule
import com.xbot.home.di.homeFeatureModule
import com.xbot.login.di.loginFeatureModule
import com.xbot.network.di.networkModule
import com.xbot.player.di.playerFeatureModule
import com.xbot.preference.di.preferenceFeatureModule
import com.xbot.search.di.searchFeatureModule
import com.xbot.sharedapp.AppViewModel
import com.xbot.title.di.titleFeatureModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    includes(networkModule, dataModule, domainModule, coilModule)
    includes(
        favoriteFeatureModule,
        homeFeatureModule,
        playerFeatureModule,
        preferenceFeatureModule,
        searchFeatureModule,
        titleFeatureModule,
        loginFeatureModule
    )

    single {
        SerializersModule {
            polymorphic(NavKey::class) {
                getAll<PolymorphicSerializerConfig<NavKey>>().forEach { it.configure(this) }
            }
        }
    }

    factory { SnackbarManager }
}
