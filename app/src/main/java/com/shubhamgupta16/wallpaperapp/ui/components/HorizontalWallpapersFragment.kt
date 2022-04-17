package com.shubhamgupta16.wallpaperapp.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.wallpaperapp.adapters.HorizontalImagesAdapter
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentForHorizontalListBinding
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import com.shubhamgupta16.wallpaperapp.ui.FullWallpaperActivity
import com.shubhamgupta16.wallpaperapp.utils.BounceEdgeEffectFactory
import com.shubhamgupta16.wallpaperapp.viewmodels.StaticWallpapersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HorizontalWallpapersFragment : Fragment() {

    companion object{
        private const val TAG = "HorizontalWallpapersFra"
    }
    private val viewModel: StaticWallpapersViewModel by viewModels()
    private lateinit var binding: FragmentForHorizontalListBinding
    private var adapter: HorizontalImagesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForHorizontalListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.filterFavorites()
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
                        if (viewModel.list.isNotEmpty() && it.from == 0)
                            binding.recyclerView.smoothScrollToPosition(0)
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
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
    }

    fun fetch(query: String? = null, category: String? = null, color: String? = null, orderBy:String? = null) {
        viewModel.init(query, category,color,orderBy)
        if (viewModel.list.isEmpty())
            viewModel.fetch()
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.edgeEffectFactory = BounceEdgeEffectFactory(true)
        adapter = HorizontalImagesAdapter(requireContext(), viewModel.list) { _, i ->
            showFullWallpaperFragment(i)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun showFullWallpaperFragment(position: Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            requireContext(), WallModelListHolder(viewModel.list), position,
            1,
            viewModel.lastPage,
            viewModel.query,
            viewModel.color,
            viewModel.category,
            viewModel.orderBy
        )
        startActivity(intent)
    }
}