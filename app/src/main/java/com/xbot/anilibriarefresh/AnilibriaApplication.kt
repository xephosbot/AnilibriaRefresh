package com.xbot.anilibriarefresh

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.xbot.anilibriarefresh.di.coilModule
import com.xbot.anilibriarefresh.di.uiModule
import com.xbot.api.di.networkModule
import com.xbot.data.di.dataSourceModule
import com.xbot.data.di.repositoryModule
import com.xbot.media.di.mediaModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import javax.inject.Inject

@HiltAndroidApp
class AnilibriaApplication : Application() {
    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()
        SingletonImageLoader.setSafe { imageLoader }
        startKoin {
            androidContext(this@AnilibriaApplication)
            modules(
                networkModule,
                dataSourceModule,
                repositoryModule,
                mediaModule,
                uiModule,
                coilModule,
            )
        }
    }
}
