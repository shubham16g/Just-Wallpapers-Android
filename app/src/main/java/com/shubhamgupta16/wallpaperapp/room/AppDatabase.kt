package com.shubhamgupta16.wallpaperapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel
import com.shubhamgupta16.wallpaperapp.models.roommodels.FavWallModel

@Database(entities = [CategoryModel::class, ColorModel::class, FavWallModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun initDao(): InitDao
    abstract fun favDao(): FavWallDao

    companion object {
        fun build(context: Context)=
             Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name1"
        ).build()
    }
}