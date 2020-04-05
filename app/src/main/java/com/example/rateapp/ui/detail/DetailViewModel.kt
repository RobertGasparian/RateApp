package com.example.rateapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.rateapp.datasource.domainmodel.*
import com.example.rateapp.domain.CashFilterManager
import com.example.rateapp.network.Resource
import com.example.rateapp.ui.base.BaseViewModel
import com.example.rateapp.ui.base.BaseViewModelImpl

class DetailViewModelImpl : BaseViewModelImpl(), DetailViewModel {
    override val branch: LiveData<Branch>
        get() = currentBranch
    override val branchList: LiveData<Resource<List<Branch>>>
        get() = initialBranchList
    override val currencies: LiveData<List<CurrencyVM>>
        get() = viewList
    override val filterState: LiveData<Filter>
        get() = Transformations.distinctUntilChanged(filter)

    private val viewList = MutableLiveData<List<CurrencyVM>>()
    private val filter = MutableLiveData(Filter(currencyType = null))
    private var currencyList: List<Currency> = emptyList()
    private lateinit var initialBranchList: LiveData<Resource<List<Branch>>>
    private var currentBranch = MutableLiveData<Branch>()

    private var filterObserver: Observer<List<CurrencyVM>> = Observer {
        it?.run {
            viewList.value = this
        }
    }

    private var initialObserver: Observer<Resource<List<Branch>>> = Observer { res ->
        if (res != null && res is Resource.Success) {
            currentBranch.value = res.data?.get(0)
        }
    }

    private val filterAction = Transformations.map(filter) { filter ->
        return@map filter?.transactionType?.let {
            filterByCashType(currencyList, it)
        } ?: emptyList()
    }

    override fun setUp(id: String, currencies: List<Currency>) {
        currencyList = currencies
        startLoadingData(id)
        observeFilterChanges()
    }

    override fun newCurrentBranch(position: Int) {
        initialBranchList.value?.data?.run {
            currentBranch.value = get(position)
        }
    }

    private fun startLoadingData(id: String) {
        initialBranchList = repo.getBranchList(id).also {
            it.observeForever(initialObserver)
        }
    }

    private fun observeFilterChanges() {
        filterAction.observeForever(filterObserver)
    }

    override fun setCashFilter(string: String?) {
        string?.let { cash ->
            filter.value = Filter(
                transactionType = TransactionType.valueOrNull(cash) ?: TransactionType.CASH,
                currencyType = null
            )
        } ?: run {
            filter.value = Filter(currencyType = null)
        }
    }

    private fun filterByCashType(list: List<Currency>, type: TransactionType): List<CurrencyVM> {
        val newList = mutableListOf<CurrencyVM>()
        list.forEach {
            val name = it.name.toString()
            val rate = when(type) {
                TransactionType.CASH -> it.cashTrans?.rate
                TransactionType.NON_CASH -> it.nonCashTrans?.rate
            }
            if (rate != null) {
                newList += CurrencyVM(
                    name,
                    rate.buy.toString(),
                    rate.sell.toString()
                )
            }
        }
        return newList.toList()
    }

    override fun onCleared() {
        super.onCleared()
        initialBranchList.removeObserver(initialObserver)
        filterAction.removeObserver(filterObserver)
    }

}

interface DetailViewModel : BaseViewModel, CashFilterManager {
    val branch: LiveData<Branch>
    val branchList: LiveData<Resource<List<Branch>>>
    val filterState: LiveData<Filter>
    val currencies: LiveData<List<CurrencyVM>>
    fun setUp(id: String, currencies: List<Currency>)
    fun newCurrentBranch(position: Int)
}