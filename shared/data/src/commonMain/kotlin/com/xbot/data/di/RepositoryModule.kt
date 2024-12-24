package com.xbot.data.di

import com.xbot.data.repository.DefaultCatalogRepository
import com.xbot.data.repository.DefaultReleaseRepository
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.ReleaseRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DefaultReleaseRepository) { bind<ReleaseRepository>() }
    singleOf(::DefaultCatalogRepository) { bind<CatalogRepository>() }
}
