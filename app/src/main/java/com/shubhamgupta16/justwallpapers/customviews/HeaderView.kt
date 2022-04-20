package com.shubhamgupta16.justwallpapers.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.shubhamgupta16.justwallpapers.R

class HeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private val titleView:TextView
    private val moreBtn: View


    var title:CharSequence?
    set(value) {
        titleView.text = value
    }
    get() = titleView.text

    var isMoreButtonVisible:Boolean
    set(value) {
        moreBtn.visibility = if (value)VISIBLE else GONE
    }
    get() = moreBtn.visibility == VISIBLE

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_header_view, this, true)
        titleView = findViewById(R.id.custom_header_title)
        moreBtn = findViewById(R.id.custom_header_more_btn)
        val a = context.obtainStyledAttributes(attrs, R.styleable.HeaderView)
        if (a.hasValue(R.styleable.HeaderView_title))
            title = a.getString(R.styleable.HeaderView_title)
        isMoreButtonVisible = a.getBoolean(R.styleable.HeaderView_moreButtonVisible, true)
        a.recycle()

    }

    fun setOnMoreClickListener(listener: OnClickListener){
        moreBtn.setOnClickListener(listener)
    }
}