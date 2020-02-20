package com.ainrom.lbcmusic.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule {
    @Provides
    fun appContext(app: Application): Context = app.applicationContext
}