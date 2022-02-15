package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.databinding.FragmentHomeBinding
import com.shubhamgupta16.wallpaperapp.ui.ListingActivity
import com.shubhamgupta16.wallpaperapp.ui.fragment.HorizontalListingFragment

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moreBtn.setOnClickListener {
            ListingActivity.open(requireContext())
        }

        (childFragmentManager.findFragmentById(binding.latestWallpaperFragment.id) as HorizontalListingFragment).fetch()
//        todo ui work
    }
}