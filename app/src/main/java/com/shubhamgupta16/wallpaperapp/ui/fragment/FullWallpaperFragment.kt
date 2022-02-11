package com.shubhamgupta16.wallpaperapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shubhamgupta16.wallpaperapp.adapters.SingleImageAdapter
import com.shubhamgupta16.wallpaperapp.databinding.ActivityFullWallpaperBinding
import com.shubhamgupta16.wallpaperapp.utils.*
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel

class FullWallpaperFragment : Fragment() {
    companion object {
        private const val POSITION = "pos"
        fun getInstance(position: Int): FullWallpaperFragment {
            return FullWallpaperFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
        }
    }

    private lateinit var binding: ActivityFullWallpaperBinding
    private lateinit var viewModel: ListingViewModel

    private var adapter: SingleImageAdapter? = null
    private var screenMeasure: ScreenMeasure? = null

    private var position = 0

    private fun initScreenMeasure() {
        if (screenMeasure == null) {
            screenMeasure = ScreenMeasure(requireActivity())
            binding.viewPager2.pivotY = screenMeasure!!.pivot()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProvider(it)[ListingViewModel::class.java]
        }
        position = arguments?.getInt(POSITION) ?: 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().setTransparentStatusBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().setNormalStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityFullWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationHeight = requireActivity().getNavigationBarHeight()

        binding.navigationOverlay.setPadding(0, 0, 0, navigationHeight)

        setupViewPager()
        if (viewModel.list.isNotEmpty())
            binding.viewPager2.setCurrentItem(position, false)
//        val h = Handler(Looper.getMainLooper())

    }

    private fun setupViewPager() {
        val h = Handler(Looper.getMainLooper())
        val r = Runnable {
            if (!isZoom) animateZoom() else animateZoomOut()
        }
        adapter = SingleImageAdapter(requireContext(), viewModel.list) {
            if (!isZoom) requireActivity().hideSystemUI() else requireActivity().showSystemUI()
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
}