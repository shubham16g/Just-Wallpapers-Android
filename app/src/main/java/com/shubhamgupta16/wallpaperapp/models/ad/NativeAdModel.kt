package com.shubhamgupta16.wallpaperapp.models.ad

import com.google.android.gms.ads.nativead.NativeAd
import java.io.Serializable

data class NativeAdModel(var nativeAd: NativeAd? = null): BaseAdModel(), Serializable