package com.shubhamgupta16.justwallpapers.utils

import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.justwallpapers.R

/*
fun showShimmer(recyclerView: RecyclerView, shimmerTypeModel: T, shimmerViewCount: Int) {
    list.clear()
    for (i in 0..shimmerViewCount) {
        list.add(shimmerTypeModel)
    }
    recyclerView.animateInsert()
}

fun removeShimmer(recyclerView: RecyclerView, onRemoved: () -> Unit) {
    recyclerView.animateRemove {
        list.clear()
        onRemoved()
    }
}
*/

fun RecyclerView.animateInsert(onAnimComplete: (() -> Unit)? = null) {
    if (layoutAnimation == null)
        layoutAnimation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.anim_recycler_fall_down
        )
    scheduleLayoutAnimation()
}

fun RecyclerView.animateRemove(onAnimComplete: (() -> Unit)? = null) {
    layoutAnimation =
        AnimationUtils.loadLayoutAnimation(context, R.anim.anim_recycler_fall_down_rev)
    adapter?.notifyDataSetChanged()
    scheduleLayoutAnimation()
    Handler(Looper.getMainLooper()).postDelayed({

        layoutAnimation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.anim_recycler_fall_down
        )
        onAnimComplete?.let { it() }
        scheduleLayoutAnimation()
    }, 500)
}