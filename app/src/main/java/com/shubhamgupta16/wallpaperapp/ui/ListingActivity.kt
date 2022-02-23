package com.shubhamgupta16.wallpaperapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.databinding.ActivityListingBinding
import com.shubhamgupta16.wallpaperapp.ui.components.VerticalWallpapersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
//        fitFullScreen()
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        showListingFragment()
    }

    private fun showListingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, VerticalWallpapersFragment.getInstance())
            .commit()
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ListingActivity::class.java))
        }
    }

}