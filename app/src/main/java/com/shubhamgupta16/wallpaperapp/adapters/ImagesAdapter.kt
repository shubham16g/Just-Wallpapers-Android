package com.shubhamgupta16.wallpaperapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.roundToInt

class ImagesAdapter(
    private val context: Context,
    private val list: List<WallModel?>,
    private val isHorizontal:Boolean = false,
    private val listener: (wallModel: WallModel, i: Int) -> Unit
) :
    RecyclerView.Adapter<ImagesAdapter.ItemViewHolder>() {

    private val cardRadius = context.resources.getDimension(R.dimen.card_corner_radius)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.imageView?.requestLayout()
        val model = list[position]
        if (model == null) {
            if (!isHorizontal) {
                val layoutParams =
                    holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
            }
        } else {

            holder.imageView?.let {
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

//            Picasso.get().load(model.urls.small).rotate(model.rotation?.toFloat() ?: 0f)
//                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                listener(model, position)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) 0 else 1
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            when {
                isHorizontal -> R.layout.item_wall_horizontal
                viewType == 1 -> R.layout.item_image
                else -> R.layout.item_loader
            }, parent, false
        ), viewType, isHorizontal
    )

    class ItemViewHolder(itemView: View, viewType: Int, isHorizontal: Boolean) : RecyclerView.ViewHolder(itemView) {
        init {
            if (isHorizontal && viewType == 0) {
                (itemView as ConstraintLayout).apply {
                    maxWidth = 0
                    removeAllViews()
                }
            }
        }
        val imageView: ImageView? =
            if (viewType == 1) itemView.findViewById(R.id.image_view) else null
    }
}