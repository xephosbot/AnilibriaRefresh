package com.xbot.data.di

import com.xbot.data.datasource.FiltersDataSource
import com.xbot.data.datasource.TitleDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::TitleDataSource)
    singleOf(::FiltersDataSource)
}
