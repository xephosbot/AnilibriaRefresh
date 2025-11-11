package com.xbot.domain.di

import com.xbot.domain.usecase.GetAuthStateUseCase
import com.xbot.domain.usecase.GetCatalogFiltersUseCase
import com.xbot.domain.usecase.GetReleasesFeedUseCase
import com.xbot.domain.usecase.GetCatalogReleasesPagerUseCase
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetAuthStateUseCase)
    singleOf(::GetReleasesFeedUseCase)
    singleOf(::GetCatalogFiltersUseCase)
    singleOf(::GetCatalogReleasesPagerUseCase)
    singleOf(::GetSortedScheduleWeekUseCase)
}