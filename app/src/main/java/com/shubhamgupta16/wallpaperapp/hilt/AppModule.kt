package com.shubhamgupta16.wallpaperapp.hilt

import android.content.Context
import com.shubhamgupta16.wallpaperapp.network.ApiService
import com.shubhamgupta16.wallpaperapp.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesApiService() = ApiService.getInstance()

    @Provides
    fun getAppDatabase(@ApplicationContext context: Context) = AppDatabase.build(context)

    @Provides
    fun getInitDao(db: AppDatabase) = db.initDao()

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