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
import com.shubhamgupta16.wallpaperapp.models.ad.NativeAdModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.wallpaperapp.ui.FullWallpaperActivity
import com.shubhamgupta16.wallpaperapp.utils.BounceEdgeEffectFactory
import com.shubhamgupta16.wallpaperapp.utils.PaginationController
import com.shubhamgupta16.wallpaperapp.utils.fadeVisibility
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingWallpapersViewModel
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerticalWallpapersFragment : Fragment() {

    private val viewModel: ListingWallpapersViewModel by viewModels()
    private lateinit var binding: FragmentForVerticalWallpapersBinding

    private var paginationController: PaginationController? = null
    private var adapter: ImagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(FAVORITE))
                viewModel.initForFavList()
            else
                viewModel.init(
                    it.getString(QUERY),
                    it.getString(CATEGORY),
                    it.getString(COLOR),
                    it.getString(ORDER_BY)
                )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.filterFavorites()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForVerticalWallpapersBinding.inflate(inflater, container, false)
//        Log.d(TAG, "onCreateView: ${viewModel.category}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewModel.listObserver.observe(viewLifecycleOwner) {
            it?.let {
                Log.d(TAG, "onViewCreated: Fetched ${it.case} -> ${viewModel.list.size}")
                when (it.case) {
                    ListCase.UPDATED -> {
                        adapter?.notifyItemChanged(it.at)
                        binding.initialLoader.fadeVisibility(View.GONE)
                        binding.noResultContainer.fadeVisibility(View.GONE)
                    }
                    ListCase.ADDED_RANGE -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                        if (viewModel.list.isNotEmpty() && it.from == 0)
                            binding.recyclerView.smoothScrollToPosition(0)
                        paginationController?.notifyDataFetched()
                        if (it.from == 0)
                            binding.recyclerView.scheduleLayoutAnimation()

                        binding.initialLoader.fadeVisibility(View.GONE)
                        binding.noResultContainer.fadeVisibility(View.GONE)
                    }
                    ListCase.REMOVED_RANGE -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                        binding.initialLoader.fadeVisibility(View.GONE)
                        binding.noResultContainer.fadeVisibility(View.GONE)
                    }
                    ListCase.REMOVED -> {
                        adapter?.notifyItemRemoved(it.at)
                        adapter?.notifyItemRangeChanged(it.at, viewModel.list.size)
                        binding.initialLoader.fadeVisibility(View.GONE)
                        binding.noResultContainer.fadeVisibility(View.GONE)
                    }
                    ListCase.NO_CHANGE -> {
                        paginationController?.notifyDataFetched()
                        binding.initialLoader.fadeVisibility(View.GONE)
                        binding.noResultContainer.fadeVisibility(View.GONE)
                    }
                    ListCase.INITIAL_LOADING -> {
                        binding.initialLoader.fadeVisibility(View.VISIBLE)
                        binding.noResultContainer.fadeVisibility(View.GONE)
                    }
                    ListCase.EMPTY -> {
                        binding.initialLoader.fadeVisibility(View.GONE)
                        binding.noResultContainer.fadeVisibility(View.VISIBLE)
                    }
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
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.edgeEffectFactory = BounceEdgeEffectFactory()
        paginationController = PaginationController(binding.recyclerView, manager) {
            viewModel.fetch()
        }
    }


    private fun showFullWallpaperFragment(position:Int) {
        val intent = FullWallpaperActivity.getLaunchingIntent(
            requireContext(),
            WallModelListHolder(
                viewModel.list.filterIsInstance<WallModel>()),
            position,
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
        private const val FAVORITE = "fav"
        private const val ORDER_BY = "order_by"
        fun getInstance(
            query: String? = null,
            category: String? = null,
            color: String? = null,
            orderBy: String? = null
        ): VerticalWallpapersFragment {
            return VerticalWallpapersFragment().apply {
                arguments = Bundle().apply {
                    putString(QUERY, query)
                    putString(CATEGORY, category)
                    putString(COLOR, color)
                    putString(ORDER_BY, orderBy)
                }
            }
        }

        fun getInstanceForFavorite(): VerticalWallpapersFragment {
            return VerticalWallpapersFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(FAVORITE, true)
                }
            }
        }
    }
}