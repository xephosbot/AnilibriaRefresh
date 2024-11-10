package com.xbot.data.di

import com.xbot.data.datasource.FiltersDataSource
import com.xbot.data.datasource.TitleDataSource
import com.xbot.data.repository.FiltersRepositoryImpl
import com.xbot.data.repository.TitleRepositoryImpl
import com.xbot.domain.repository.FiltersRepository
import com.xbot.domain.repository.TitleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTitleRepository(dataSource: TitleDataSource): TitleRepository {
        return TitleRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideFiltersRepository(dataSource: FiltersDataSource): FiltersRepository {
        return FiltersRepositoryImpl(dataSource)
    }
}

/*
 * Created by AnyGogin31 on 10.11.2024
 */

val repositoryModule = module {
    singleOf(::TitleRepositoryImpl) bind TitleRepository::class
    singleOf(::FiltersRepositoryImpl) bind FiltersRepository::class
}
