package com.xbot.data.di

import com.xbot.data.datasource.TitleDataSource
import com.xbot.data.repository.TitleRepositoryImpl
import com.xbot.domain.repository.TitleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideTitleRepository(dataSource: TitleDataSource): TitleRepository {
        return TitleRepositoryImpl(dataSource)
    }
}