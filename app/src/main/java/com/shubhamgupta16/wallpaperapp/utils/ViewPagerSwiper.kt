package com.shubhamgupta16.wallpaperapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


fun ViewPager2.apply3DSwiper(){
    this.apply {
        clipToPadding = false   // allow full width shown with padding
        clipChildren = false    // allow left/right item is not clipped
        offscreenPageLimit = 2  // make sure left/right item is rendered
    }

    val nextItemVisiblePx = 88.px
    val currentItemHorizontalMarginPx = 70.px
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
    val pageTransformer = ViewPager2.PageTransformer { page, position ->
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
        page.scaleX = 1 - (0.25f * abs(position))
    }
    setPageTransformer(pageTransformer)

    val itemDecoration = HorizontalMarginItemDecoration(
        currentItemHorizontalMarginPx
    )
    addItemDecoration(itemDecoration)
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