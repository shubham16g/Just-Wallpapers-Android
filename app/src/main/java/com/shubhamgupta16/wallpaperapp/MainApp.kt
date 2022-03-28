package com.shubhamgupta16.wallpaperapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : Application() /*{

    private var initModel:InitModel?=null
    val initData:InitModel get() = initModel ?: InitModel(ArrayList(), ArrayList())

    val isInitialized get() = initModel != null

    fun initialize(initModel: InitModel){
        this.initModel = initModel
    }
}

val Application.initData: InitModel
    get() = (this as MainApp).initData

val Application.main: MainApp
    get() = this as MainApp*/

/** TODOS
 *
 * implement load colors, categories, etc in splash screen ad store it as hilt
 * remove listingFragment and merge code in listing activity
 * */