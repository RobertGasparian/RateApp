package com.example.rateapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.rateapp.R
import com.example.rateapp.datasource.domainmodel.Bank
import com.example.rateapp.datasource.domainmodel.BankVM
import com.example.rateapp.ui.viewholder.BankViewHolder

class BanksAdapter(itemClickListener: BankClickListener? = null) : BaseAdapter<BankVM, BankViewHolder, BankClickListener>(itemClickListener) {
    //TODO: need to implement view holder factory
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val holderView = LayoutInflater.from(parent.context).inflate(R.layout.item_bank, parent, false)
        return BankViewHolder(holderView)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            listener?.onBankClick(position, list[position])
        }
    }
}

interface BankClickListener: ItemClickListener {
    fun onBankClick(position: Int, item: BankVM)
}