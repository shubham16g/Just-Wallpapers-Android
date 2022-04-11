package com.shubhamgupta16.wallpaperapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ThemeController @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = context.getSharedPreferences("ThemePref", Context.MODE_PRIVATE)


//    fun getMode(): Int {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
//        return sharedPref.getInt("mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//    }

    fun init() {
        AppCompatDelegate.setDefaultNightMode(
            sharedPref.getInt(
                "mode",
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
    }

    fun setMode(mode: Int) {
        if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM || mode == AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY ||
            mode == AppCompatDelegate.MODE_NIGHT_YES || mode == AppCompatDelegate.MODE_NIGHT_NO
        ) {
            sharedPref.edit().putInt("mode", mode).apply()
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}