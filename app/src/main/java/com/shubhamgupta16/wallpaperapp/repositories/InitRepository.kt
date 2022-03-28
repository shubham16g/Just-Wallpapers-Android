package com.shubhamgupta16.wallpaperapp.repositories

import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.network.ApiResponse
import com.shubhamgupta16.wallpaperapp.network.ApiService
import javax.inject.Inject

class InitRepository @Inject constructor(private val apiService: ApiService) {

    /*suspend fun fetchInit(): Response<InitModel> {
        *//*val response = apiService.getInitData()
        if (response.isSuccessful && response.body() != null) {
            categoryDao.deleteAllCategories()
            colorDao.deleteAllColors()
            response.body()?.categories?.forEach {
                categoryDao.insertCategory(it)
            }
            response.body()?.colors?.forEach {
                colorDao.insertColor(it)
            }
        }
        return response*//*
    }*/

    suspend fun getAllColors(): List<ColorModel> {
        val response = ApiResponse.from { apiService.getColors() }
        return response.data ?: ArrayList()
    }
    suspend fun getAllCategories(): List<CategoryModel> {
        val response = ApiResponse.from { apiService.getCategories() }
        return response.data ?: ArrayList()
    }
}