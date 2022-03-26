package com.shubhamgupta16.wallpaperapp.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.repositories.GlideRepository
import com.shubhamgupta16.wallpaperapp.repositories.WallRepository
import com.shubhamgupta16.wallpaperapp.utils.applyWall
import com.shubhamgupta16.wallpaperapp.utils.fadeVisibility
import com.shubhamgupta16.wallpaperapp.utils.playAndHide
import com.shubhamgupta16.wallpaperapp.utils.saveImageToExternal
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListCase
import com.shubhamgupta16.wallpaperapp.viewmodels.live_observer.ListObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PagerViewModel
@Inject constructor(
    private val wallRepository: WallRepository,
    private val glideRepository: GlideRepository
) :
    ViewModel() {
    var currentPosition = 0

    private val _listObserver = MutableLiveData<ListObserver>()
    val listObserver: LiveData<ListObserver> = _listObserver

    private val _list = ArrayList<WallModel>()
    val list: List<WallModel> = _list

    private var page = 1
    private var id = 0
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
        this.id = 0
    }

    fun init(id: Int) {
        this.id = id
    }

    fun isShowingSharedImage() = id != 0

    fun fetch() {
        if (page > lastPage) return
        if (_listObserver.value?.case == ListCase.INITIAL_LOADING) return
        _listObserver.value = ListObserver(ListCase.INITIAL_LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val response = if (id == 0)
                wallRepository.getWalls(page = page, s = query, category = category, color = color)
            else
                wallRepository.getWallsWithIds(listOf(id))
            if (response.isSuccessful && response.data != null) {
                lastPage = response.data!!.lastPage
                val size = _list.size
                _list.addAll(response.data!!.data)
                page++
                _listObserver.postValue(
                    ListObserver(ListCase.ADDED_RANGE, from = size, itemCount = _list.size)
                )
            } else
                _listObserver.postValue(ListObserver(ListCase.NO_CHANGE))
        }
    }

    private fun downloadWallpaper(wallId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            wallRepository.downloadWallpaper(wallId)
        }
    }

    /** *********** Glide Image System ******************* */

    private val _downloadBitmapLoading = MutableLiveData<Boolean?>(null)
    val downloadBitmapLoading get() :LiveData<Boolean?> = _downloadBitmapLoading

    private val _wallBitmapLoading = MutableLiveData<Boolean?>(null)
    val wallBitmapLoading get() :LiveData<Boolean?> = _wallBitmapLoading

    fun applyWallpaper(context: Context, wallModel: WallModel, flag: Int?) {
        _wallBitmapLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val bmp = glideRepository.downloadImage(wallModel.urls.full, wallModel.rotation ?: 0)
            if (bmp != null) {
                context.applyWall(bmp, bmp.width, bmp.height, flag)
                withContext(Dispatchers.Main) {
                    downloadWallpaper(wallModel.wallId)
                    _wallBitmapLoading.value = false
                    _wallBitmapLoading.value = null
                }
            } else {
                _wallBitmapLoading.postValue(null)
            }
        }
    }

    fun downloadWallpaper(context: Context, wallModel: WallModel) {
        _downloadBitmapLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val bmp = glideRepository.downloadImage(wallModel.urls.raw?: wallModel.urls.full, wallModel.rotation ?: 0)
            if (bmp != null) {
                context.saveImageToExternal(context.getString(R.string.app_name), "wallpaper_${wallModel.wallId}", bmp).also {
                    withContext(Dispatchers.Main){
                        if (!it) {
//                            _downloadBitmapLoading.value = null
                        } else {
                            downloadWallpaper(wallModel.wallId)
                            _downloadBitmapLoading.value = false
                        }
                        _downloadBitmapLoading.value = null
                    }
                }
            } else {
                _downloadBitmapLoading.postValue(null)
            }
        }
    }

    /************** favorite *****************/

    fun toggleFav(position: Int) {
        viewModelScope.launch {
            val model = _list[position]
            if (model.isFav) {
                wallRepository.removeFav(model.wallId)
                _list[position].isFav = false
            }
            else {
                wallRepository.applyFav(model.wallId)
                _list[position].isFav = true
            }
            _listObserver.postValue(ListObserver(ListCase.UPDATED, at = position))
        }
    }
}