package com.shubhamgupta16.wallpaperapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.init.ColorModel

class ColorsHorizontalAdapter(
    private val list: List<ColorModel>,
    private val listener: (colorName: String, colorValue: Int) -> Unit
) :
    RecyclerView.Adapter<ColorsHorizontalAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position]
        val colorValue = Color.parseColor(model.value)
        holder.cardView.setCardBackgroundColor(colorValue)

/*
        Glide.with(it.context).load(model.urls.small)
            .thumbnail()
            .centerCrop()
            .transform(RotationTransform(model.rotation?.toFloat() ?: 0f))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(it)
*/


        holder.itemView.setOnClickListener {
            listener(model.name, colorValue)
        }
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_color_horizontal, parent, false)
    )

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val cardView: CardView = itemView.findViewById(R.id.image_card_container)
    }
}