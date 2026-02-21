@file:OptIn(ExperimentalObjCRefinement::class)

package com.xbot.sharedapp.di

import com.xbot.common.navigation.NavKey
import com.xbot.common.serialization.PolymorphicSerializerConfig
import com.xbot.data.di.DataModule
import com.xbot.designsystem.utils.SnackbarManager
import com.xbot.domain.di.DomainModule
import com.xbot.network.di.NetworkModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
@KoinApplication(modules = [NetworkModule::class, DataModule::class, DomainModule::class, AppModule::class])
internal class AnilibriaApp

@HiddenFromObjC
@Module
@ComponentScan("com.xbot.sharedapp")
internal class AppModule {

    @Single
    fun snackbarManager(): SnackbarManager = SnackbarManager

    @Single
    fun navKeySerializersModule(
        configs: List<PolymorphicSerializerConfig<NavKey>>
    ): SerializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            configs.forEach { it.configure(this) }
        }
    }
}
