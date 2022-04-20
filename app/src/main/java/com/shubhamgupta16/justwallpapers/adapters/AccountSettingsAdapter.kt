package com.shubhamgupta16.justwallpapers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubhamgupta16.justwallpapers.R

data class AccountSettingsModel(val icon:Int, val title: String, var subTitle:String?=null)

class AccountSettingsAdapter(
    private val list: List<AccountSettingsModel>,
    private val listener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<AccountSettingsAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.icon.requestLayout()
        val model = list[position]

        holder.title.text = model.title
        holder.icon.setImageResource(model.icon)
        holder.subTitle.text = model.subTitle

        holder.itemView.setOnClickListener {
            listener(position)
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
        val subTitle: TextView = itemView.findViewById(R.id.sub_title)
    }
}