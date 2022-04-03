package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.shubhamgupta16.wallpaperapp.adapters.ViewPager2Adapter
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainCategoriesBinding
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainFavoritesBinding
import com.shubhamgupta16.wallpaperapp.ui.ListingActivity
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalColorsFragment
import com.shubhamgupta16.wallpaperapp.ui.components.VerticalWallpapersFragment
import com.shubhamgupta16.wallpaperapp.utils.*
import com.shubhamgupta16.wallpaperapp.viewmodels.CategoriesViewModel
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentMainFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainFavoritesBinding.inflate(inflater, container, false)
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

        childFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, VerticalWallpapersFragment.getInstanceForFavorite()).commit()
//        binding.viewPager2.isUserInputEnabled = false
    }
}