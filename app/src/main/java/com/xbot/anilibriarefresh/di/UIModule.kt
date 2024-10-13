package com.xbot.anilibriarefresh.di

import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UIModule {
    @Provides
    fun provideSnackbarManager(): SnackbarManager {
        return SnackbarManager
    }
}
