package com.wengabytes.movieviewer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.wengabytes.movieviewer.components.vmFactoryModule
import com.wengabytes.movieviewer.di.networkModule
import com.wengabytes.movieviewer.services.movieRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MovieViewerApplication: Application() {

    // START: Callbacks

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MovieViewerApplication)
            modules(listOf(networkModule,
                movieRepository,
                vmFactoryModule))
        }
    }

    // END  : Callbacks
}