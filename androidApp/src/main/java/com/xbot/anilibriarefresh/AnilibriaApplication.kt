package com.xbot.anilibriarefresh

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.xbot.anilibriarefresh.di.coilModule
import com.xbot.anilibriarefresh.di.uiModule
import com.xbot.anilibriarefresh.di.viewModelModule
import com.xbot.together.di.initKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext

class AnilibriaApplication : Application() {
    private val imageLoader: ImageLoader by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AnilibriaApplication)
            modules(
                viewModelModule,
                uiModule,
                coilModule
            )
        }
        SingletonImageLoader.setSafe { imageLoader }
    }
}
