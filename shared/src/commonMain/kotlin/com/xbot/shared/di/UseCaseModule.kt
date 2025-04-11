package com.xbot.shared.di

import com.xbot.shared.domain.usecase.GetCatalogFilters
import com.xbot.shared.domain.usecase.GetReleasesFeed
import com.xbot.shared.domain.usecase.GetReleasesPager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetReleasesFeed)
    singleOf(::GetCatalogFilters)
    singleOf(::GetReleasesPager)
}