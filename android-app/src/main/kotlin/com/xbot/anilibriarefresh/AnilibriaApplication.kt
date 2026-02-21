package com.xbot.anilibriarefresh

import android.app.Application
import com.xbot.sharedapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class AnilibriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AnilibriaApplication)
        }
    }
}
