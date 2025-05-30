package com.xbot.data.di

import com.xbot.data.repository.DefaultAuthRepository
import com.xbot.data.repository.DefaultCatalogRepository
import com.xbot.data.repository.DefaultGenresRepository
import com.xbot.data.repository.DefaultProfileRepository
import com.xbot.data.repository.DefaultReleasesRepository
import com.xbot.data.repository.DefaultScheduleRepository
import com.xbot.domain.repository.AuthRepository
import com.xbot.domain.repository.CatalogRepository
import com.xbot.domain.repository.GenresRepository
import com.xbot.domain.repository.ProfileRepository
import com.xbot.domain.repository.ReleasesRepository
import com.xbot.domain.repository.ScheduleRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DefaultAuthRepository) { bind<AuthRepository>() }
    singleOf(::DefaultReleasesRepository) { bind<ReleasesRepository>() }
    singleOf(::DefaultCatalogRepository) { bind<CatalogRepository>() }
    singleOf(::DefaultProfileRepository) { bind<ProfileRepository>() }
    singleOf(::DefaultScheduleRepository) { bind<ScheduleRepository>() }
    singleOf(::DefaultGenresRepository) { bind<GenresRepository>() }
}
