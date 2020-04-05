package com.example.rateapp.domain

interface SortAndFilterManager: SortManager, FilterManager

//SORT
interface SortManager: DistanceSortManager, BuySortManager, SellSortManager

interface DistanceSortManager { fun sortByDistance() }

interface BuySortManager { fun sortByBuy() }

interface SellSortManager { fun sortBySell() }


//FILTER
interface FilterManager: CurrencyFilterManager, CashFilterManager

interface CashFilterManager { fun setCashFilter(string: String?) }

interface CurrencyFilterManager { fun setCurrencyFilter(string: String?) }
