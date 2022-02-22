package com.shubhamgupta16.wallpaperapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.databinding.ActivityListingBinding
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.utils.PaginationController
import com.shubhamgupta16.wallpaperapp.viewmodels.WallpapersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingBinding
    private var paginationController: PaginationController? = null
    private var adapter: ImagesAdapter? = null

    //    private lateinit var viewModel: ListingViewModel
    private val viewModel: WallpapersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
//        fitFullScreen()
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        setupRecyclerView()
        viewModel.listObserver.observe(this) {
            it?.let {
                when (it.case) {
                    ListCase.UPDATED -> {
                        adapter?.notifyItemChanged(it.at)
                    }
                    ListCase.ADDED_RANGE -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                        paginationController?.notifyDataFetched(true)
                    }
                    ListCase.REMOVED_RANGE -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                    }
                    ListCase.NO_CHANGE -> {
                        paginationController?.notifyDataFetched(true)
                    }
                    ListCase.LOADING -> {}
                }
            }
        }
        viewModel.fetch()
    }

    private fun setupRecyclerView() {
        val manager = StaggeredGridLayoutManager(
            resources.getInteger(R.integer.card_span_count),
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.recyclerView.layoutManager = manager
        adapter = ImagesAdapter(viewModel.list) { wallModel, i ->
            showFullWallpaperFragment(i)
        }
        binding.recyclerView.adapter = adapter
        paginationController = PaginationController(binding.recyclerView, manager) {
            viewModel.fetch()
        }
    }

    private fun showFullWallpaperFragment(position:Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            this, WallModelListHolder(viewModel.list.filterNotNull()), position,
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