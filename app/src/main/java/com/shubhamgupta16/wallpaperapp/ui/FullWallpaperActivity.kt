package com.shubhamgupta16.wallpaperapp.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.shubhamgupta16.wallpaperapp.adapters.SingleImageAdapter
import com.shubhamgupta16.wallpaperapp.databinding.ActivityFullWallpaperBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation


class FullWallpaperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullWallpaperBinding
    private lateinit var viewModel: ListingViewModel

    private var adapter: SingleImageAdapter? = null
    private var screenMeasure: ScreenMeasure? = null
    private fun initScreenMeasure() {
        if (screenMeasure == null) {
            screenMeasure = ScreenMeasure(this)
            binding.viewPager2.pivotY = screenMeasure!!.pivot()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, SingletonNameViewModelFactory())[ListingViewModel::class.java]
        binding = ActivityFullWallpaperBinding.inflate(layoutInflater)
        setTransparentStatusBar()
        setContentView(binding.root)

        val position = intent.getIntExtra("position", 0)

        val navigationHeight = getNavigationBarHeight()

        binding.navigationOverlay.setPadding(0, 0, 0, navigationHeight)

        setupViewPager()
        binding.viewPager2.currentItem = position
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val wallModel = viewModel.list[position] ?: return
//                Glide.get(this@FullWallpaperActivity).clearMemory()
                val multiTransformation = MultiTransformation(
                    RotationTransform((wallModel.rotation ?: 0f).toFloat()),
                    BrightnessFilterTransformation(0.5f),
                    ColorFilterTransformation(darkenColor(Color.parseColor(wallModel.color), 156)),
                    BlurTransformation(25, 1)
                )

                Glide.with(this@FullWallpaperActivity).asBitmap().load(wallModel.urls.small)
                    .thumbnail(0.4f)
                    .transform(multiTransformation)
                    .addBitmapListener { isReady, resource, e ->
                        if (isReady)
                            binding.imageView.fadeImage(resource)
                    }.submit()

            }
        })
    }

    private fun setupViewPager() {
        val h = Handler(Looper.getMainLooper())
        val r = Runnable {
            if (!isZoom) animateZoom() else animateZoomOut()
        }
        adapter = SingleImageAdapter(this, viewModel.list) {
            if (!isZoom) hideSystemUI() else showSystemUI()
            h.post(r)
        }
        binding.viewPager2.adapter = adapter
        binding.viewPager2.apply3DSwiper()
        binding.viewPager2.offscreenPageLimit = 1
        /*(binding.viewPager2.getChildAt(0)as RecyclerView).apply {
            setItemViewCacheSize(50)
            setHasFixedSize(true)
        }*/
    }

    private var isZoom = false
    private fun animateZoom() {
        initScreenMeasure()
        isZoom = true
        binding.viewPager2.animate().scaleX(screenMeasure!!.scale()).scaleY(screenMeasure!!.scale())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(200).start()
        binding.viewPager2.isUserInputEnabled = false
        animateActionCard(false)
    }

    private fun animateZoomOut() {
        initScreenMeasure()
        isZoom = false
        binding.viewPager2.animate().scaleX(1f).scaleY(1f).setDuration(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
        binding.viewPager2.isUserInputEnabled = true
        animateActionCard(true)
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

//    private fun updateAuthor(author: AdminAuthor?) {
//        if (author == null) {
//            binding.authorContainer.visibility = View.INVISIBLE
//            return
//        }
//        binding.authorContainer.visibility = View.VISIBLE
//        binding.authorName.text = author.name
//        Picasso.get().load(author.image).transform(CropCircleTransformation())
//            .into(binding.authorProfile)
//    }
}