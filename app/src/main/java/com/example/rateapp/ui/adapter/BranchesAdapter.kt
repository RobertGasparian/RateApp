package com.example.rateapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.rateapp.R
import com.example.rateapp.datasource.domainmodel.Branch
import com.example.rateapp.ui.viewholder.BranchViewHolder

class BranchesAdapter(itemClickListener: BranchClickListener? = null) : BaseAdapter<Branch, BranchViewHolder, BranchClickListener>(itemClickListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        val holderView = LayoutInflater.from(parent.context).inflate(R.layout.item_branch, parent, false)
        return BranchViewHolder(holderView)
    }

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            listener?.onBranchClick(position, list[position])
        }
    }
}

interface BranchClickListener: ItemClickListener {
    fun onBranchClick(position: Int, branch: Branch)
}