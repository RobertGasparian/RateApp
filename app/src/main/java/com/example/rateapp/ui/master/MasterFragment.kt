package com.example.rateapp.ui.master

import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rateapp.R
import com.example.rateapp.datasource.domainmodel.*
import com.example.rateapp.exhaustive
import com.example.rateapp.network.Resource
import com.example.rateapp.setEndDrawable
import com.example.rateapp.ui.activities.Navigator
import com.example.rateapp.ui.adapter.BankClickListener
import com.example.rateapp.ui.adapter.BanksAdapter
import com.example.rateapp.ui.base.BaseFragment
import com.example.rateapp.ui.base.BaseViewModel
import com.example.rateapp.ui.detail.DetailFragment
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_master.*

class MasterFragment : BaseFragment<MasterViewModel>(), BankClickListener {

    override val contentLayoutResource: Int
        get() = R.layout.fragment_master
    override val viewModel: BaseViewModel?
        get() = model

    private lateinit var model: MasterViewModel

    private val banksAdapter = BanksAdapter(this)

    companion object {
        private val allCurrencies = CurrencyType.values().map { it.toString() }
        private val allTransactions = TransactionType.values().map { it.toString() }
        fun newInstance() : MasterFragment {
            return MasterFragment()
        }
    }

    override fun setupViews() {
        context?.run {
            currencySp.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allCurrencies).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            transTypeSp.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allTransactions).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            branchRecycler.adapter = banksAdapter
        }
    }

    override fun configureViewActionHandling() {
        buyTv.setOnClickListener{ buyClick() }
        sellTv.setOnClickListener{ sellClick() }
        currencySp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                model.setCurrencyFilter(allCurrencies[p2])
            }

        }
        transTypeSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                model.setCashFilter(allTransactions[p2])
            }

        }
    }

    override fun setupViewModel() {
        model = ViewModelProvider(this).get(MasterViewModelImpl::class.java)
        model.setUp()
    }

    override fun bindWithModel() {
        model.sortState.observe(viewLifecycleOwner, Observer {
            when (it) {
                //TODO: implement distance check
                is Sort.DistanceSort -> {}
                is Sort.BuySort -> {
                    if (it.inAscState) {
                        buyTv.setEndDrawable(R.drawable.ic_arrow_drop_down)
                    } else {
                        buyTv.setEndDrawable(R.drawable.ic_arrow_drop_up)
                    }
                    sellTv.setEndDrawable(R.drawable.ic_arrow_transparent)
                }
                is Sort.SellSort -> {
                    if (it.inAscState) {
                        sellTv.setEndDrawable(R.drawable.ic_arrow_drop_down)
                    } else {
                        sellTv.setEndDrawable(R.drawable.ic_arrow_drop_up)
                    }
                    buyTv.setEndDrawable(R.drawable.ic_arrow_transparent)
                }
                null -> {
                    buyTv.setEndDrawable(R.drawable.ic_arrow_transparent)
                    sellTv.setEndDrawable(R.drawable.ic_arrow_transparent)
                }
            }.exhaustive
        })
        model.filterState.observe(viewLifecycleOwner, Observer {
            val (currencyType, transType) = it
            val curIndex = allCurrencies.indexOf(currencyType.toString())
            val transIndex = allTransactions.indexOf(currencyType.toString())
            currencySp.setSelection(curIndex)
            transTypeSp.setSelection(transIndex)
        })
        model.bankList.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    dismissLoading()
                    it.data?.run {
                        banksAdapter.newList(this)
                    }
                }
                is Resource.Error -> {
                    dismissLoading()
                    showErrorDialog()
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        })
        model.nextPageData.observe(viewLifecycleOwner, Observer {
            (activity as? Navigator)?.run {
                it?.let { triple ->
                    openFragment(DetailFragment.newInstance(triple.first, triple.second, triple.third))
                }
            }
        })
    }

    override fun onBankClick(position: Int, item: BankVM) {
        model.bankSelected(item.id, item.title)
    }

    private fun buyClick() {
        model.sortByBuy()
    }

    private fun sellClick() {
        model.sortBySell()
    }
}