package com.shubhamgupta16.wallpaperapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.network.ListObserver
import com.shubhamgupta16.wallpaperapp.room.InitDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val initDao: InitDao) : ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<CategoryModel>()
    val list: List<CategoryModel> = _list

    fun fetch() {
        if (_list.isNotEmpty()) return
        viewModelScope.launch {
            _list.addAll(initDao.getAllCategories())
            withContext(Dispatchers.Main) {
                _listObserver.value =
                    ListObserver(ListCase.ADDED_RANGE, from = 0, itemCount = _list.size)
            }
        }
    }

    companion object {
        private const val TAG = "CategoryViewModel"
    }
}