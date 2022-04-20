package com.shubhamgupta16.justwallpapers.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallModel
import com.shubhamgupta16.justwallpapers.repositories.InitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(private val initRepository: InitRepository) :
    ViewModel() {
    private val _liveIsLoading = MutableLiveData<Boolean>()
    val liveIsLoading: LiveData<Boolean> = _liveIsLoading

    private var _title: String? = null
    val title get() = _title

    private var _subTitle: String? = null
    val subTitle get() = _subTitle

    private var _wallModel: WallModel? = null
    val wallModel get() = _wallModel

    fun fetch() {
        if (wallModel != null) return
        _liveIsLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val featured = initRepository.getFeatured()
            Log.d(TAG, "fetch: $featured")
            _wallModel = featured?.data
            _title = featured?.title
            _subTitle = featured?.subTitle
            withContext(Dispatchers.Main) {
                _liveIsLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "FeaturedViewModel"
    }
}