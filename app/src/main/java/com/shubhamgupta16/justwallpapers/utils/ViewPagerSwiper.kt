package com.shubhamgupta16.justwallpapers.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.shubhamgupta16.justwallpapers.R
import kotlin.math.abs


fun ViewPager2.apply3DSwiper(){
    val currentItemHorizontalMarginPx = resources.getDimensionPixelSize(R.dimen.view_pager_item_margin)
    val nextItemVisiblePx = resources.getDimension(R.dimen.view_pager_next_visible_item)
    val abs = 0.25f
    applyCarousels(currentItemHorizontalMarginPx, nextItemVisiblePx, abs)
}

fun ViewPager2.applyCarousels(currentItemHorizontalMarginPx:Int, nextItemVisiblePx:Float, abs:Float){
    this.apply {
        clipToPadding = false   // allow full width shown with padding
        clipChildren = false    // allow left/right item is not clipped
        offscreenPageLimit = 2  // make sure left/right item is rendered
    }

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