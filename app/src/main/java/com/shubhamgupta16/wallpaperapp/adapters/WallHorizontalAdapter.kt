package com.shubhamgupta16.wallpaperapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.wallpapers.WallModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform

class WallHorizontalAdapter(
    private val list: List<WallModel>,
    private val listener: (wallModel: WallModel, i: Int) -> Unit
) :
    RecyclerView.Adapter<WallHorizontalAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.imageView.requestLayout()
        val model = list[position]

        holder.imageView.setBackgroundColor(Color.parseColor(model.color))

        Glide.with(holder.imageView.context).load(model.urls.small)
            .thumbnail()
            .centerCrop()
            .transform(RotationTransform(model.rotation?.toFloat() ?: 0f))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            listener(model, position)
        }
    }

    override fun onViewRecycled(holder: ItemViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.imageView.context).clear(holder.imageView)
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_wall_horizontal, parent, false
        )
    )

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}