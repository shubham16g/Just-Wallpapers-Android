package com.shubhamgupta16.wallpaperapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.databinding.FragmentMainHomeBinding
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModelListHolder
import com.shubhamgupta16.wallpaperapp.ui.FullWallpaperActivity
import com.shubhamgupta16.wallpaperapp.ui.ListingActivity
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalCategoriesFragment
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalColorsFragment
import com.shubhamgupta16.wallpaperapp.ui.components.HorizontalWallpapersFragment
import com.shubhamgupta16.wallpaperapp.utils.*
import com.shubhamgupta16.wallpaperapp.viewmodels.FeaturedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentMainHomeBinding
    private val viewModel: FeaturedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    var categoryClickListener: ((categoryName: String) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTransparentStatusBar()


//        childFragmentManager.beginTransaction()
//            .replace(binding.featuredFragment.id, FeaturedFragment()).commit()

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            Log.d(
                "TAG",
                "onViewCreated: ${abs(verticalOffset) - appBarLayout.totalScrollRange == 0}"
            )
            if (requireContext().isUsingNightMode()) return@OnOffsetChangedListener
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                requireActivity().lightStatusBar()
            } else {
                requireActivity().nonLightStatusBar()
            }
        })

        viewModel.liveIsLoading.observe(viewLifecycleOwner) {
            it?.let { it ->
                when (it) {
                    false -> {
                        viewModel.wallModel?.let { wallModel ->
                            Glide.with(requireContext())
                                .load(wallModel.urls.regular ?: wallModel.urls.small)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.headerImage)


                            binding.openHeaderImage.setOnClickListener {
                                FullWallpaperActivity.getLaunchingIntent(requireContext(), WallModelListHolder(
                                    listOf(wallModel)),0,1,1).let { intent ->
                                    startActivity(intent)
                                }
                            }
                        }
                        binding.headerTitle.text = viewModel.title
                    }
                    else -> {}
                }
            }
        }
        fetchFeatured()



        binding.searchBar.setSearchViewFont(R.font.product_sans_regular)
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                ListingActivity.open(
                    requireContext(),
                    "Showing results for '$query'",
                    query = query
                )
                return true
            }

            override fun onQueryTextChange(newText: String?) = true

        })

        binding.newlyAddedHeader.setOnMoreClickListener {
            ListingActivity.open(requireContext(), "Newly Added")
        }
        binding.popularHeader.setOnMoreClickListener {
            ListingActivity.open(requireContext(), "Popular", orderBy = "downloads")
        }


        childFragmentManager.beginTransaction()
            .replace(binding.colorsFragment.id, HorizontalColorsFragment().apply {
                colorClickListener = { colorName, colorValue ->
                    ListingActivity.open(
                        requireContext(),
                        colorName, color = colorName, colorValue = colorValue
                    )
                }
            }).commit()
        (childFragmentManager.findFragmentById(binding.categoriesFragment.id) as HorizontalCategoriesFragment).apply {
            categoryClickListener = { categoryName ->
                this@HomeFragment.categoryClickListener?.let { it(categoryName) }
            }
            fetch()
        }

        (childFragmentManager.findFragmentById(binding.latestWallpaperFragment.id) as HorizontalWallpapersFragment).fetch()
        (childFragmentManager.findFragmentById(binding.popularWallpaperFragment.id) as HorizontalWallpapersFragment).fetch(
            orderBy = "downloads"
        )
    }

    private fun fetchFeatured() {
        viewModel.fetch()
    }
}