package com.xbot.anilibriarefresh.di

import com.xbot.anilibriarefresh.service.PlaybackService
import com.xbot.anilibriarefresh.ui.utils.SnackbarManager
import com.xbot.media.service.PlayerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val uiModule = module {
    factory { SnackbarManager }
    factory { PlayerProvider.create<PlaybackService>(context = androidContext()) }
}
