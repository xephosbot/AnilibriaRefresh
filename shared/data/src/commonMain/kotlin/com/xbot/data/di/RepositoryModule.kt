package com.xbot.data.di

import com.xbot.data.repository.DefaultFiltersRepository
import com.xbot.data.repository.DefaultTitleRepository
import com.xbot.domain.repository.FiltersRepository
import com.xbot.domain.repository.TitleRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::DefaultTitleRepository) { bind<TitleRepository>() }
    singleOf(::DefaultFiltersRepository) { bind<FiltersRepository>() }
}
