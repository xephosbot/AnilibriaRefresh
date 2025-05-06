package com.xbot.domain.di

import com.xbot.domain.usecase.GetCatalogFilters
import com.xbot.domain.usecase.GetReleasesFeed
import com.xbot.domain.usecase.GetReleasesPager
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetReleasesFeed)
    singleOf(::GetCatalogFilters)
    singleOf(::GetReleasesPager)
}