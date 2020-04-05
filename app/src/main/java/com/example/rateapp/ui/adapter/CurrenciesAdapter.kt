package com.example.rateapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.rateapp.R
import com.example.rateapp.datasource.domainmodel.Currency
import com.example.rateapp.datasource.domainmodel.CurrencyVM
import com.example.rateapp.ui.viewholder.CurrencyViewHolder

class CurrenciesAdapter(itemClickListener: CurrencyClickListener? = null) : BaseAdapter<CurrencyVM, CurrencyViewHolder, CurrencyClickListener>(itemClickListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val holderView = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(holderView)
    }
}

interface CurrencyClickListener: ItemClickListener {
    fun onCurrencyClick(position: Int, currency: CurrencyVM)
}