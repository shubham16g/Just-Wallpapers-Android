package com.shubhamgupta16.wallpaperapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.shubhamgupta16.wallpaperapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ThemeController @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPref = context.getSharedPreferences("ThemePref", Context.MODE_PRIVATE)

    fun init() {
        AppCompatDelegate.setDefaultNightMode(getMode())
    }

    fun setMode(mode: Int) {
        if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM || mode == AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY ||
            mode == AppCompatDelegate.MODE_NIGHT_YES || mode == AppCompatDelegate.MODE_NIGHT_NO
        ) {
            saveMode(mode)
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    fun getMode() = sharedPref.getInt("mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    private fun saveMode(mode:Int){
        sharedPref.edit().putInt("mode", mode).apply()
    }
}