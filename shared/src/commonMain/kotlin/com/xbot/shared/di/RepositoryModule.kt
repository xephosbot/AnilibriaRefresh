package com.xbot.shared.di

import com.xbot.shared.data.repositories.DefaultCatalogRepository
import com.xbot.shared.data.repositories.DefaultReleaseRepository
import com.xbot.shared.data.repositories.DefaultUserRepository
import com.xbot.shared.domain.repository.CatalogRepository
import com.xbot.shared.domain.repository.ReleaseRepository
import com.xbot.shared.domain.repository.UserRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DefaultReleaseRepository) { bind<ReleaseRepository>() }
    singleOf(::DefaultCatalogRepository) { bind<CatalogRepository>() }
    singleOf(::DefaultUserRepository) { bind<UserRepository>() }
}