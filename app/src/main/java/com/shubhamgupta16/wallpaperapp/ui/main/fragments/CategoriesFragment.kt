package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shubhamgupta16.wallpaperapp.adapters.ViewPager2Adapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainCategoriesBinding
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainHomeBinding
import com.shubhamgupta16.wallpaperapp.initData
import com.shubhamgupta16.wallpaperapp.ui.ListingActivity
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalCategoriesFragment
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalColorsFragment
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalWallpapersFragment
import dagger.hilt.android.AndroidEntryPoint


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoriesBinding
    private var adapter: ViewPager2Adapter<Fragment>? = null

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
        adapter!!.addFragment(HorizontalColorsFragment())
        adapter!!.addFragment(HorizontalColorsFragment())
        adapter!!.addFragment(HorizontalColorsFragment())
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Tutors"
                1 -> tab.text = "Courses"
                2 -> tab.text = "e-Library"
            }
        }.attach()

    }
}