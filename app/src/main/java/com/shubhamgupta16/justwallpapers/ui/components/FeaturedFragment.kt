package com.shubhamgupta16.justwallpapers.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shubhamgupta16.justwallpapers.adapters.SingleImageAdapter
import com.shubhamgupta16.justwallpapers.databinding.FragmentFeaturedBinding
import com.shubhamgupta16.justwallpapers.utils.*
import com.shubhamgupta16.justwallpapers.viewmodels.FeaturedViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewModel.liveIsLoading.observe(viewLifecycleOwner) {
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
        adapter = SingleImageAdapter(requireContext(), viewModel.list.) {
//            click it
        }
        binding.viewPager2.adapter = adapter
        binding.viewPager2.isUserInputEnabled = false
    }*/

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