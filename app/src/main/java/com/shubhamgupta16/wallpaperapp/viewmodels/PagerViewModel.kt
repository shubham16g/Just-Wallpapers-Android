package com.shubhamgupta16.wallpaperapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.network.ListObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagerViewModel
@Inject constructor(private val apiService: ApiService) : ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<WallModel>()
    val list: List<WallModel> = _list

    private var page = 1
    private var lastPage = 1
    private var query: String? = null
    private var category: String? = null
    private var color: String? = null

    fun init(
        list: List<WallModel>,
        page: Int,
        lastPage: Int,
        query: String? = null,
        color: String? = null,
        category: String? = null
    ) {
        if (_list.isNotEmpty()) return
        _list.addAll(list)
        this.page = page
        this.lastPage = lastPage
        this.query = query
        this.color = color
        this.category = category
    }

    fun fetch() {
        if (page > lastPage) return
        if (_listObserver.value?.case == ListCase.LOADING) return
        _listObserver.value = ListObserver(ListCase.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                apiService.getWalls(page = page, s = query, category = category, color = color)
            if (response.isSuccessful) {
                response.body()?.let {
                    lastPage = it.lastPage
                    val size = _list.size
                    _list.addAll(it.data)
                    page++
                    _listObserver.postValue(
                        ListObserver(ListCase.ADDED, from = size, itemCount = _list.size)
                    )
                }
            } else
                _listObserver.postValue(ListObserver(ListCase.NO_CHANGE))
        }
    }
}