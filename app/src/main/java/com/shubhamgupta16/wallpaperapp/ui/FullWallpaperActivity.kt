package com.shubhamgupta16.wallpaperapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.databinding.ActivityFullWallpaperBinding
import com.shubhamgupta16.wallpaperapp.databinding.ActivityMainBinding

class FullWallpaperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullWallpaperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}