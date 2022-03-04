package com.shubhamgupta16.wallpaperapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shubhamgupta16.wallpaperapp.ui.components.VerticalWallpapersFragment

class ViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mCategoryList: ArrayList<String> = ArrayList()

    fun clear(){
        mCategoryList.clear()
    }
    fun addFragment(categoryName: String) {
        mCategoryList.add(categoryName)
    }

    override fun createFragment(position: Int): Fragment {
        return VerticalWallpapersFragment.getInstance(category = mCategoryList[position])
    }

    override fun getItemCount(): Int {
        return mCategoryList.size
    }
}