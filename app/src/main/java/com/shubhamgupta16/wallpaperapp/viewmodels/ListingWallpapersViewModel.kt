package com.shubhamgupta16.wallpaperapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.repositories.WallRepository
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListingWallpapersViewModel
@Inject constructor(private val wallRepository: WallRepository) : ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<WallModel?>()
    val list: List<WallModel?> = _list

    private var _page = 1
    private var _lastPage = 1
    private var _query: String? = null
    private var _category: String? = null
    private var _color: String? = null
    private var _orderBy: String? = null
    private var isFavList: Boolean = false

    val page get() = _page
    val lastPage get() = _lastPage
    val query get() = _query
    val category get() = _category
    val color get() = _color
    val orderBy get() = _orderBy

    fun fetch() {
        if (_page > _lastPage) return
        Log.d(TAG, "fetch: $_lastPage, $_page")
        if (_page == 1 && _list.isNotEmpty()) {
            val size = _list.size
            _list.clear()
            _listObserver.value = ListObserver(ListCase.REMOVED_RANGE, 0, size)
        } else if (page == 1 && _list.isEmpty()) {
            _listObserver.value = ListObserver(ListCase.INITIAL_LOADING)
        }
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetch: IO")
            val response = if (isFavList)
                wallRepository.getFavoriteWallpapers(page = _page)
            else
                wallRepository.getWalls(
                    page = _page,
                    s = _query,
                    category = _category,
                    color = _color,
                    orderBy = _orderBy
                )

            if (response.isSuccessful && response.body != null) {
                Log.d(TAG, "fetch: $category success")
                _lastPage = response.body!!.lastPage
                val size = _list.size
                if (_list.isNotEmpty())
                    _list.removeAt(_list.lastIndex)
                _list.addAll(response.body!!.data)
                if (_lastPage > _page)
                    _list.add(null)
                _page++
                if (_list.isNotEmpty()) {
                    _listObserver.postValue(ListObserver(ListCase.UPDATED, at = size - 1))
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
        if (isFavList) {
            viewModelScope.launch {
                if (_list.isNotEmpty()) {
                    wallRepository.filterFavorites(_list)
                    withContext(Dispatchers.Main) {
                        _list.reversed().forEachIndexed { index, wallModel ->
                            if (wallModel?.isFav == false) {
                                _list.removeAt(index)
                                _listObserver.value = ListObserver(ListCase.REMOVED, at = index)
                            }
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                wallRepository.filterFavorites(_list)
            }
        }
    }

    fun init(query: String? = null, category: String? = null, color: String? = null, orderBy:String? = null) {
        _page = 1
        this._query = query
        this._category = category
        this._color = color
        this._orderBy = orderBy
        this.isFavList = false
    }

    fun initForFavList() {
        _page = 1
        this._query = null
        this._category = null
        this._color = null
        this._orderBy = null
        this.isFavList = true
    }



    companion object {
        private const val TAG = "ListingViewModel"
    }
}