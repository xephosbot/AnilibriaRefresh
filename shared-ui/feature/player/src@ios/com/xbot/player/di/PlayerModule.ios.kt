package com.xbot.player.di

import com.xbot.player.ui.AVPlayer
import com.xbot.player.ui.VideoPlayer
import org.koin.dsl.module

actual val playerModule = module {
    single<VideoPlayer> { AVPlayer() }
}