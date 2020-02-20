package com.ainrom.lbcmusic.di

import com.ainrom.lbcmusic.App

class Injector private constructor() {
    companion object {
        fun get(): AppComponent = App.component
    }
}