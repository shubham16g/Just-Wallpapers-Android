package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.adapters.AccountSettingsAdapter
import com.shubhamgupta16.wallpaperapp.adapters.AccountSettingsModel
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainAccountBinding
import com.shubhamgupta16.wallpaperapp.databinding.LayoutChooseThemeBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentMainAccountBinding
    @Inject lateinit var wallpaperHelper: WallpaperHelper
    @Inject lateinit var themeController: ThemeController
    private var themeDialog: AlertDialog? = null

    private val permissionLauncher = getPermissionLauncher { isAllPermissionGranted, _ ->
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
        AccountSettingsModel(R.drawable.ic_night, "App Theme"),
        AccountSettingsModel(R.drawable.ic_info, "About"),
        AccountSettingsModel(R.drawable.ic_help, "Privacy Policy"),
    )

    private val ad = AccountSettingsAdapter(settingsList) {
        when (it) {
            0 -> requireContext().shareText("Check this app", "Checkout this cool Wallpaper App\n${getString(R.string.app_name)}")
            1 -> requireContext().openPlayStorePage()
            2 -> themeDialog?.show()
        }
    }

    private fun getModeName() = when (themeController.getMode()) {
        AppCompatDelegate.MODE_NIGHT_YES -> requireContext().getString(R.string.mode_dark)
        AppCompatDelegate.MODE_NIGHT_NO -> requireContext().getString(R.string.mode_light)
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> requireContext().getString(R.string.mode_follow_system)
        else -> requireContext().getString(R.string.mode_follow_system)
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
        themeDialog = getThemeDialog()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            adapter = ad
        }
        updateThemeName()
    }

    private fun updateThemeName(){
        settingsList[2].subTitle = getModeName()
        ad.notifyItemChanged(2)
    }

    private fun getThemeDialog(): AlertDialog? {
        val themeLayout =
            LayoutChooseThemeBinding.inflate(layoutInflater)
        val dialog = requireContext().alertDialog(themeLayout)
        themeLayout.dark.setOnClickListener{
            themeController.setMode(AppCompatDelegate.MODE_NIGHT_YES)
            updateThemeName()
            dialog.dismiss()
        }
        themeLayout.light.setOnClickListener {
            themeController.setMode(AppCompatDelegate.MODE_NIGHT_NO)
            updateThemeName()
            dialog.dismiss()
        }
        themeLayout.followSystem.setOnClickListener {
            themeController.setMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            updateThemeName()
            dialog.dismiss()
        }
        return dialog
    }

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
}