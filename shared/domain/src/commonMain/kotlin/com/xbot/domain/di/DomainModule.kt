package com.xbot.domain.di

import com.xbot.domain.usecase.GetCatalogFilters
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetCatalogReleasesPager
import com.xbot.domain.usecase.GetSortedScheduleWeekUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetReleasesFeed)
    singleOf(::GetCatalogFilters)
    singleOf(::GetCatalogReleasesPager)
    singleOf(::GetSortedScheduleWeekUseCase)
}