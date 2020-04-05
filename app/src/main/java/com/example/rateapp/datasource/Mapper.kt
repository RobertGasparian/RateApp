package com.example.rateapp.datasource

import android.location.Location
import com.example.rateapp.datasource.domainmodel.*
import com.example.rateapp.datasource.domainmodel.Currency
import com.example.rateapp.extractIntRange
import com.example.rateapp.getHours
import com.example.rateapp.network.dto.BankDTO
import com.example.rateapp.network.dto.BranchDTO
import java.util.*

interface Mapper<I, O> {
    fun map(input: I): O?
}

interface ListMapper<I, O> :
    Mapper<List<I>, List<O>> {

    val singleMapper: Mapper<I, O?>

    override fun map(input: List<I>): List<O> {
        val newList = mutableListOf<O>()
        input.forEach {
            singleMapper.map(it)?.apply {
                newList += this
            }
        }
        return newList.toList()
    }
}

object BranchListMapper :
    ListMapper<BranchDTO, Branch> {
    override val singleMapper: Mapper<BranchDTO, Branch?>
        get() = BranchMapper
}

object BranchMapper :
    Mapper<BranchDTO, Branch?> {
    override fun map(input: BranchDTO): Branch? {
        val (id,
            head,
            title,
            address,
            location,
            contacts,
            workhours) = input
        return if (head != null
            && title != null
            && address != null
            && location != null
            && contacts != null
            && workhours != null
        ) {
            Branch(
                head = head,
                title = title,
                address = address,
                location = Location("").apply {
                    latitude = location.lat ?: 0.0; longitude = location.lng ?: 0.0
                },
                contacts = contacts,
                workHours = workhours
                    .mapNotNull myMap@{
                        val (days, hours) = it
                        if (days != null && hours != null) {
                            val range = days.extractIntRange()
                            val pair = hours.getHours()
                            if (range != null && pair != null) {
                                return@myMap WorkHour(range, pair.first, pair.second)
                            } else {
                                return@myMap null
                            }
                        }
                        return@myMap null
                    }
            )
        } else null
    }
}

object BankListMapper : ListMapper<BankDTO, Bank> {
    override val singleMapper: Mapper<BankDTO, Bank?>
        get() = BankMapper
}

object BankMapper : Mapper<BankDTO, Bank?> {
    override fun map(input: BankDTO): Bank? {
        val (id, title, date, logo, currencies) = input
        return if (title != null
            && date != null
            && currencies != null
        ) {
            Bank(
                id = id,
                title = title,
                date = Date(date),
                logo = logo,
                currencies = currencies
                    .asSequence()
                    .mapNotNull myMap@{
                        val (name, cash, nonCash) = it
                        if (name != null && cash != null && nonCash != null) {
                            val (cashBuy, cashSell) = cash
                            val (nonBuy, nonSell) = nonCash
                            val cashTrans = if (cashBuy != null && cashSell != null) {
                                Transaction(TransactionType.CASH, Rate(cashBuy, cashSell))
                            } else null
                            val nonCashTrans = if (nonBuy != null && nonSell != null) {
                                Transaction(TransactionType.NON_CASH, Rate(nonBuy, nonSell))
                            } else null
                            val currency = CurrencyType.valueOrNull(name)
                            if ((cashTrans != null || nonCashTrans != null) && currency != null) {
                                return@myMap Currency(currency, cashTrans, nonCashTrans)
                            }
                            return@myMap null
                        }
                        return@myMap null
                    }.map {
                        it.name to it
                    }.toMap()
            )
        } else null
    }

}