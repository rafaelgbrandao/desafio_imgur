package com.godinho.desafioimgur

import android.app.Application
import com.godinho.desafioimgur.di.appModule
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            modules(appModule)
        }
    }
}