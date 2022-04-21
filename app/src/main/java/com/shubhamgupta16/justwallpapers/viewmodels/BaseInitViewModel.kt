package com.shubhamgupta16.justwallpapers.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.justwallpapers.models.init.BaseModel
import com.shubhamgupta16.justwallpapers.repositories.InitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BaseInitViewModel @Inject constructor(private val initRepository: InitRepository) :
    ViewModel() {
    private val _liveIsLoading = MutableLiveData<Boolean?>()
    val liveIsLoading: LiveData<Boolean?> = _liveIsLoading

    private var _baseModel :BaseModel?=null
    val baseModel:BaseModel? get() = _baseModel

    fun fetch() {
        if (baseModel != null) return
        _liveIsLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val baseModel = initRepository.getBase()
            Log.d(TAG, "fetch: $baseModel")
            _baseModel = baseModel
            withContext(Dispatchers.Main) {
                _liveIsLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "FeaturedViewModel"
    }
}