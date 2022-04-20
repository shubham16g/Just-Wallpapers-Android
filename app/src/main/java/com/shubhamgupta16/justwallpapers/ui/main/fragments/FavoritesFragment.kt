package com.shubhamgupta16.justwallpapers.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shubhamgupta16.justwallpapers.databinding.FragmentMainFavoritesBinding
import com.shubhamgupta16.justwallpapers.ui.components.VerticalWallpapersFragment
import com.shubhamgupta16.justwallpapers.utils.*
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