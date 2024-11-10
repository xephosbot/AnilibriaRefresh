package com.xbot.media.di

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaModule = module {
    single<Player> {
        ExoPlayer.Builder(androidContext())
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
}
