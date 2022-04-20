package com.shubhamgupta16.justwallpapers.ui

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.shubhamgupta16.justwallpapers.R
import com.shubhamgupta16.justwallpapers.adapters.SingleImageAdapter
import com.shubhamgupta16.justwallpapers.databinding.ActivityFullWallpaperBinding
import com.shubhamgupta16.justwallpapers.databinding.LayoutInfoBinding
import com.shubhamgupta16.justwallpapers.databinding.LayoutSetOnBinding
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallModel
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.justwallpapers.utils.*
import com.shubhamgupta16.justwallpapers.viewmodels.PagerViewModel
import com.shubhamgupta16.justwallpapers.viewmodels.live_observer.ListCase
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation
import javax.inject.Inject


@AndroidEntryPoint
class FullWallpaperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullWallpaperBinding
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private val viewModel: PagerViewModel by viewModels()
    private var adapter: SingleImageAdapter? = null
    private var screenMeasure: ScreenMeasure? = null
    @Inject lateinit var appMemory: AppMemory
    private fun initScreenMeasure() {
        if (screenMeasure == null) {
            screenMeasure = ScreenMeasure(this)
            binding.viewPager2.pivotY = screenMeasure!!.pivot()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullWallpaperBinding.inflate(layoutInflater)
        fitFullScreen()
        setTransparentStatusBar()
        setTransparentNavigation()
        initInterstitial()
        setContentView(binding.root)

        permissionLauncher = getPermissionLauncher { isAllPermissionGranted, _ ->
            if (isAllPermissionGranted)
                processDownloadWallpaper()
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

        val navigationHeight = getNavigationBarHeight()
        binding.navigationOverlay.setPadding(0, 0, 0, navigationHeight)
        setupViewPager()

        val position = if (viewModel.currentPosition >= 0) viewModel.currentPosition
        else intent.getIntExtra("position", 0)
        val data: Uri? = intent.data
        when {
            intent.hasExtra("list") -> {
                val wallModelListHolder = intent.getSerializableExtra("list") as WallModelListHolder
                initOnList(wallModelListHolder.list)
                binding.viewPager2.setCurrentItem(position, true)
                renderOtherComponents(position)
            }
            data != null -> {
                processOnUri(data)
                viewModel.fetch()
            }
            else -> {
                Toast.makeText(this, "Internal Error", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }


        viewModel.listObserver.observe(this) {
            it?.let {
                when (it.case) {
                    ListCase.INITIAL_LOADING -> {
                    }
                    ListCase.UPDATED -> {
                        if (it.at == viewModel.currentPosition)
                            updateFavButton(viewModel.list[viewModel.currentPosition].isFav)
                    }
                    ListCase.ADDED_RANGE -> {
                        adapter?.notifyItemRangeInserted(it.from, it.itemCount)
                    }
                    ListCase.REMOVED_RANGE -> {
                        adapter?.notifyItemRangeRemoved(it.from, it.itemCount)
                    }
                    ListCase.NO_CHANGE -> {
                    }
                    ListCase.EMPTY -> {

                    }
                }
            }
        }

        viewModel.wallBitmapLoading.observe(this) {
            when {
                it == null -> {
                    binding.setWallpaperButton.isEnabled = true
                    binding.setWallpaperProgress.fadeVisibility(View.INVISIBLE)
                }
                it -> {
                    binding.setWallpaperButton.isEnabled = false
                    binding.setWallpaperProgress.visibility = View.VISIBLE
                }
                else -> {
                    binding.setDoneTick.playAndHide()
                }
            }
        }

        viewModel.downloadBitmapLoading.observe(this) {
            when {
                it == null -> {
                    binding.downloadButton.isEnabled = true
                    binding.downloadProgress.fadeVisibility(View.INVISIBLE)
                }
                it -> {
                    binding.downloadButton.isEnabled = false
                    binding.downloadProgress.visibility = View.VISIBLE
                }
                else -> {
                    binding.downloadDoneTick.playAndHide()
                }
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.favButton.setOnClickListener {
            viewModel.toggleFav(viewModel.currentPosition)
        }

        binding.shareButton.setOnClickListener {
            shareText(
                "Checkout this Cool Wallpaper - Wallpaper App",
                "Checkout this Cool Wallpaper\n ${getString(R.string.SCHEMA)}://${getString(R.string.BASE_URL)}/id/${viewModel.list[viewModel.currentPosition].wallId}"
            )
        }

        binding.downloadButton.setOnClickListener {
            if (isHaveWriteExternalStoragePermission()) {
                processDownloadWallpaper()
            } else {
                permissionLauncher?.launchPermission(this, write = true)
            }
        }

        setupInfoAlert()

        setupSetWallAlert()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                renderOtherComponents(position)
            }
        })
    }

    private fun setupInfoAlert(){
        val infoLayout = LayoutInfoBinding.inflate(layoutInflater)
        val infoDialog = alertDialog(infoLayout)
        binding.infoButton.setOnClickListener {
            val model = viewModel.list[viewModel.currentPosition]
            infoLayout.link.text = model.source
            infoLayout.authority.text = model.source.toUri().authority?.substringBefore(".", "")
                ?.replaceFirstChar { it.uppercase() }
            infoLayout.downloads.text = "Downloads: ${model.downloads}"
            infoLayout.license.text = model.license
            addTags(
                infoLayout.chipGroup,
                model.categories + model.tags.map { it.replaceFirstChar { c -> c.uppercase() } })
            if (model.author == null) {
                infoLayout.authorContainer.visibility = View.INVISIBLE
                return@setOnClickListener
            }
            infoLayout.authorContainer.visibility = View.VISIBLE
            infoLayout.authorName.text = model.author.name
            Glide.with(this).load(model.author.image).centerCrop().circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(infoLayout.authorProfile)
            infoDialog.show()
        }
    }

    private fun setupSetWallAlert(){
        val setOnDialog =
            LayoutSetOnBinding.inflate(layoutInflater)
        val dialog = alertDialog(setOnDialog)
        binding.setWallpaperButton.setOnClickListener {
            if (isOrientationLandscape()) {
                Toast.makeText(this, "Set Wallpapers only in Portrait Mode", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val model = viewModel.list[viewModel.currentPosition]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setOnDialog.onHomeScreenBtn.setOnClickListener {
                    viewModel.applyWallpaper(applicationContext, model, WallpaperManager.FLAG_SYSTEM)
                    dialog.dismiss()
                }
                setOnDialog.onLockScreenBtn.setOnClickListener {
                    viewModel.applyWallpaper(applicationContext, model, WallpaperManager.FLAG_LOCK)
                    dialog.dismiss()
                }
                setOnDialog.onBothScreenBtn.setOnClickListener {
                    viewModel.applyWallpaper(applicationContext, model)
                    dialog.dismiss()
                }
                dialog.show()
            } else {
                viewModel.applyWallpaper(applicationContext, model)
            }
        }
    }

    private fun addTags(chipGroup: ChipGroup, list: List<String>) {
        chipGroup.removeAllViews()
        for (i in list) {
            chipGroup.addView((layoutInflater.inflate(R.layout.chip_tag, chipGroup, false)
                    as Chip).apply {
                text = i
            })
        }
    }

    private fun processDownloadWallpaper() {
        val model = viewModel.list[viewModel.currentPosition]
        viewModel.downloadWallpaper(this, model)
    }

    private fun processOnUri(uri: Uri) {
        val segments = uri.pathSegments
        Log.d(TAG, "processOnUri: $segments")
        if (segments.contains("id"))
            viewModel.init(segments.last().toInt())
    }

    private fun initOnList(list: List<WallModel>) {
        viewModel.init(
            list,
            intent.getIntExtra("page", 1),
            intent.getIntExtra("lastPage", 1),
            query = intent.getStringExtra("query"),
            color = intent.getStringExtra("color"),
            category = intent.getStringExtra("category"),
            orderBy = intent.getStringExtra("order_by")
        )
    }

    private var thumbEven = false
    var isLastTaskCompleted = true
    val h = Handler(Looper.getMainLooper())
    val runnable = {
        isLastTaskCompleted = true
    }

    private fun renderOtherComponents(position: Int) {
        viewModel.currentPosition = position
        if (position == viewModel.list.lastIndex)
            viewModel.fetch()
        val wallModel = viewModel.list[position]
        updateDetails(wallModel)
        updateFavButton(wallModel.isFav)

        val multiTransformation = MultiTransformation(
            RotationTransform((wallModel.rotation ?: 0f).toFloat()),
            FastBlurTransform(),
            BrightnessFilterTransformation(0.2f),
            ColorFilterTransformation(darkenColor(wallModel.color, 156))
        )



        Glide.with(this@FullWallpaperActivity).load(wallModel.urls.small)
            .transform(multiTransformation).addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (isLastTaskCompleted) {
                        thumbEven = !thumbEven
                        binding.thumbViewEven.fadeVisibility(
                            if (thumbEven) View.INVISIBLE else View.VISIBLE,
                            400
                        )
                    }
                    if (binding.thumbView.visibility != View.VISIBLE)
                        h.postDelayed({
                            binding.thumbView.fadeVisibility(View.VISIBLE, 400)
                        }, 100)

                    val status = isLastTaskCompleted
                    isLastTaskCompleted = false
                    Log.d("TAG", "onResourceReady: $status")
                    h.removeCallbacks(runnable)
                    h.postDelayed(runnable, 400)
                    return !status
                }
            })
            .into(if (thumbEven) binding.thumbViewEven else binding.thumbView)

    }

    private fun updateFavButton(fav: Boolean) {
        binding.favButton.setImageResource(
            if (fav) R.drawable.ic_favorite_active
            else R.drawable.ic_favorite
        )
    }

    private fun setupViewPager() {

        adapter = SingleImageAdapter(this, viewModel.list) {
            if (isOrientationLandscape()) return@SingleImageAdapter
            if (!isZoom) animateZoom() else animateZoomOut()
        }
        binding.viewPager2.adapter = adapter
        binding.viewPager2.apply3DSwiper()
    }

    private var isZoom = false
    val r = Runnable {
        if (isZoom) hideSystemUI() else showSystemUI()
        val y = if (!isZoom) 0f else screenMeasure!!.actionButtonsRowY()
        binding.buttonsRow?.let {
            val animator = it.animate().translationY(y).setDuration(170)
            animator.interpolator = DecelerateInterpolator()
            animator.start()
        }
    }

    private fun animateZoom() {
        binding.backButton.playForward(1.5f)
        initScreenMeasure()
        val interpolatorCompat = PathInterpolatorCompat.create(0.4f, -0.03f, .26f, 1.1f)
//        val interpolatorCompat = PathInterpolatorCompat.create(0.32f,0.41f,1f,.26f)
        isZoom = true
        binding.viewPager2.animate().scaleX(screenMeasure!!.scale()).scaleY(screenMeasure!!.scale())
            .setInterpolator(interpolatorCompat)
            .setDuration(200).start()
        binding.viewPager2.isUserInputEnabled = false
        animateActionCard(false)
        h.post(r)
    }

    private fun animateZoomOut() {
        binding.backButton.playReverse(1.5f)
        initScreenMeasure()
        isZoom = false
        binding.viewPager2.animate().scaleX(1f).scaleY(1f).setDuration(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
        binding.viewPager2.isUserInputEnabled = true
        animateActionCard(true)
        h.post(r)
    }

    private var isActionCardVisible = true

    private fun animateActionCard(isVisible: Boolean) {
        val y = if (isVisible) 0f else screenMeasure!!.actionCollapsedY()
        if (isVisible != isActionCardVisible) {
            val animator = binding.actionCard.animate().translationY(y).setDuration(200)
            animator.interpolator = DecelerateInterpolator()
            animator.start()
            isActionCardVisible = isVisible
        }
    }

    override fun onBackPressed() {
        when {
            viewModel.isShowingSharedImage() -> {
                val intent = Intent(this, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            isZoom -> animateZoomOut()
            else -> {
                showInterstitial()
                super.onBackPressed()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateDetails(model: WallModel) {
        binding.license.text = model.license
        binding.downloads.text = "Downloads: ${model.downloads}"
        if (model.author == null) {
            binding.authorContainer.visibility = View.INVISIBLE
            return
        }
        binding.authorContainer.visibility = View.VISIBLE
        binding.authorName.text = model.author.name
        Glide.with(this).load(model.author.image).centerCrop().circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.authorProfile)
    }

    private var interstitialAd:InterstitialAd?=null
    private fun initInterstitial() {
        h.postDelayed({
            InterstitialAd.load(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        super.onAdLoaded(interstitialAd)
                        this@FullWallpaperActivity.interstitialAd = interstitialAd
                    }
                })
        }, appMemory.getDurationToLoadInterstitialAd())

    }
    private fun showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd?.show(this)
            appMemory.interstitialAdShowed()
        }
    }

    companion object {
        private const val TAG = "FullWallpaperActivity"
        fun getLaunchingIntent(
            context: Context,
            listHolder: WallModelListHolder,
            position: Int,
            page: Int,
            lastPage: Int,
            query: String? = null,
            color: String? = null,
            category: String? = null,
            orderBy: String? = null,
        ): Intent {
            val intent = Intent(context, FullWallpaperActivity::class.java)
            intent.putExtra("list", listHolder)
            intent.putExtra("position", position)
            intent.putExtra("page", page)
            intent.putExtra("lastPage", lastPage)
            intent.putExtra("query", query)
            intent.putExtra("color", color)
            intent.putExtra("category", category)
            intent.putExtra("order_by", orderBy)
            return intent
        }
    }
}