package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.AccountSettingsAdapter
import com.shubhamgupta16.wallpaperapp.adapters.AccountSettingsModel
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainAccountBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentMainAccountBinding
    private val permissionLauncher = getPermissionLauncher { isAllPermissionGranted, map ->
        if (isAllPermissionGranted)
            binding.currentWall.setImageDrawable(requireContext().getCurrentWall())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val settingsList = listOf(
        AccountSettingsModel(R.drawable.ic_icon_search, "Search")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setNormalStatusBar()

        permissionLauncher.launchPermission(requireContext(), true)
        if (requireContext().isOrientationLandscape()) {
            binding.toolbar.visibility = View.GONE
        }
        if (!requireActivity().isUsingNightMode()) {
            requireActivity().lightStatusBar()
        }
        binding.root.setPadding(0, requireContext().getStatusBarHeight(), 0, 0)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AccountSettingsAdapter(settingsList) {
                when (it) {
                    "Search" -> Toast.makeText(requireContext(), "SEARCH", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        binding.currentWall.setImageDrawable(requireContext().getCurrentWall())
//        childFragmentManager.beginTransaction()
//            .replace(binding.fragmentContainerView.id, VerticalWallpapersFragment.getInstanceForFavorite()).commit()
//        binding.viewPager2.isUserInputEnabled = false
    }
}