package com.shubhamgupta16.wallpaperapp.hilt

import android.content.Context
import com.shubhamgupta16.wallpaperapp.network.ApiServiceBuilder
import com.shubhamgupta16.wallpaperapp.network.InitService
import com.shubhamgupta16.wallpaperapp.network.WallService
import com.shubhamgupta16.wallpaperapp.room.AppDatabase
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
    fun providesWallService(@ApplicationContext context: Context) = ApiServiceBuilder.build(context, "wall-cache", 10 * 1024 * 1024, WallService::class.java)


    @Singleton
    @Provides
    fun providesInitService(@ApplicationContext context: Context) = ApiServiceBuilder.build(context, "init-cache", 10 * 1024 * 1024, InitService::class.java)

    @Singleton
    @Provides
    fun getAppDatabase(@ApplicationContext context: Context) = AppDatabase.build(context)

    @Singleton
    @Provides
    fun getFavDao(db: AppDatabase) = db.favDao()

//    @Provides
//    fun providesWoocommerce(): Woocommerce {
//        return Woocommerce.Builder()
//            .setSiteUrl(Config.siteUrl)
//            .setApiVersion(Woocommerce.API_V3)
//            .setConsumerKey(Config.consumerKey)
//            .setConsumerSecret(Config.consumerSecret)
//            .build()
//    }
}