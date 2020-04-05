package com.example.rateapp.ui.viewholder

import android.view.View
import com.example.rateapp.datasource.domainmodel.CurrencyVM
import kotlinx.android.synthetic.main.item_currency.*

class CurrencyViewHolder(itemView: View) : BaseViewHolder<CurrencyVM>(itemView) {
    override fun bind(item: CurrencyVM) {
        currencyTv.text = item.name
        buyTv.text = item.buy
        sellTv.text = item.sell
    }
}