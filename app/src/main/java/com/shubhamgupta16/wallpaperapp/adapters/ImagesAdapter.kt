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
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.wallpapers.wall.BaseWallModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.wall.AdModel
import com.shubhamgupta16.wallpaperapp.models.wallpapers.wall.WallModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.roundToInt


class ImagesAdapter(
    context: Context,
    private val list: List<BaseWallModel?>,
    private val adList: List<NativeAd>,
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
        if (holder is ItemViewHolder && model is WallModel) {

            holder.imageView.requestLayout()

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
        } else if (holder is UnifiedNativeAdViewHolder && model is AdModel) {
            goFullSpan(holder)
            val unifiedNativeAdView = holder.adView
            unifiedNativeAdView.visibility = View.VISIBLE
            if (adList.isNotEmpty())
                populateNativeAdView(
                    adList[if (model.adPosition < adList.size) model.adPosition else adList.lastIndex],
                    unifiedNativeAdView
                )

        } else if (holder is EmptyViewHolder) {
            goFullSpan(holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            null -> 0
            is WallModel -> 1
            is AdModel -> {
                if (adList.isNotEmpty()) 2 else 3
            }
            else -> 0
        }
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
        )
        3 -> EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wall_ad2, parent, false)
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

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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