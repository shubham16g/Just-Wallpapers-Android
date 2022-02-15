package com.shubhamgupta16.wallpaperapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.databinding.ActivityMainBinding
import com.shubhamgupta16.wallpaperapp.ui.main.fragments.HomeFragment


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swapFragment(HomeFragment(), false)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    swapFragment(HomeFragment())
                }
            }
            true
        }
    }

    private fun swapFragment(frag: Fragment, animate: Boolean = true) {
//        todo animation
        supportFragmentManager.beginTransaction().replace(binding.container.id, frag).commit()
    }


}