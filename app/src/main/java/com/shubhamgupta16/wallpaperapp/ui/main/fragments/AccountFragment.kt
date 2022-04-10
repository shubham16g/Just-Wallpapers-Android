package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.AccountSettingsAdapter
import com.shubhamgupta16.wallpaperapp.adapters.AccountSettingsModel
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainAccountBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentMainAccountBinding
    @Inject
    lateinit var wallpaperHelper: WallpaperHelper
    private val permissionLauncher = getPermissionLauncher { isAllPermissionGranted, map ->
        if (isAllPermissionGranted)
            updateCurrentWallCards()
    }
    override fun onStart() {
        super.onStart()
        updateCurrentWallCards()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val settingsList = listOf(
        AccountSettingsModel(R.drawable.ic_send, "Tell a Friend"),
        AccountSettingsModel(R.drawable.ic_feedback, "Feedback"),
        AccountSettingsModel(R.drawable.ic_night, "Night Mode"),
        AccountSettingsModel(R.drawable.ic_info, "About"),
        AccountSettingsModel(R.drawable.ic_help, "Privacy Policy"),
    )

    @SuppressLint("SetTextI18n")
    private fun updateCurrentWallCards() {
        wallpaperHelper.getCurrentWall()?.let {
            if (it.bitmap != null) {
                binding.currentWall.visibility = View.VISIBLE
                binding.liveWallIcon.visibility = View.INVISIBLE
                binding.liveWallText.visibility = View.INVISIBLE
                binding.currentWall.setImageBitmap(it.bitmap)
            }
            else {
                binding.currentWall.visibility = View.INVISIBLE
                binding.liveWallIcon.visibility = View.VISIBLE
                binding.liveWallText.visibility = View.VISIBLE
                binding.liveWallIcon.setImageDrawable(it.icon)
                binding.liveWallText.text = "${it.description}\nLive Wallpaper"
            }

        }
        wallpaperHelper.getLockScreenWall()?.let {
            if (it.bitmap != null) {
                binding.currentWall2.visibility = View.VISIBLE
                binding.liveWallIcon2.visibility = View.INVISIBLE
                binding.liveWallText2.visibility = View.INVISIBLE
                binding.currentWall2.setImageBitmap(it.bitmap)
            }
            else {
                binding.currentWall2.visibility = View.INVISIBLE
                binding.liveWallIcon2.visibility = View.VISIBLE
                binding.liveWallText2.visibility = View.VISIBLE
                binding.liveWallIcon2.setImageDrawable(it.icon)
                binding.liveWallText2.text = "${it.description}\nLive Wallpaper"
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setNormalStatusBar()

        permissionLauncher.launchPermission(requireContext(), true)
        if (!requireActivity().isUsingNightMode()) {
            requireActivity().lightStatusBar()
        }
        binding.root.setPadding(0, requireContext().getStatusBarHeight(), 0, 0)

        binding.deviceName.text = Build.MODEL

        val ad = AccountSettingsAdapter(settingsList) {
            when (it) {
                "Search" -> Toast.makeText(requireContext(), "SEARCH", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            adapter = ad
        }
    }
}