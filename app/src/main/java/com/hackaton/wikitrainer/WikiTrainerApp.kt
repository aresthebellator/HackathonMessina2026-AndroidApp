package com.hackaton.wikitrainer

import android.app.Application
import com.hackaton.wikitrainer.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WikiTrainerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@WikiTrainerApp)
            modules(appModules)
        }
    }
}
