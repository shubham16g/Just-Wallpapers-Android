package com.shubhamgupta16.wallpaperapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.repositories.InitRepository
import com.shubhamgupta16.wallpaperapp.room.CategoryDao
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

    @Inject
    lateinit var initRepository: InitRepository

    @Inject
    lateinit var categoryDao: CategoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            val response = initRepository.fetchInit()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    openMainActivity()
                } else {
//                    todo network error
                }
            }
        }

    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}