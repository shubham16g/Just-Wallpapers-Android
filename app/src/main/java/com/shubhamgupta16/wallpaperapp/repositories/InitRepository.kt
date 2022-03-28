package com.shubhamgupta16.wallpaperapp.repositories

import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.network.ApiResponse
import com.shubhamgupta16.wallpaperapp.network.ApiService
import javax.inject.Inject

class InitRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllColors(): List<ColorModel> {
        val response = ApiResponse.from { apiService.getColors() }
        return response.body ?: ArrayList()
    }
    suspend fun getAllCategories(): List<CategoryModel> {
        val response = ApiResponse.from { apiService.getCategories() }
        return response.body ?: ArrayList()
    }
}