package com.example.rateapp.ui.viewholder

import android.view.View
import com.example.rateapp.datasource.domainmodel.Branch
import kotlinx.android.synthetic.main.item_branch.*

class BranchViewHolder(itemView: View) : BaseViewHolder<Branch>(itemView) {
    override fun bind(item: Branch) {
        titleTv.text = item.title
    }
}