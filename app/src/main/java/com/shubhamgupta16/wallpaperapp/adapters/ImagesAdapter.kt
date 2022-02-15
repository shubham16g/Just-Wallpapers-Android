package com.shubhamgupta16.wallpaperapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.app.WallModel
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform
import com.shubhamgupta16.wallpaperapp.utils.px

class ImagesAdapter(
    private val list: List<WallModel?>,
    private val isHorizontal:Boolean = false,
    private val listener: (wallModel: WallModel, i: Int) -> Unit
) :
    RecyclerView.Adapter<ImagesAdapter.ItemViewHolder>() {

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


            holder.imageView?.setBackgroundColor(Color.parseColor(model.color))

            holder.imageView?.let {
                Glide.with(it.context).load(model.urls.small)
                    .thumbnail()
                    .centerCrop()
                    .transform(RotationTransform(model.rotation?.toFloat() ?: 0f))
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
            if (viewType == 1) R.layout.item_image else R.layout.item_loader, parent, false
        ), viewType, isHorizontal
    )

    class ItemViewHolder(itemView: View, viewType: Int, isHorizontal: Boolean) : RecyclerView.ViewHolder(itemView) {
        init {
            if (isHorizontal) {
                (itemView as ConstraintLayout).apply {
                    maxWidth = if (viewType == 0) {
                        removeAllViews()
                        0
                    } else
                        112.px
                }
            }
        }
        val imageView: ImageView? =
            if (viewType == 1) itemView.findViewById(R.id.image_view) else null
        /*
        val cardView: CardView? =
            if (viewType == 1) itemView.findViewById(R.id.image_card_container) else null
        */
    }
}