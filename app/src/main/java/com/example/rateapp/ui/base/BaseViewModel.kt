package com.example.rateapp.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.rateapp.ServiceLocator

open class BaseViewModelImpl : ViewModel(), BaseViewModel {
    protected val repo = ServiceLocator.repo
}

interface BaseViewModel