package com.shubhamgupta16.wallpaperapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.app.WallModelLite
import com.shubhamgupta16.wallpaperapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListingViewModel : ViewModel() {
    private val apiService = ApiService.getInstance()

    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<WallModelLite?>()
    val list: List<WallModelLite?> = _list

    private var page = 1
    private var lastPage = 1
    private var query: String? = null
    private var category: String? = null
    private var color: String? = null

    fun fetch() {
        if (page > lastPage) return
        Log.d(TAG, "fetch: $lastPage, $page")
        if (page == 1) {
            val size = _list.size
            _list.clear()
            _listObserver.value = ListObserver(ListCase.REMOVED, 0, size)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                apiService.getWalls(page = page, s = query, category = category, color = color)

            if (response.isSuccessful) {
                response.body()?.let {
                    lastPage = it.lastPage
                    val size = _list.size
                    if (_list.isNotEmpty())
                        _list.removeAt(_list.lastIndex)
                    _list.addAll(it.data)
                    if (lastPage > page)
                        _list.add(null)
                    page++
                    if (_list.isNotEmpty())
                        _listObserver.postValue(ListObserver(ListCase.UPDATED, at = size - 1))
                    _listObserver.postValue(ListObserver(ListCase.ADDED, from = size, itemCount = _list.size))
                }
            } else
                _listObserver.postValue(ListObserver(ListCase.NO_CHANGE))
        }
    }

    fun setQuery(query: String?) {
        page = 1
        this.query = query
    }

    fun setCategory(category: String?) {
        page = 1
        this.category = category
    }

    fun setColor(color: String?) {
        page = 1
        this.color = color
    }

    companion object {
        private const val TAG = "ListingViewModel"

        private var instance : ListingViewModel? = null
        fun getInstance() =
            instance ?: synchronized(ListingViewModel::class.java){
                instance ?: ListingViewModel().also { instance = it }
            }
    }
}

data class ListObserver(
    val case: ListCase,
    val from: Int = -1,
    val itemCount: Int = -1,
    val at: Int = -1
)

enum class ListCase {
    REMOVED,
    UPDATED,
    ADDED,
    NO_CHANGE
}