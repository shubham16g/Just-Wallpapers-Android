package com.shubhamgupta16.justwallpapers.ui.main.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhamgupta16.justwallpapers.R
import com.shubhamgupta16.justwallpapers.adapters.AccountSettingsAdapter
import com.shubhamgupta16.justwallpapers.adapters.AccountSettingsModel
import com.shubhamgupta16.justwallpapers.databinding.FragmentMainAccountBinding
import com.shubhamgupta16.justwallpapers.databinding.LayoutChooseThemeBinding
import com.shubhamgupta16.justwallpapers.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentMainAccountBinding

    @Inject
    lateinit var wallpaperHelper: WallpaperHelper

    @Inject
    lateinit var appMemory: AppMemory
    private var themeDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val settingsList = listOf(
        AccountSettingsModel(R.drawable.ic_send, "Share with Friends"),
        AccountSettingsModel(R.drawable.ic_feedback, "Feedback"),
        AccountSettingsModel(R.drawable.ic_night, "App Theme"),
        AccountSettingsModel(R.drawable.ic_help, "Privacy Policy"),
        AccountSettingsModel(R.drawable.ic_info, "Terms & Conditions"),
    )

    private val ad = AccountSettingsAdapter(settingsList) {
        when (it) {
            0 -> requireContext().shareText(
                getString(R.string.app_name),
                "Checkout this cool Wallpaper App\n${getString(R.string.app_name)} : https://play.google.com/store/apps/details?id=${requireContext().packageName}"
            )
            1 -> requireContext().openPlayStorePage()
            2 -> themeDialog?.show()
            3 -> requireContext().openLink(getString(R.string.privacy_policy_url))
            4 -> requireContext().openLink(getString(R.string.terms_and_conditions_url))
        }
    }

    private fun getModeName() = when (appMemory.getMode()) {
        AppCompatDelegate.MODE_NIGHT_YES -> requireContext().getString(R.string.mode_dark)
        AppCompatDelegate.MODE_NIGHT_NO -> requireContext().getString(R.string.mode_light)
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> requireContext().getString(R.string.mode_follow_system)
        else -> requireContext().getString(R.string.mode_follow_system)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setNormalStatusBar()

        if (!requireActivity().isUsingNightMode()) {
            requireActivity().lightStatusBar()
        } else {
            requireActivity().nonLightStatusBar()
        }
        binding.root.setPadding(0, requireContext().getStatusBarHeight(), 0, 0)

//        binding.deviceName.text = Build.MODEL
        themeDialog = getThemeDialog()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            adapter = ad
        }
        updateThemeName()
    }

    private fun updateThemeName() {
        settingsList[2].subTitle = getModeName()
        ad.notifyItemChanged(2)
    }

    private fun getThemeDialog(): AlertDialog {
        val themeLayout =
            LayoutChooseThemeBinding.inflate(layoutInflater)
        val dialog = requireContext().alertDialog(themeLayout)
        themeLayout.dark.setOnClickListener {
            setMode(AppCompatDelegate.MODE_NIGHT_YES)
            updateThemeName()
            dialog.dismiss()
        }
        themeLayout.light.setOnClickListener {
            setMode(AppCompatDelegate.MODE_NIGHT_NO)
            updateThemeName()
            dialog.dismiss()
        }
        themeLayout.followSystem.setOnClickListener {
            setMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            updateThemeName()
            dialog.dismiss()
        }
        return dialog
    }

    private fun setMode(mode: Int) {
        if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM || mode == AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY ||
            mode == AppCompatDelegate.MODE_NIGHT_YES || mode == AppCompatDelegate.MODE_NIGHT_NO
        ) {
            appMemory.saveMode(mode)
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

}