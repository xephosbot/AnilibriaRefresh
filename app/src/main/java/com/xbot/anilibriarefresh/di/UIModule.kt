package com.xbot.anilibriarefresh.di

import android.content.Context
import com.xbot.anilibriarefresh.service.PlaybackService
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.media.service.PlayerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object UIModule {
    @Provides
    fun provideSnackbarManager(): SnackbarManager {
        return SnackbarManager
    }

    @Provides
    fun providePlayerProvider(@ApplicationContext context: Context): PlayerProvider {
        return PlayerProvider.create<PlaybackService>(context)
    }
}
