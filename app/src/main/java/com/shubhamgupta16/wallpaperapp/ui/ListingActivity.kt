package com.shubhamgupta16.wallpaperapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.databinding.ActivityListingBinding
import com.shubhamgupta16.wallpaperapp.models.app.WallModelList
import com.shubhamgupta16.wallpaperapp.ui.fragment.ListingFragment
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingBinding

    //    private lateinit var viewModel: ListingViewModel
    private val viewModel: ListingViewModel by viewModels()

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
            .replace(binding.container.id, ListingFragment().apply {
                listener = {
                    showFullWallpaperFragment(it)
                }
            })
            .commit()
    }

    private fun showFullWallpaperFragment(position:Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            this, WallModelList(viewModel.list.filterNotNull()), position,
            viewModel.page,
            viewModel.lastPage,
            viewModel.query,
            viewModel.color,
            viewModel.category
        )
        startActivity(intent)
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ListingActivity::class.java))
        }
    }

}