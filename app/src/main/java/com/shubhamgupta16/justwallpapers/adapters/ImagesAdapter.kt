package com.shubhamgupta16.justwallpapers.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.shubhamgupta16.justwallpapers.R
import com.shubhamgupta16.justwallpapers.models.wallpapers.WallModel
import com.shubhamgupta16.justwallpapers.utils.RotationTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.roundToInt


class ImagesAdapter(
    context: Context,
    private val list: List<WallModel?>,
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
        if (holder is ItemViewHolder && model != null) {

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
        } else {
            goFullSpan(holder)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            null -> 0
            else -> 1
        }
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> LoaderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
        )
        else -> ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wall, parent, false)
        )
    }

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}