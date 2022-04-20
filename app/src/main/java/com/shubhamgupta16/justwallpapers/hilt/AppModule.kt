package com.shubhamgupta16.justwallpapers.hilt

import android.content.Context
import com.shubhamgupta16.justwallpapers.network.ApiService
import com.shubhamgupta16.justwallpapers.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesApiService(@ApplicationContext context: Context) = ApiService.build(context)

    @Singleton
    @Provides
    fun getAppDatabase(@ApplicationContext context: Context) = AppDatabase.build(context)

    @Singleton
    @Provides
    fun getFavDao(db: AppDatabase) = db.favDao()
}