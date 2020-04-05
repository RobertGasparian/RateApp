package com.example.rateapp.ui.detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rateapp.R
import com.example.rateapp.datasource.domainmodel.Branch
import com.example.rateapp.datasource.domainmodel.Currency
import com.example.rateapp.datasource.domainmodel.CurrencyVM
import com.example.rateapp.datasource.domainmodel.TransactionType
import com.example.rateapp.extractWorkHoursString
import com.example.rateapp.network.Resource
import com.example.rateapp.ui.adapter.*
import com.example.rateapp.ui.base.BaseFragment
import com.example.rateapp.ui.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : BaseFragment<DetailViewModel>(), CurrencyClickListener, BranchClickListener {

    override val contentLayoutResource: Int
        get() = R.layout.fragment_detail

    override val viewModel: BaseViewModel?
        get() = model

    private lateinit var model: DetailViewModel

    private var currencyAdapter = CurrenciesAdapter(this)
    private var branchesAdapter = BranchesAdapter(this)

    companion object {
        private const val ID_KEY = "id"
        private const val NAME_KEY = "name"
        private const val CURRENCIES_KEY = "currencies"

        fun newInstance(
            id: String,
            bankName: String,
            currencies: ArrayList<Currency>
        ): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_KEY, id)
                    putString(NAME_KEY, bankName)
                    putParcelableArrayList(CURRENCIES_KEY, currencies)
                }
            }
        }
    }

    override fun setupViews() {
        arguments?.run {
            bankNameTv.text = getString(NAME_KEY)
        }
        currencyRV.adapter = currencyAdapter
        branchesRV.adapter = branchesAdapter
    }

    override fun configureViewActionHandling() {
        radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.cashButton -> {
                    model.setCashFilter(TransactionType.CASH.toString())
                }
                R.id.nonCashButton -> {
                    model.setCashFilter(TransactionType.NON_CASH.toString())
                }
            }
        }
    }

    override fun setupViewModel() {
        model = ViewModelProvider(this).get(DetailViewModelImpl::class.java)
        arguments?.run {
            val currencies = getParcelableArrayList<Currency>(CURRENCIES_KEY) ?: ArrayList()
            getString(ID_KEY)?.run {
                model.setUp(this, currencies)
            }
        }
    }

    override fun bindWithModel() {
        model.branch.observe(viewLifecycleOwner, Observer {
            it?.let {
                titleTv.text = it.title
                addressTv.text = it.address
                contactsTv.text = it.contacts
                workhourstv.text = it.workHours.extractWorkHoursString()
            } ?: run {

            }
        })
        model.branchList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    dismissLoading()
                    it.data?.run {
                        branchesAdapter.newList(this)
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
        model.currencies.observe(viewLifecycleOwner, Observer {
            currencyAdapter.newList(it)
        })
        model.filterState.observe(viewLifecycleOwner, Observer {
            when(it.transactionType) {
                TransactionType.CASH -> cashButton.isSelected = true
                TransactionType.NON_CASH -> nonCashButton.isSelected = true
            }
        })
    }

    override fun onCurrencyClick(position: Int, currency: CurrencyVM) {
        //do nothing
    }

    override fun onBranchClick(position: Int, branch: Branch) {
        model.newCurrentBranch(position)
    }
}