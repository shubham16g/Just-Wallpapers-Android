package com.shubhamgupta16.wallpaperapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.databinding.ActivityMainBinding
import com.shubhamgupta16.wallpaperapp.ui.main.fragments.CategoriesFragment
import com.shubhamgupta16.wallpaperapp.ui.main.fragments.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private var currentFrag = R.id.action_home


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swapFragment(R.id.action_home, HomeFragment(), false)

        binding.bottomNav.setOnItemSelectedListener {
            if (currentFrag == it.itemId) return@setOnItemSelectedListener false
            when (it.itemId) {
                R.id.action_home -> swapFragment(it.itemId, HomeFragment())
                R.id.action_category -> swapFragment(it.itemId, CategoriesFragment())
                else -> false
            }
        }
    }

    private fun swapFragment(id:Int,frag: Fragment, animate: Boolean = true): Boolean {
        currentFrag = id
//        todo animation
        supportFragmentManager.beginTransaction().replace(binding.container.id, frag).commit()
        return true
    }


}