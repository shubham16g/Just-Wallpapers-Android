package com.shubhamgupta16.wallpaperapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.roundToInt

class HorizontalImagesAdapter(
    context: Context,
    private val list: List<WallModel>,
    private val listener: (wallModel: WallModel, i: Int) -> Unit
) :
    RecyclerView.Adapter<HorizontalImagesAdapter.ItemViewHolder>() {

    private val cardRadius = context.resources.getDimension(R.dimen.card_corner_radius)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.imageView.requestLayout()
        val model = list[position]

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

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_wall_horizontal, parent, false)
    )

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)


    }
}