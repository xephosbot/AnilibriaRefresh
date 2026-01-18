package com.xbot.domain.di

import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetCatalogFiltersUseCase
import com.xbot.domain.usecase.GetReleasesFeedUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import com.xbot.domain.usecase.GetAppearanceSettingsUseCase
import com.xbot.domain.usecase.UpdateDynamicThemeUseCase
import com.xbot.domain.usecase.UpdateExpressiveColorUseCase
import com.xbot.domain.usecase.UpdatePureBlackUseCase
import com.xbot.domain.usecase.UpdateThemeOptionUseCase
import com.xbot.domain.utils.DefaultDispatcherProvider
import com.xbot.domain.utils.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }

    singleOf(::GetAuthStateUseCase)
    singleOf(::GetReleasesFeedUseCase)
    singleOf(::GetCatalogFiltersUseCase)
    singleOf(::GetCatalogReleasesPagerUseCase)
    singleOf(::GetSortedScheduleWeekUseCase)
    singleOf(::GetAppearanceSettingsUseCase)
    singleOf(::UpdateThemeOptionUseCase)
    singleOf(::UpdateDynamicThemeUseCase)
    singleOf(::UpdatePureBlackUseCase)
    singleOf(::UpdateExpressiveColorUseCase)
}