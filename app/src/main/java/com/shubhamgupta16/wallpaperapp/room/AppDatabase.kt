package com.shubhamgupta16.wallpaperapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.models.roommodels.FavWallModel

@Database(entities = [CategoryModel::class, ColorModel::class, FavWallModel::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun colorDao(): ColorDao
    abstract fun favDao(): FavWallDao

    companion object {
        fun build(context: Context)=
             Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name3"
        ).build()
    }
}