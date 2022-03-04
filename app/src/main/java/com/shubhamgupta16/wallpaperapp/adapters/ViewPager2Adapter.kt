package com.shubhamgupta16.wallpaperapp.adapters

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentViewHolder
import com.shubhamgupta16.wallpaperapp.ui.components.VerticalWallpapersFragment

class ViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mFragmentList: ArrayList<String> = ArrayList()

    fun clear(){
        mFragmentList.clear()
    }
    fun addFragment(categoryName: String) {
        mFragmentList.add(categoryName)
    }

    override fun createFragment(position: Int): Fragment {
        return VerticalWallpapersFragment.getInstance(category = mFragmentList[position])
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        Log.d("Adapter", "onBindViewHolder: $position")
        Log.d("Adapter", "onBindViewHolder payloads: $payloads")
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }
}