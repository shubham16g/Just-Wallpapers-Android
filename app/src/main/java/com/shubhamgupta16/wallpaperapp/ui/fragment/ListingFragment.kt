package com.shubhamgupta16.wallpaperapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.ImagesAdapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentListingBinding
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.utils.PaginationController
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel

class ListingFragment : Fragment() {

    private lateinit var viewModel: ListingViewModel
    var listener: ((position:Int)->Unit)?=null
    private lateinit var binding: FragmentListingBinding

    private var paginationController: PaginationController? = null
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
            listener?.let { it(i) }
            /*val intent = Intent(this, FullWallpaperActivity::class.java)
            intent.putExtra("position", i)
            intent.putExtra("list", WallModelList(viewModel.list))
            startActivity(intent)*/
        }
        binding.recyclerView.adapter = adapter
        paginationController = PaginationController(binding.recyclerView, manager) {
            viewModel.fetch()
        }
    }
}