package com.xbot.player.di

import com.xbot.player.platform.VLCPlayerController
import com.xbot.player.ui.VideoPlayerController
import org.koin.dsl.module

actual val playerModule = module {
    single<VideoPlayerController>(createdAtStart = true) { VLCPlayerController() }
}