/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.data.di

import com.xbot.data.repository.FiltersRepositoryImpl
import com.xbot.data.repository.TitleRepositoryImpl
import com.xbot.domain.repository.FiltersRepository
import com.xbot.domain.repository.TitleRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TitleRepositoryImpl) bind TitleRepository::class
    singleOf(::FiltersRepositoryImpl) bind FiltersRepository::class
}
