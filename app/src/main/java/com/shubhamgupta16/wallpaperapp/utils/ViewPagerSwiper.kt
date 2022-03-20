package com.shubhamgupta16.wallpaperapp.utils

import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.roundToInt


fun ViewPager2.apply3DSwiper(){
    this.apply {
        clipToPadding = false   // allow full width shown with padding
        clipChildren = false    // allow left/right item is not clipped
        offscreenPageLimit = 2  // make sure left/right item is rendered
    }


    doOnLayout {
        val currentItemHorizontalMarginPx = ((measuredWidth-506)/1.794f).roundToInt()
        val nextItemVisiblePx = (measuredWidth-506)/1.794f + 18.px
        val abs = 0.25f
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (abs * abs(position))
            page.scaleX = 1 - (abs * abs(position))
        }
        setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            currentItemHorizontalMarginPx
        )
        addItemDecoration(itemDecoration)
    }

}

class HorizontalMarginItemDecoration(horizontalMarginInDp: Int) :
    RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int = horizontalMarginInDp

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }
}