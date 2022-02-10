package com.shubhamgupta16.wallpaperapp.utils

import android.app.Activity
import android.util.Log
import com.shubhamgupta16.wallpaperapp.R

class ScreenMeasure(activity: Activity) {

    companion object {
        private const val TAG = "ScreenMeasure"
    }

    private var screenHeight = 0
    private var top = 0f
    private var bottom = 0f
    private var scale = 0f
    private var measuredHeight = 0f
    private var pivot = 0f
    private val actionCardHeight: Float
    private val navigationHeight: Int = activity.getNavigationBarHeight()

    fun actionCollapsedY() = actionCardHeight + navigationHeight - 15.dp
    fun scale() = scale
    fun pivot() = pivot

    init {
        screenHeight = activity.window.decorView.measuredHeight
        actionCardHeight = activity.resources.getDimension(R.dimen.action_card_height)
        bottom = activity.resources.getDimension(R.dimen.pager_bottom)
        top = activity.resources.getDimension(R.dimen.pager_top)
        measuredHeight = (screenHeight - top - bottom)
        scale = screenHeight / measuredHeight
        pivot = top * (measuredHeight / (top + bottom))

        Log.d(TAG, "screenHeight -> $screenHeight")
        Log.d(TAG, "top -> $top")
        Log.d(TAG, "bottom -> $bottom")
        Log.d(TAG, "scale -> $scale")
        Log.d(TAG, "measuredHeight -> $measuredHeight")
        Log.d(TAG, "pivot -> $pivot")
    }

}