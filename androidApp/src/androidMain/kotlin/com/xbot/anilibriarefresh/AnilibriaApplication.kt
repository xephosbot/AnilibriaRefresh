package com.xbot.anilibriarefresh

import android.app.Application
import com.xbot.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AnilibriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AnilibriaApplication)
            androidLogger()
        }
    }
}