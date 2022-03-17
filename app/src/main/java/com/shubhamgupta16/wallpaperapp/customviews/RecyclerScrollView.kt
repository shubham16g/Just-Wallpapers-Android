package com.shubhamgupta16.wallpaperapp.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class RecyclerScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    fun <T:ViewBinding> init(manager:LayoutManager, binding: T, onLayout: (binding:T)->Unit){
        class VH(val binding: T) :RecyclerView.ViewHolder(binding.root)
        layoutManager = manager
        adapter = object : RecyclerView.Adapter<VH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= VH(binding)

            override fun onBindViewHolder(holder: VH, position: Int) {
                onLayout(holder.binding)
            }
            override fun getItemCount()=1
        }
    }
    fun init(manager:LayoutManager, view: View, onLayout: (view:View)->Unit){
        class VVH(val view: View) :RecyclerView.ViewHolder(view)
        layoutManager = manager
        adapter = object : RecyclerView.Adapter<VVH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= VVH(view)

            override fun onBindViewHolder(holder: VVH, position: Int) {
                onLayout(holder.view)
            }
            override fun getItemCount()=1
        }
    }
}