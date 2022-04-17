package com.shubhamgupta16.wallpaperapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.shubhamgupta16.wallpaperapp.utils.AppMemory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application(){

    @Inject
    lateinit var appMemory: AppMemory

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(appMemory.getMode())
    }
}