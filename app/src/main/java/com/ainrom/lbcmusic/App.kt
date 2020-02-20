package com.ainrom.lbcmusic

import android.app.Application
import com.ainrom.lbcmusic.di.AppComponent
import com.ainrom.lbcmusic.di.DaggerAppComponent

class App: Application() {

    companion object {
        lateinit var instance: App
            private set
        lateinit var component: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.builder().application(this).build()
    }
}