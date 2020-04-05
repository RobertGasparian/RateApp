package com.example.rateapp.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class NetworkBoundResource<ResultType>(needLoading: Boolean = true) {
    private val liveData: MutableLiveData<Resource<ResultType>> = MutableLiveData()
    val asLiveData: LiveData<Resource<ResultType>>
        get() = liveData

    init {
        if (needLoading) {
            liveData.value = Resource.Loading(data = null)
        }
        this.createCall()
    }

    @MainThread
    protected abstract fun createCall()

    fun setValue(value: Resource<ResultType>) {
        liveData.value = value
    }
}