package com.shubhamgupta16.justwallpapers.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.shubhamgupta16.justwallpapers.BuildConfig
import com.shubhamgupta16.justwallpapers.R
import com.shubhamgupta16.justwallpapers.models.init.BaseModel
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.justwallpapers.ui.main.MainActivity
import com.shubhamgupta16.justwallpapers.viewmodels.BaseInitViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: BaseInitViewModel by viewModels()
//    lazy app update manager
    private val appUpdateManager by lazy {AppUpdateManagerFactory.create(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.fetch()
        viewModel.liveIsLoading.observe(this) { isLoading ->
            if (isLoading == false) {
                if (viewModel.baseModel == null){
                    Toast.makeText(this, "Network Error!", Toast.LENGTH_SHORT).show()
                } else viewModel.baseModel?.let {
                        if (it.immediateUpdate != null && it.immediateUpdate >= BuildConfig.VERSION_CODE)
                            immediateUpdateApp(it)
                    else openMainActivity(it)

                }
                /*viewModel.wallModel?.let { wallModel ->
                    Glide.with(requireContext())
                        .load(wallModel.urls.regular ?: wallModel.urls.small)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.headerImage)


                    binding.openHeaderImage.setOnClickListener {
                        FullWallpaperActivity.getLaunchingIntent(
                            requireContext(), WallModelListHolder(
                                listOf(wallModel)
                            ), 0, 1, 1
                        ).let { intent ->
                            startActivity(intent)
                        }
                    }
                }
                binding.headerTitle.text = viewModel.title*/
            }
        }
    }

    private fun immediateUpdateApp(baseModel: BaseModel) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true)
                        .build()
                )
            } else {
                openMainActivity(baseModel)
            }
        }.addOnFailureListener {
            openMainActivity(baseModel)
        }

    }

    private fun openMainActivity(baseModel: BaseModel) {
        startActivity(Intent(this, MainActivity::class.java).apply { putExtra("baseModel", baseModel) })
        finish()
    }
}