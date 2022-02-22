package com.shubhamgupta16.wallpaperapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shubhamgupta16.wallpaperapp.main
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.network.ListObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class ColorsViewModel: ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<ColorModel>()
    val list: List<ColorModel> = _list

    fun fetch(application: Application) {
        if (_list.isEmpty())
            _list.addAll(application.main.initData.colors)
        _listObserver.value = ListObserver(ListCase.ADDED_RANGE, from = 0, itemCount = _list.size)
    }

    companion object {
        private const val TAG = "ColorViewModel"
    }
}