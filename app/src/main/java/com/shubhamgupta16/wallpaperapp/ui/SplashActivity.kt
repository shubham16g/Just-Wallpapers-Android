package com.shubhamgupta16.wallpaperapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.room.InitDao
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
    lateinit var apiService: ApiService
    @Inject
    lateinit var initDao: InitDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        if (!application.main.isInitialized){
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.getInitData()
                withContext(Dispatchers.Main){
                    if (response.isSuccessful && response.body() != null) {
                        initDao.deleteAllCategories()
                        initDao.deleteAllColors()
                        response.body()?.categories?.forEach {
                            initDao.insertCategory(it)
                        }
                        response.body()?.colors?.forEach {
                            initDao.insertColor(it)
                        }
                        /*application.main.initialize(response.body()!!)*/
                        openMainActivity()
                    } else {
//                        todo network error
                    }
                }
            }

    }
    private fun openMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}