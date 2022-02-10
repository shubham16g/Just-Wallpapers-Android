package com.shubhamgupta16.wallpaperapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhamgupta16.wallpaperapp.viewmodels.ListingViewModel


class SingletonNameViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(ListingViewModel::class.java) -> ListingViewModel.getInstance()
            else -> super.create(modelClass)
        }
    } as T

}