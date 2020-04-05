package com.example.rateapp.ui.master

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.rateapp.datasource.domainmodel.*
import com.example.rateapp.ui.base.BaseViewModelImpl
import com.example.rateapp.datasource.domainmodel.Sort.*
import com.example.rateapp.domain.SortAndFilterManager
import com.example.rateapp.network.Resource
import com.example.rateapp.ui.base.BaseViewModel
import com.example.rateapp.ui.base.DoubleTrigger

class MasterViewModelImpl : BaseViewModelImpl(), MasterViewModel {

    companion object {
        const val DEFAULT_COMPARE_LAT: Double = 40.177200
        const val DEFAULT_COMPARE_LONG: Double = 44.503490
    }

    override val bankList: LiveData<Resource<List<BankVM>>>
        get() = viewList

    override val filterState: LiveData<Filter>
        get() = Transformations.distinctUntilChanged(filter)

    override val sortState: LiveData<Sort?>
        get() = Transformations.distinctUntilChanged(sort)

    override val nextPageData: LiveData<Triple<String, String, ArrayList<Currency>>?>
        get() = next


    private val next = MutableLiveData<Triple<String, String, ArrayList<Currency>>?>()
    private val viewList = MutableLiveData<Resource<List<BankVM>>>()
    private val filter = MutableLiveData(Filter())
    private val sort = MutableLiveData<Sort?>()
    private lateinit var initialList: LiveData<Resource<List<Bank>>>
    private var initialObserver: Observer<Resource<List<Bank>>> = Observer { res ->
        when(res){
            is Resource.Success -> {
                viewList.value = Resource.Success(
                    sortAndFilterList(
                        res.data ?: emptyList(),
                        sort.value,
                        filter.value ?: Filter()
                    )
                )
            }
            is Resource.Error -> {
                viewList.value = Resource.Error(message = res.message)
            }
            is Resource.Loading -> {
                viewList.value = Resource.Loading()
            }
        }
    }
    private var sortAndFilterObserver: Observer<List<BankVM>?> = Observer {
        it?.run {
            viewList.value = Resource.Success(this)
        }
    }

    private val sortAndFilterAction = Transformations.map(DoubleTrigger(sort, filter)) { pair ->
        return@map initialList.value?.data?.let { list ->
            sortAndFilterList(list, pair.first, pair.second ?: Filter())
        }
    }

    override fun setUp() {
        startLoadingData()
        observeSortAndFilterChanges()
    }

    override fun bankSelected(id: String, name: String) {
        initialList.value?.data?.find {
            it.id == id
        }?.let {
            val listOfCurrencies = ArrayList(it.currencies.values)
            next.value = Triple(id, name, listOfCurrencies)
        }.also {
            next.value = null
        }

    }

    private fun sortAndFilterList(list: List<Bank>, sort: Sort?, filter: Filter): List<BankVM> {
        val filteredList = list.asSequence()
            .filter { bank ->
                val (currency, cash) = filter
                val foundCashType = bank.currencies.values.find {
                    return@find when (cash) {
                        TransactionType.CASH -> it.cashTrans != null
                        TransactionType.NON_CASH -> it.nonCashTrans != null
                    }
                }
                bank.currencies.contains(currency) && foundCashType != null
            }
        val sortedList = sort?.let { s ->
            return@let list.sortedWith(Comparator { o1, o2 ->
                val cur1 = o1.currencies[filter.currencyType]
                val cur2 = o2.currencies[filter.currencyType]
                val rate1 = cur1?.let { c ->
                    if (filter.transactionType == TransactionType.CASH) c.cashTrans else c.nonCashTrans
                }?.rate
                val rate2 = cur2?.let { c ->
                    if (filter.transactionType == TransactionType.CASH) c.cashTrans else c.nonCashTrans
                }?.rate
                val thisLocation = Location("").apply {
                    latitude = DEFAULT_COMPARE_LAT
                    longitude = DEFAULT_COMPARE_LONG
                }
                when (s) {
                    is DistanceSort -> {
//                        val firstLocation = Location("").apply {
//                            latitude = o1.latLng.latitude
//                            longitude = o1.latLng.longitude
//                        }
//
//                        val secondLocation = Location("").apply {
//                            latitude = o2.latLng.latitude
//                            longitude = o2.latLng.longitude
//                        }
//                        thisLocation.distanceTo(firstLocation).toInt() - thisLocation.distanceTo(secondLocation).toInt()
                        0
                    }
                    is SellSort -> {
                        val sell1 = rate1?.sell
                        val sell2 = rate2?.sell
                        if (sell1 != null && sell2 != null) {
                            val forAsc = if (sell1 > sell2) 1 else if (sell1 == sell2) 0 else -1
                            if (s.inAscState) forAsc else -forAsc
                        } else {
                            0
                        }
                    }
                    is BuySort -> {
                        val buy1 = rate1?.buy
                        val buy2 = rate2?.buy
                        if (buy1 != null && buy2 != null) {
                            val forAsc = if (buy1 > buy2) 1 else if (buy1 == buy2) 0 else -1
                            if (s.inAscState) forAsc else -forAsc
                        } else {
                            0
                        }
                    }
                }
            })
        } ?: run {
            filteredList.toList()
        }
        val vmList = sortedList.mapNotNull vmMap@{ bank ->
            val cur = bank.currencies[filter.currencyType]
            val rate = when (filter.transactionType) {
                TransactionType.CASH -> cur?.cashTrans?.rate
                TransactionType.NON_CASH -> cur?.cashTrans?.rate
            }
            rate?.let {
                BankVM(
                    bank.id,
                    bank.title,
                    it.buy.toString(),
                    it.sell.toString()
                )
            } ?: run {
                return@vmMap null
            }

        }
        return vmList.toList()
    }

    private fun observeSortAndFilterChanges() {
        sortAndFilterAction.observeForever(sortAndFilterObserver)
    }

    private fun startLoadingData() {
        initialList = repo.getBankList().also {
            it.observeForever(initialObserver)
        }
    }

    override fun setCurrencyFilter(string: String?) {
        string?.let { currency ->
            filter.value = Filter(
                currencyType = CurrencyType.valueOrNull(currency) ?: CurrencyType.USD,
                transactionType = filter.value?.transactionType ?: TransactionType.CASH
            )
        } ?: run {
            filter.value =
                Filter(transactionType = filter.value?.transactionType ?: TransactionType.CASH)
        }
    }

    override fun setCashFilter(string: String?) {
        string?.let { cash ->
            filter.value = Filter(
                currencyType = filter.value?.currencyType ?: CurrencyType.USD,
                transactionType = TransactionType.valueOrNull(cash) ?: TransactionType.CASH
            )
        } ?: run {
            filter.value = Filter(currencyType = filter.value?.currencyType ?: CurrencyType.USD)
        }
    }

    override fun sortByDistance() {
        sort.value = if (sort.value is DistanceSort) {
            DistanceSort(!(sort.value?.inAscState ?: false))
        } else DistanceSort(true)
    }

    override fun sortByBuy() {
        sort.value = if (sort.value is BuySort) {
            BuySort(!(sort.value?.inAscState ?: false))
        } else BuySort(true)
    }

    override fun sortBySell() {
        sort.value = if (sort.value is SellSort) {
            SellSort(!(sort.value?.inAscState ?: false))
        } else SellSort(true)
    }

    override fun onCleared() {
        super.onCleared()
        initialList.removeObserver(initialObserver)
        sortAndFilterAction.removeObserver(sortAndFilterObserver)
    }
}

interface MasterViewModel : BaseViewModel, SortAndFilterManager {
    val bankList: LiveData<Resource<List<BankVM>>>
    val filterState: LiveData<Filter>
    val sortState: LiveData<Sort?>
    val nextPageData: LiveData<Triple<String, String, ArrayList<Currency>>?>
    fun setUp()
    fun bankSelected(id: String, name: String)
}