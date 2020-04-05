package com.example.rateapp.datasource.domainmodel

data class Filter(
    var currencyType: CurrencyType? = CurrencyType.USD,
    var transactionType: TransactionType = TransactionType.CASH
)

enum class TransactionType {
    CASH,
    NON_CASH;

    companion object {
        fun valueOrNull(name: String): TransactionType? {
            return try {
                valueOf(name)
            } catch (ex: IllegalArgumentException) {
                null
            }
        }
    }
}