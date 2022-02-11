package com.shubhamgupta16.wallpaperapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.databinding.ActivityListingBinding
import com.shubhamgupta16.wallpaperapp.models.app.WallModelList
import com.shubhamgupta16.wallpaperapp.ui.fragment.ListingFragment
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel

class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingBinding

    //    private lateinit var viewModel: ListingViewModel
    private val viewModel: ListingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
//        fitFullScreen()
        setContentView(binding.root)

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
        /*val intent = Intent(this, FullWallpaperActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("list", WallModelList(viewModel.list.filterNotNull()))
        startActivity(intent)*/
        /*supportFragmentManager.beginTransaction()
            .add(binding.container.id, FullWallpaperFragment.getInstance(position))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()*/
    }


}