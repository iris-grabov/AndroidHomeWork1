package com.example.mygame

import android.app.Application
import com.example.mygame.utilities.SharedPreferencesManagerV3

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManagerV3.init(this)
    }
}