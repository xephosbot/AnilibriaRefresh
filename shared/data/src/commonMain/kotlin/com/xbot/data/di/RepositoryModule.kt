package com.xbot.data.di

import com.xbot.data.repository.FiltersRepositoryImpl
import com.xbot.data.repository.TitleRepositoryImpl
import com.xbot.domain.repository.FiltersRepository
import com.xbot.domain.repository.TitleRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::TitleRepositoryImpl) { bind<TitleRepository>() }
    factoryOf(::FiltersRepositoryImpl) { bind<FiltersRepository>() }
}
