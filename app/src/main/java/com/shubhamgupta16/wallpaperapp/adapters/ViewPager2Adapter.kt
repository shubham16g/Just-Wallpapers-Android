package com.shubhamgupta16.wallpaperapp.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.Fragment
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

    override fun getItemCount(): Int {
        return mFragmentList.size
    }
}