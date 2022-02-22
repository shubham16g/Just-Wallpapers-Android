package com.shubhamgupta16.wallpaperapp.hilt

import com.shubhamgupta16.wallpaperapp.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesApiService(): ApiService {
        return ApiService.getInstance()
    }

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