package com.shubhamgupta16.wallpaperapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.wallpaperapp.databinding.ActivityListingBinding
import com.shubhamgupta16.wallpaperapp.ui.components.VerticalWallpapersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListingBinding
//    private var initialTitle:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingBinding.inflate(layoutInflater)
//        fitFullScreen()
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        val initialTitle = intent.getStringExtra("title")
        binding.toolbarTitle.text = initialTitle


        val query = intent.getStringExtra("query")
        val category = intent.getStringExtra("category")
        val color = intent.getStringExtra("color")
        val colorValue = intent.getIntExtra("color_value", 0)
        if (colorValue != 0)
            binding.toolbarColorCard.apply {
                visibility = View.VISIBLE
                setCardBackgroundColor(colorValue)
            }
        showListingFragment(query, category, color)
    }

    private fun showListingFragment(
        query: String? = null,
        category: String? = null,
        color: String? = null
    ) {
        supportFragmentManager.beginTransaction()
            .replace(
                binding.container.id,
                VerticalWallpapersFragment.getInstance(query, category, color)
            )
            .commit()
    }

    companion object {
        fun open(
            context: Context,
            title: String,
            query: String? = null,
            category: String? = null,
            color: String? = null,
            colorValue: Int? = null
        ) {
            context.startActivity(Intent(context, ListingActivity::class.java).apply {
                putExtra("title", title)
                putExtra("query", query)
                putExtra("category", category)
                putExtra("color", color)
                putExtra("color_value", colorValue)
            })
        }
    }

}