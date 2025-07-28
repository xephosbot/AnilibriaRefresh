package com.xbot.player.di

import com.xbot.player.ui.VLCPlayer
import com.xbot.player.ui.VideoPlayer
import org.koin.dsl.module

actual val playerModule = module {
    single<VideoPlayer>(createdAtStart = true) { VLCPlayer() }
}