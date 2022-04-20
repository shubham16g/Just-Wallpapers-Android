package com.shubhamgupta16.justwallpapers.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.shubhamgupta16.justwallpapers.R
import com.shubhamgupta16.justwallpapers.databinding.ActivityMainBinding
import com.shubhamgupta16.justwallpapers.ui.main.fragments.AccountFragment
import com.shubhamgupta16.justwallpapers.ui.main.fragments.CategoriesFragment
import com.shubhamgupta16.justwallpapers.ui.main.fragments.FavoritesFragment
import com.shubhamgupta16.justwallpapers.ui.main.fragments.HomeFragment
import com.shubhamgupta16.justwallpapers.utils.*
import com.shubhamgupta16.justwallpapers.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullStatusBar()
        setTransparentStatusBar()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*binding.bottomNav.layoutParams = (binding.bottomNav.layoutParams as ConstraintLayout.LayoutParams).apply {
            bottomMargin = getNavigationBarHeight()
        }*/

        Log.d(TAG, "onCreate: ${getCurrentFragName()}")

        applyFragment(viewModel.currentFrag)

        val navListener = NavigationBarView.OnItemSelectedListener {
            if (viewModel.currentFrag == it.itemId) return@OnItemSelectedListener false
            applyFragment(it.itemId)
        }

        binding.bottomNav.setOnItemSelectedListener(navListener)
    }

    private fun applyFragment(itemId: Int): Boolean {
        return when (itemId) {
            R.id.action_home -> swapFragment(itemId, getHomeFragment())
            R.id.action_category -> swapFragment(
                itemId,
                getCategoryFragment(viewModel.categoryName)
            )
            R.id.action_fav -> swapFragment(itemId, getFavoriteFragment())
            R.id.action_account -> swapFragment(itemId, getAccountFragment())
            else -> false
        }
    }

    private fun getFavoriteFragment() = FavoritesFragment()

    private fun getAccountFragment() = AccountFragment()

    private fun getHomeFragment() = HomeFragment().apply {
        categoryClickListener = { categoryName ->
            viewModel.categoryName = categoryName
            binding.bottomNav.selectedItemId = R.id.action_category
            viewModel.categoryName = null
        }
    }

    private fun getCategoryFragment(categoryName: String? = null) =
        CategoriesFragment().apply { this.categoryName = categoryName }

    private fun swapFragment(id: Int, frag: Fragment, animate: Boolean = true): Boolean {
        Log.d(TAG, "swapFragment: swapFragment()")
        viewModel.currentFrag = id
//        todo animation
        supportFragmentManager.beginTransaction().replace(binding.container.id, frag).commit()
        updateToolbarTitle()
        return true
    }

    private fun updateToolbarTitle() {
//        getCurrentFragName().also {
//            binding.toolbarTitle?.text = it
//        }
    }

    private fun getCurrentFragName(): String {
        return when (viewModel.currentFrag) {
            R.id.action_home -> "Home"
            R.id.action_category -> "Categories"
            R.id.action_fav -> "Favorites"
            else -> ""
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isUsingNightMode()) {
            lightNavigationBar()
        }
    }
    /*override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ${getCurrentFragName()}")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ${getCurrentFragName()}")
    }*/

    override fun onBackPressed() {
        if (viewModel.currentFrag != R.id.action_home)
            binding.bottomNav.selectedItemId = R.id.action_home
        else
            super.onBackPressed()
    }

}