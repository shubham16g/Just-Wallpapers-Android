package com.shubhamgupta16.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.wallpaperapp.R
import com.shubhamgupta16.wallpaperapp.models.init.CategoryModel

data class AccountSettingsModel(val icon:Int, val title: String)

class AccountSettingsAdapter(
    private val list: List<AccountSettingsModel>,
    private val listener: (categoryName: String) -> Unit
) :
    RecyclerView.Adapter<AccountSettingsAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.icon.requestLayout()
        val model = list[position]

        holder.title.text = model.title
        holder.icon.setImageResource(model.icon)

        holder.itemView.setOnClickListener {
            listener(model.title)
        }
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_account_row,
            parent,
            false
        )
    )

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val icon: ImageView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)
        /*
        val cardView: CardView? =
            if (viewType == 1) itemView.findViewById(R.id.image_card_container) else null
        */
    }
}