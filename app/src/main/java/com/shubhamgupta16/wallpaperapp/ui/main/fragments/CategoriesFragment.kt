package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.shubhamgupta16.wallpaperapp.adapters.ViewPager2Adapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainCategoriesBinding
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalColorsFragment
import com.shubhamgupta16.wallpaperapp.ui.components.VerticalWallpapersFragment
import com.shubhamgupta16.wallpaperapp.viewmodels.CategoriesViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoriesBinding
    private val viewModel: CategoriesViewModel by viewModels()
    private var adapter: ViewPager2Adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ViewPager2Adapter(childFragmentManager, lifecycle)
        viewModel.listObserver.observe(viewLifecycleOwner) {
            it?.let {
                when (it.case) {
                    ListCase.ADDED_RANGE -> {
                        adapter?.clear()
                        viewModel.list.forEach { model->
                            adapter?.addFragment(model.name)
                        }
                        binding.viewPager2.adapter = adapter
                        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                            tab.text = viewModel.list[position].name
                        }.attach()
                    }
                    else -> {}
                }
            }
        }
        viewModel.fetch(requireActivity().application)

        binding.viewPager2.isUserInputEnabled = false
    }
}