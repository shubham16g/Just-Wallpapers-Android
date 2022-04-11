package com.shubhamgupta16.wallpaperapp

import android.app.Application
import com.shubhamgupta16.wallpaperapp.utils.ThemeController
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application(){

    @Inject
    lateinit var themeController: ThemeController

    override fun onCreate() {
        super.onCreate()
        themeController.init()
    }
}