package com.shubhamgupta16.wallpaperapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.wallpapers.wall.WallModel
import com.shubhamgupta16.wallpaperapp.repositories.WallRepository
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticWallpapersViewModel
@Inject constructor(private val wallRepository: WallRepository) : ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<WallModel>()
    val list: List<WallModel> = _list

    private var _lastPage: Int = 1
    private var _query: String? = null
    private var _category: String? = null
    private var _color: String? = null
    private var _orderBy: String? = null

    val query get() = _query
    val lastPage get() = _lastPage
    val category get() = _category
    val color get() = _color
    val orderBy get() = _orderBy

    fun fetch() {
        if (_list.isNotEmpty() || _listObserver.value?.case == ListCase.INITIAL_LOADING) return
        _listObserver.value = ListObserver(ListCase.INITIAL_LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetch: IO")
            val response =
                wallRepository.getWalls(
                    page = 1,
                    s = _query,
                    category = _category,
                    color = _color,
                    orderBy = _orderBy
                )

            if (response.isSuccessful && response.body != null) {
                _lastPage = response.body!!.lastPage
                val size = _list.size
                _list.addAll(response.body!!.data)
                if (_list.isNotEmpty()) {
                    _listObserver.postValue(
                        ListObserver(
                            ListCase.ADDED_RANGE,
                            from = size,
                            itemCount = _list.size
                        )
                    )
                } else {
                    _listObserver.postValue(ListObserver(ListCase.EMPTY))
                }
            } else
                _listObserver.postValue(ListObserver(ListCase.NO_CHANGE))
        }
    }

    fun filterFavorites() {
        if (_listObserver.value == null || _listObserver.value?.case == ListCase.INITIAL_LOADING) return
        viewModelScope.launch {
            wallRepository.filterFavorites(_list)
        }

    }

    fun init(
        query: String? = null,
        category: String? = null,
        color: String? = null,
        orderBy: String? = null
    ) {
        this._query = query
        this._category = category
        this._color = color
        this._orderBy = orderBy
    }

    companion object {
        private const val TAG = "ListingViewModel"
    }
}