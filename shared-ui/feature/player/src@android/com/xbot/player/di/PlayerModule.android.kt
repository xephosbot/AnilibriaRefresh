package com.xbot.player.di

import com.xbot.player.platform.ExoPlayerController
import com.xbot.player.ui.VideoPlayerController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val playerModule = module {
    single<VideoPlayerController>(createdAtStart = true) { ExoPlayerController(androidContext()) }
}