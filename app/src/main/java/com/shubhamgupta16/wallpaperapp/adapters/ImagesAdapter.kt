package com.shubhamgupta16.wallpaperapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.ad.BaseAdModel
import com.shubhamgupta16.wallpaperapp.models.ad.NativeAdModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.roundToInt


class ImagesAdapter(
    private val context: Context,
    private val list: List<BaseAdModel?>,
    private val listener: (wallModel: WallModel, i: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cardRadius = context.resources.getDimension(R.dimen.card_corner_radius)

    private fun goFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams =
            holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is ItemViewHolder) {

            holder.imageView.requestLayout()
            when (model) {
                is WallModel -> {

                    holder.imageView.let {
                        val mlt = MultiTransformation(
                            RotationTransform(model.rotation?.toFloat() ?: 0f),
                            CenterCrop(),
                            RoundedCornersTransformation(
                                cardRadius.roundToInt(),
                                0
                            )
                        )
                        Glide.with(it.context).load(model.urls.small)
                            .thumbnail()
                            .placeholder(GradientDrawable().apply {
                                setColor(Color.parseColor(model.color))
                                cornerRadius = cardRadius
                            })
                            .transform(mlt)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(it)
                    }

                    holder.itemView.setOnClickListener {
                        listener(model, position)
                    }
                }
            }
        } else if (holder is UnifiedNativeAdViewHolder) {
            goFullSpan(holder)
            if (model is NativeAdModel && model.nativeAd != null) {
                val unifiedNativeAdView = holder.adView
                unifiedNativeAdView.visibility = View.VISIBLE
                populateNativeAdView(model.nativeAd!!, unifiedNativeAdView)
            } else {
                loadNativeAd(position)
            }

        } else {
            goFullSpan(holder)
        }
    }

    private fun loadNativeAd(position: Int) {
        if (list[position] is NativeAdModel) {
            val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd { nativeAd ->
                    (list[position] as NativeAdModel).nativeAd = nativeAd
                    notifyItemChanged(position)
//                val unifiedNativeAdView = holder.adView
//                unifiedNativeAdView!!.visibility = View.VISIBLE
//                mapUnifiedNativeAdToLayout(nativeAd, unifiedNativeAdView)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        //                            holder.adView.setVisibility(View.GONE);
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder() // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build()
                )
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            null -> 0
            is WallModel -> 1
            else -> 2
        }
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> LoaderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
        )
        1 -> ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wall, parent, false)
        )
        else -> UnifiedNativeAdViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wall_ad, parent, false)
        )
    }


    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as? TextView)?.text = nativeAd.headline
        (adView.bodyView as? TextView)?.text = nativeAd.body
        (adView.callToActionView as? Button)?.text = nativeAd.callToAction

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        val icon: NativeAd.Image? = nativeAd.icon
        if (icon == null) {
            adView.iconView.visibility = View.INVISIBLE
        } else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd)
    }
/*

    private fun mapUnifiedNativeAdToLayout(adFromGoogle: NativeAd, myAdView: NativeAdView) {
        val mediaView: MediaView = myAdView.findViewById(R.id.ad_media)
        myAdView.mediaView = mediaView
        myAdView.headlineView = myAdView.findViewById(R.id.ad_headline)
        myAdView.bodyView = myAdView.findViewById(R.id.ad_body)
        myAdView.callToActionView = myAdView.findViewById(R.id.ad_call_to_action)
        myAdView.iconView = myAdView.findViewById(R.id.ad_icon)
        myAdView.priceView = myAdView.findViewById(R.id.ad_price)
        myAdView.starRatingView = myAdView.findViewById(R.id.ad_rating)
        myAdView.storeView = myAdView.findViewById(R.id.ad_store)
        myAdView.advertiserView = myAdView.findViewById(R.id.ad_advertiser)
        (myAdView.headlineView as TextView).text = adFromGoogle.headline
        if (adFromGoogle.body == null) {
            myAdView.bodyView.visibility = View.GONE
        } else {
            (myAdView.bodyView as TextView).text = adFromGoogle.body
        }
        if (adFromGoogle.callToAction == null) {
            myAdView.callToActionView.visibility = View.GONE
        } else {
            (myAdView.callToActionView as Button).text = adFromGoogle.callToAction
        }
        if (adFromGoogle.icon == null) {
            myAdView.iconView.visibility = View.GONE
        } else {
            (myAdView.iconView as ImageView).setImageDrawable(adFromGoogle.icon.drawable)
        }
        if (adFromGoogle.price == null) {
            myAdView.priceView.visibility = View.GONE
        } else {
            (myAdView.priceView as TextView).text = adFromGoogle.price
        }
        if (adFromGoogle.starRating == null) {
            myAdView.starRatingView.visibility = View.GONE
        } else {
            (myAdView.starRatingView as RatingBar).rating = adFromGoogle.starRating.toFloat()
        }
        if (adFromGoogle.store == null) {
            myAdView.storeView.visibility = View.GONE
        } else {
            (myAdView.storeView as TextView).text = adFromGoogle.store
        }
        if (adFromGoogle.advertiser == null) {
            myAdView.advertiserView.visibility = View.GONE
        } else {
            (myAdView.advertiserView as TextView).text = adFromGoogle.advertiser
        }
        myAdView.setNativeAd(adFromGoogle)
    }
*/

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class UnifiedNativeAdViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        val adView: NativeAdView = view.findViewById(R.id.unifiedNativeAd)

        init {
            adView.iconView = adView.findViewById(R.id.ad_icon)
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
            adView.starRatingView = adView.findViewById(R.id.ad_stars)
            adView.mediaView = adView.findViewById(R.id.ad_media) as MediaView
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.priceView = adView.findViewById(R.id.ad_price)
            adView.storeView = adView.findViewById(R.id.ad_store)
        }
    }
}