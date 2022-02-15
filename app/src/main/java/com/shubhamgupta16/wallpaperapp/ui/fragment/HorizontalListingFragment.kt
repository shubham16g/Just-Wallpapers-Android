package com.shubhamgupta16.wallpaperapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentListingBinding
import com.shubhamgupta16.wallpaperapp.models.app.WallModelList
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.ui.FullWallpaperActivity
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel

class HorizontalListingFragment : Fragment() {

    private lateinit var viewModel: ListingViewModel
    private lateinit var binding: FragmentListingBinding
    private var adapter: ImagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProvider(it)[ListingViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewModel.listObserver.observe(viewLifecycleOwner) {
            it?.let {
                when (it.case) {
                    ListCase.UPDATED -> {
                        adapter?.notifyItemChanged(it.at)
                    }
                    ListCase.ADDED -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                    }
                    ListCase.REMOVED -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                    }
                    ListCase.NO_CHANGE -> {
                    }
                    else -> {}
                }
            }
        }
        viewModel.fetch()
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = manager
        adapter = ImagesAdapter(viewModel.list, true) { wallModel, i ->
            showFullWallpaperFragment(i)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun showFullWallpaperFragment(position: Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            requireContext(), WallModelList(viewModel.list.filterNotNull()), position,
            viewModel.page,
            viewModel.lastPage,
            viewModel.query,
            viewModel.color,
            viewModel.category
        )
        startActivity(intent)
    }
}