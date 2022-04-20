package com.shubhamgupta16.justwallpapers.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.justwallpapers.adapters.ColorsHorizontalAdapter
import com.shubhamgupta16.justwallpapers.databinding.FragmentForHorizontalListBinding
import com.shubhamgupta16.justwallpapers.viewmodels.live_observer.ListCase
import com.shubhamgupta16.justwallpapers.utils.BounceEdgeEffectFactory
import com.shubhamgupta16.justwallpapers.viewmodels.ColorsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HorizontalColorsFragment : Fragment() {

    private val viewModel: ColorsViewModel by viewModels()
    private lateinit var binding: FragmentForHorizontalListBinding
    private var adapter: ColorsHorizontalAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForHorizontalListBinding.inflate(inflater, container, false)
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
            viewModel.fetch()
    }
    var colorClickListener:((colorName:String, colorValue:Int)->Unit)?=null

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.edgeEffectFactory = BounceEdgeEffectFactory(true)

        adapter = ColorsHorizontalAdapter(viewModel.list) { colorName, colorValue ->
            colorClickListener?.let { it(colorName, colorValue) }
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