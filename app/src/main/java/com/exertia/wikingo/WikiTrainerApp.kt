package com.exertia.wikingo

import android.app.Application
import com.exertia.wikingo.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

import com.exertia.wikingo.core.i18n.I18nManager

class WikiTrainerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        I18nManager.load(this)

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@WikiTrainerApp)
            modules(appModules)
        }
    }
}
