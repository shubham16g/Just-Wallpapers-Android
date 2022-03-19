package com.shubhamgupta16.wallpaperapp.viewmodels

import androidx.lifecycle.ViewModel
import com.shubhamgupta16.wallpaperapp.R


class MainViewModel : ViewModel() {

    var currentFrag = R.id.action_home
    var categoryName: String? = null
}