package com.shubhamgupta16.wallpaperapp.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.shubhamgupta16.wallpaperapp.adapters.CategoriesAdapter
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.adapters.SingleImageAdapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentFeaturedBinding
import com.shubhamgupta16.wallpaperapp.databinding.FragmentForHorizontalListBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.CategoriesViewModel
import com.shubhamgupta16.wallpaperapp.viewmodels.FeaturedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil
import kotlin.math.roundToInt

@AndroidEntryPoint
class FeaturedFragment : Fragment() {

    private val viewModel: FeaturedViewModel by viewModels()
    private lateinit var binding: FragmentFeaturedBinding
    private var adapter: SingleImageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeaturedBinding.inflate(inflater, container, false)
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
                    ListCase.ADDED_RANGE -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                        if (viewModel.list.isNotEmpty())
                            binding.viewPager2.setCurrentItem(ceil(viewModel.list.lastIndex/2f).roundToInt(), true)
                    }
                    ListCase.REMOVED_RANGE -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                    }
                    ListCase.NO_CHANGE -> {
                    }
                    else -> {}
                }
            }
        }
        fetch()
    }

    private fun fetch() {
        if (viewModel.list.isEmpty())
            viewModel.fetch()
    }

    private fun setupRecyclerView() {
        adapter = SingleImageAdapter(requireContext(), viewModel.list) {
//            click it
        }
        binding.viewPager2.adapter = adapter
        binding.viewPager2.applyCarousels(32.px, 24f.px, 0f)

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = " "
        }.attach()
    }

    /*private fun showFullWallpaperFragment(position: Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            requireContext(), WallModelListHolder(viewModel.list.filterNotNull()), position,
            viewModel.page,
            viewModel.lastPage,
            viewModel.query,
            viewModel.color,
            viewModel.category
        )
        startActivity(intent)
    }*/
}