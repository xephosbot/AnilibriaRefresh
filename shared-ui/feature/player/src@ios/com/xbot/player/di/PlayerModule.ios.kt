package com.xbot.player.di

import com.xbot.player.platform.AVPlayerController
import com.xbot.player.ui.VideoPlayerController
import org.koin.dsl.module

actual val playerModule = module {
    single<VideoPlayerController> { AVPlayerController() }
}