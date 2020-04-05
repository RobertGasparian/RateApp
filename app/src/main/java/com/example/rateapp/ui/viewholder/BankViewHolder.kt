package com.example.rateapp.ui.viewholder

import android.view.View
import com.example.rateapp.datasource.domainmodel.BankVM
import kotlinx.android.synthetic.main.item_bank.*

class BankViewHolder(itemView:View) : BaseViewHolder<BankVM>(itemView) {
    override fun bind(item: BankVM) {
        titleTv.text = item.title
        buyTv.text = item.buy
        sellTv.text = item.sell
    }
}