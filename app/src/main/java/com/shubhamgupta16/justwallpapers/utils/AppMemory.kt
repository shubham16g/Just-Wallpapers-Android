package com.shubhamgupta16.justwallpapers.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppMemory @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val PREF_NAME = "app_memory"
        private const val INTERSTITIAL_AD_DURATION = 1000 * 50
    }

    private val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    fun getMode() = sharedPref.getInt("mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    fun saveMode(mode: Int) {
        sharedPref.edit().putInt("mode", mode).apply()
    }

    /*fun interstitialAdShowed() {
        sharedPref.edit().putLong("interstitialAdShowed", System.currentTimeMillis()).apply()
    }

    fun getDurationToLoadInterstitialAd(): Long {
        val currentTime = System.currentTimeMillis()
        val lastTime = sharedPref.getLong("interstitialAdShowed", currentTime - INTERSTITIAL_AD_DURATION) + INTERSTITIAL_AD_DURATION
        val ct = if (currentTime >= lastTime) 0 else lastTime - currentTime
        Log.d("TAG", "getDurationToLoadInterstitialAd: $ct")
        return ct
    }*/
}