package com.example.rateapp.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer, Bindable<T> {
    override val containerView: View?
        get() = itemView
}

interface Bindable<T> {
    fun bind(item: T)
}