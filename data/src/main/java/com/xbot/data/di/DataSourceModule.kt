package com.xbot.data.di

import com.xbot.api.service.AnilibriaClient
import com.xbot.data.datasource.FiltersDataSource
import com.xbot.data.datasource.TitleDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideTitleDataSource(client: AnilibriaClient): TitleDataSource {
        return TitleDataSource(client)
    }

    @Provides
    @Singleton
    fun provideFiltersDataSource(client: AnilibriaClient): FiltersDataSource {
        return FiltersDataSource(client)
    }
}