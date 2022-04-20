package com.shubhamgupta16.justwallpapers.viewmodels

import androidx.lifecycle.ViewModel
import com.shubhamgupta16.justwallpapers.R
import com.shubhamgupta16.justwallpapers.models.init.BaseModel


class MainViewModel : ViewModel() {

    var currentFrag = R.id.action_home
    var categoryName: String? = null

    private var _baseModel :BaseModel? = null
    fun initBaseModel(baseModel: BaseModel) {
        _baseModel = baseModel
    }

    fun getBaseModel() :BaseModel? {
        return _baseModel
    }

}