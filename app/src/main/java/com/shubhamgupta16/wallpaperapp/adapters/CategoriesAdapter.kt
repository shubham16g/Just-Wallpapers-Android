package com.shubhamgupta16.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform

class CategoriesAdapter(
    private val list: List<CategoryModel>,
    private val isHorizontal: Boolean = false,
    private val listener: (categoryName: String) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.imageView.requestLayout()
        val model = list[position]

        holder.name.text = model.name

/*
        Glide.with(it.context).load(model.urls.small)
            .thumbnail()
            .centerCrop()
            .transform(RotationTransform(model.rotation?.toFloat() ?: 0f))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(it)
*/


        holder.itemView.setOnClickListener {
            listener(model.name)
        }
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            if (isHorizontal) R.layout.item_category_horizontal else R.layout.item_category_horizontal,
            parent,
            false
        )
    )

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val name: TextView = itemView.findViewById(R.id.category_name_text)
        /*
        val cardView: CardView? =
            if (viewType == 1) itemView.findViewById(R.id.image_card_container) else null
        */
    }
}