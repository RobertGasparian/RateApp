package com.example.rateapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.rateapp.ui.viewholder.BaseViewHolder

abstract class BaseAdapter<Model, Holder: BaseViewHolder<Model>, Listener: ItemClickListener>(listener: Listener? = null): RecyclerView.Adapter<Holder>() {
    var listener: Listener? = null
    var list: MutableList<Model> = mutableListOf()

    init {
        this.listener = listener
    }

    fun newList(items: List<Model>) {
        list.clear()
        list.addAll(items)
        //TODO: implement DiffUtils
        notifyDataSetChanged()
    }

    //Override if needed
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

interface ItemClickListener