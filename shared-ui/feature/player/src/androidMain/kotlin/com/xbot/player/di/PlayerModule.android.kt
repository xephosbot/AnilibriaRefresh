package com.xbot.player.di

import com.xbot.player.ui.ExoPlayer
import com.xbot.player.ui.VideoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val playerModule = module {
    single<VideoPlayer>(createdAtStart = true) { ExoPlayer(androidContext()) }
}