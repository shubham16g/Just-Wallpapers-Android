package com.shubhamgupta16.wallpaperapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.network.ListCase
import com.shubhamgupta16.wallpaperapp.network.ListObserver
import com.shubhamgupta16.wallpaperapp.repositories.InitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ColorsViewModel @Inject constructor(private val initRepository: InitRepository): ViewModel() {
    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<ColorModel>()
    val list: List<ColorModel> = _list

    fun fetch() {
        if (_list.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            _list.addAll(initRepository.getAllColors())
            withContext(Dispatchers.Main) {
                _listObserver.value =
                    ListObserver(ListCase.ADDED_RANGE, from = 0, itemCount = _list.size)
            }
        }
    }

    companion object {
        private const val TAG = "ColorViewModel"
    }
}