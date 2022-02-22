package com.shubhamgupta16.wallpaperapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.main
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!application.main.isInitialized){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.getInitData()
                withContext(Dispatchers.Main){
                    if (response.isSuccessful && response.body() != null) {
                        application.main.initialize(response.body()!!)
                        openMainActivity()
                    } else {
//                        todo network error
                    }
                }
            }
        } else
            Handler(Looper.getMainLooper()).postDelayed({
                openMainActivity()
            }, 1000)
    }
    private fun openMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}