package com.example.rateapp.datasource.domainmodel


sealed class Sort(val inAscState: Boolean) {
    class DistanceSort(inAscState: Boolean) : Sort(inAscState) {
        override fun equals(other: Any?): Boolean {
            return if (other is DistanceSort) {inAscState == other.inAscState} else return false
        }
    }
    class BuySort(inAscState: Boolean) : Sort(inAscState) {
        override fun equals(other: Any?): Boolean {
            return if (other is BuySort) {inAscState == other.inAscState} else return false
        }
    }
    class SellSort(inAscState: Boolean) : Sort(inAscState) {
        override fun equals(other: Any?): Boolean {
            return if (other is SellSort) {inAscState == other.inAscState} else return false
        }
    }
}