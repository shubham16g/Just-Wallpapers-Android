package com.shubhamgupta16.wallpaperapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubhamgupta16.wallpaperapp.databinding.ItemWallClientBinding
import com.shubhamgupta16.wallpaperapp.models.app.WallModelLite
import com.shubhamgupta16.wallpaperapp.utils.RotationTransform


class SingleImageAdapter(
    private val context: Context,
    private val list: List<WallModelLite?>,
    private val listener: ()->Unit
) :
    RecyclerView.Adapter<SingleImageAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = list[position] ?: return

        Glide.with(context).asBitmap()
            .load(model.urls.regular ?: model.urls.small)
            .thumbnail()
            .centerCrop()
            .transform(RotationTransform(model.rotation?.toFloat() ?: 0f))
            .into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            listener()
        }
    }

    override fun onViewRecycled(holder: ItemViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(context).clear(holder.binding.imageView)
        Glide.get(context).clearMemory()
        holder.binding.imageView.setImageBitmap(null)
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(ItemWallClientBinding.inflate(LayoutInflater.from(context), parent, false))
    class ItemViewHolder(val binding: ItemWallClientBinding) : RecyclerView.ViewHolder(binding.root)
}