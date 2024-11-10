package com.xbot.anilibriarefresh

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.xbot.anilibriarefresh.di.coilModule
import com.xbot.anilibriarefresh.di.uiModule
import com.xbot.anilibriarefresh.di.viewModelModule
import com.xbot.api.di.networkModule
import com.xbot.data.di.dataSourceModule
import com.xbot.data.di.repositoryModule
import com.xbot.media.di.mediaModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnilibriaApplication : Application() {

    private val imageLoader: ImageLoader by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnilibriaApplication)
            modules(
                networkModule,
                dataSourceModule,
                repositoryModule,
                viewModelModule,
                mediaModule,
                uiModule,
                coilModule,
            )
        }
        SingletonImageLoader.setSafe { imageLoader }
    }
}
