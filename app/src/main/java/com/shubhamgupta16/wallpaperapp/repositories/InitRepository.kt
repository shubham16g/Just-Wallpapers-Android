package com.shubhamgupta16.wallpaperapp.repositories

import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.models.init.InitModel
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.room.CategoryDao
import com.shubhamgupta16.wallpaperapp.room.ColorDao
import retrofit2.Response
import javax.inject.Inject

class InitRepository @Inject constructor(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val colorDao: ColorDao
) {

    suspend fun fetchInit(): Response<InitModel> {
        val response = apiService.getInitData()
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
        return response
    }

    suspend fun getAllColors(): List<ColorModel> {
        return colorDao.getAllColors()
    }
    suspend fun getAllCategories(): List<CategoryModel> {
        return categoryDao.getAllCategories()
    }
}