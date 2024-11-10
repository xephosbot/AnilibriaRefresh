/*
 * Created by AnyGogin31 on 10.11.2024
 */

package com.xbot.media.di

import androidx.media3.exoplayer.ExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaModule = module {
    single {
        ExoPlayer.Builder(androidContext())
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
}
