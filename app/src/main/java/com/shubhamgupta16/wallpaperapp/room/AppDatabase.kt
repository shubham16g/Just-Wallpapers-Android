package com.shubhamgupta16.wallpaperapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel

@Database(entities = [CategoryModel::class, ColorModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun initDao(): InitDao

    companion object {
        val Context.db: AppDatabase
            get() = Room.databaseBuilder(
                this,
                AppDatabase::class.java, "database-name"
            ).build()
    }
}