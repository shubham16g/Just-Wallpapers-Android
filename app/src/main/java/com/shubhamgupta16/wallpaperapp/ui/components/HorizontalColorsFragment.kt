package com.shubhamgupta16.wallpaperapp.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.wallpaperapp.adapters.ColorsHorizontalAdapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentListingBinding
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.ColorsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HorizontalColorsFragment : Fragment() {

    private val viewModel: ColorsViewModel by viewModels()
    private lateinit var binding: FragmentListingBinding
    private var adapter: ColorsHorizontalAdapter? = null

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
                    ListCase.ADDED_RANGE -> {
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
        fetch()
    }

    private fun fetch() {
        if (viewModel.list.isEmpty())
            viewModel.fetch(requireActivity().application)
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = manager
        adapter = ColorsHorizontalAdapter(viewModel.list) { categoryName, i ->
//            showFullWallpaperFragment(i)
            Toast.makeText(requireContext(), categoryName, Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.adapter = adapter
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