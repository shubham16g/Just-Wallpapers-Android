package com.shubhamgupta16.wallpaperapp.ui.components

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentForVerticalWallpapersBinding
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.ui.FullWallpaperActivity
import com.shubhamgupta16.wallpaperapp.utils.PaginationController
import com.shubhamgupta16.wallpaperapp.viewmodels.WallpapersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerticalWallpapersFragment : Fragment() {

    private val viewModel: WallpapersViewModel by viewModels()
    private lateinit var binding: FragmentForVerticalWallpapersBinding

    private var paginationController: PaginationController? = null
    private var adapter: ImagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.setQuery(it.getString(QUERY))
            viewModel.setCategory(it.getString(CATEGORY))
            viewModel.setColor(it.getString(COLOR))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForVerticalWallpapersBinding.inflate(inflater, container, false)
//        Log.d(TAG, "onCreateView: ${viewModel.category}")
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        Log.d(TAG, "onAttach: ${viewModel.category}")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored: ${TAG}")
//        Log.d(TAG, "onViewStateRestored: ${viewModel.list}")

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onVCreated: ${viewModel.category}")
        Log.d(TAG, "onVCreated: ${viewModel.list}")

        setupRecyclerView()
        viewModel.listObserver.observe(viewLifecycleOwner) {
            it?.let {
                Log.d(TAG, "onViewCreated: Fetched ${it.case} -> ${viewModel.list.size}")
                when (it.case) {
                    ListCase.UPDATED -> {
                        adapter?.notifyItemChanged(it.at)
                    }
                    ListCase.ADDED_RANGE -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                        if (viewModel.list.isNotEmpty() && it.from == 0)
                            binding.recyclerView.smoothScrollToPosition(0)
                        paginationController?.notifyDataFetched(true)
                    }
                    ListCase.REMOVED_RANGE -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                    }
                    ListCase.NO_CHANGE -> {
                        paginationController?.notifyDataFetched(true)
                    }
                    else -> {}
                }
            }
        }
        viewModel.fetch()
        Log.d(TAG, "onViewCreated: VERTICAL FETCH")
    }

 /*   override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: called")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored: called")
    }*/

    private fun setupRecyclerView() {
        val manager = StaggeredGridLayoutManager(
            resources.getInteger(R.integer.card_span_count),
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.recyclerView.layoutManager = manager
        adapter = ImagesAdapter(requireContext(), viewModel.list) { _, i ->
            showFullWallpaperFragment(i)
        }
        binding.recyclerView.adapter = adapter
        paginationController = PaginationController(binding.recyclerView, manager) {
            viewModel.fetch()
        }
    }


    private fun showFullWallpaperFragment(position:Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            requireContext(), WallModelListHolder(viewModel.list.filterNotNull()), position,
            viewModel.page,
            viewModel.lastPage,
            viewModel.query,
            viewModel.color,
            viewModel.category
        )
        startActivity(intent)
    }


    companion object {
        private const val TAG = "VerticalWallpapersFrag"
        private const val QUERY = "query"
        private const val CATEGORY = "category"
        private const val COLOR = "color"
        private const val ORDER_BY = "order_by"
        fun getInstance(query: String? = null, category: String? = null, color: String? = null): VerticalWallpapersFragment {
            return VerticalWallpapersFragment().apply {
                arguments = Bundle().apply {
                    putString(QUERY, query)
                    putString(CATEGORY, category)
                    putString(COLOR, color)
                }
            }
        }
    }
}