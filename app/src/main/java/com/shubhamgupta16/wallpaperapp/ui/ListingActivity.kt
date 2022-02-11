package com.shubhamgupta16.wallpaperapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.databinding.ActivityListingBinding
import com.shubhamgupta16.wallpaperapp.models.app.WallModelList
import com.shubhamgupta16.wallpaperapp.utils.PaginationController
import com.shubhamgupta16.wallpaperapp.utils.SingletonNameViewModelFactory
import com.shubhamgupta16.wallpaperapp.viewmodels.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel

class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingBinding
    private lateinit var viewModel: ListingViewModel
//    private val viewModel: ListingViewModel by viewModels()


    private var paginationController: PaginationController? = null
    private var adapter: ImagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, SingletonNameViewModelFactory())[ListingViewModel::class.java]
        binding = ActivityListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        setupRecyclerView()

        viewModel.listObserver.observe(this) {
            it?.let {
                when (it.case) {
                    ListCase.UPDATED -> {
                        adapter?.notifyItemChanged(it.from)
                    }
                    ListCase.ADDED -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                        paginationController?.notifyDataFetched(true)
                    }
                    ListCase.REMOVED -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                    }
                    ListCase.NO_CHANGE -> {
                        paginationController?.notifyDataFetched(true)
                    }
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
            val intent = Intent(this, FullWallpaperActivity::class.java)
            intent.putExtra("position", i)
            intent.putExtra("list", WallModelList(viewModel.list))
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
        paginationController = PaginationController(binding.recyclerView, manager) {
            viewModel.fetch()
        }
    }
}