package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shubhamgupta16.wallpaperapp.databinding.FragmentHomeBinding
import com.shubhamgupta16.wallpaperapp.initData
import com.shubhamgupta16.wallpaperapp.ui.ListingActivity
import com.shubhamgupta16.wallpaperapp.ui.fragment.HorizontalListingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        binding.newlyAddedHeader.setOnMoreClickListener {
            ListingActivity.open(requireContext())
        }

        (childFragmentManager.findFragmentById(binding.latestWallpaperFragment.id) as HorizontalListingFragment).fetch()

        Log.d("TAG", "InitData: ${requireActivity().application.initData}")
//        todo ui work
    }
}