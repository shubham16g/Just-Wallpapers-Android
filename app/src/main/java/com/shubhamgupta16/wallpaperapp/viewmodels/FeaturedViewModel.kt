package com.shubhamgupta16.wallpaperapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListObserver
import com.shubhamgupta16.wallpaperapp.repositories.InitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(private val initRepository: InitRepository) :
    ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<WallModel>()
    val list: List<WallModel> = _list

    private val _titles = ArrayList<String>()
    val titles: List<String> = _titles

    fun fetch() {
        if (_list.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            val featured = initRepository.getFeatured()
            Log.d(TAG, "fetch: $featured")
            _list.addAll(featured.data)
            _titles.addAll(featured.titles)
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