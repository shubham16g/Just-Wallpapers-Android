package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.ViewPager2Adapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainCategoriesBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import com.shubhamgupta16.wallpaperapp.viewmodels.CategoriesViewModel
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoriesBinding
    private val viewModel: CategoriesViewModel by viewModels()
    private var adapter: ViewPager2Adapter? = null
    var categoryName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setNormalStatusBar()
        if (requireContext().isOrientationLandscape()){
            binding.toolbar.visibility = View.GONE
        }
        if (!requireActivity().isUsingNightMode()) {
            requireActivity().lightStatusBar()
        }
        binding.root.setPadding(0,requireContext().getStatusBarHeight(),0,0)
        adapter = ViewPager2Adapter(childFragmentManager, lifecycle)
        binding.viewPager2.reduceDragSensitivity()
        viewModel.listObserver.observe(viewLifecycleOwner) {
            it?.let {
                when (it.case) {
                    ListCase.ADDED_RANGE -> {
                        adapter?.clear()
                        var initPosition: Int? = null
                        for ((i, model) in viewModel.list.withIndex()) {
                            adapter?.addFragment(model.name)
                            if (categoryName == model.name)
                                initPosition = i
                        }
                        binding.viewPager2.adapter = adapter
                        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                            tab.text = viewModel.list[position].name
                        }.attach()

                        binding.viewPager2.doOnLayout {
                            initPosition?.let { it1 -> binding.viewPager2.currentItem = it1 }
                        }
                    }
                    else -> {}
                }
            }
        }
        viewModel.fetch()

//        binding.viewPager2.isUserInputEnabled = false
    }
}