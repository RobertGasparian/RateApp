package com.example.rateapp.ui.base

import android.R
import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


abstract class BaseFragment<VM: BaseViewModel> : Fragment() {

    @get:LayoutRes
    protected abstract val contentLayoutResource: Int

    protected abstract val viewModel: BaseViewModel?

    val loadingDialog: LoadingDialogFragment by lazy {
        LoadingDialogFragment.newInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(contentLayoutResource, container, false)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        configureViewActionHandling()
        setupViewModel()
        bindWithModel()
    }

    protected open fun showErrorDialog(title: String? = null,
                                       message: String? = null,
                                       buttonText: String? = null) {
        context?.run {
            AlertDialog.Builder(this)
                .setTitle(title ?: getString(com.example.rateapp.R.string.error_title))
                .setMessage(message ?: getString(com.example.rateapp.R.string.error_message))
                .setPositiveButton(buttonText ?: getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .create().show()
        }
    }

    protected open fun showLoading() {
        fragmentManager?.run {
            loadingDialog.show(this, LoadingDialogFragment.LOADING_TAG)
        }
    }

    protected open fun dismissLoading() {
        if(fragmentManager?.findFragmentByTag(LoadingDialogFragment.LOADING_TAG) != null) {
            loadingDialog.dismiss()
        }
    }

    //override if needed
    protected open fun setupViews() {}

    //override if needed
    protected open fun configureViewActionHandling() {}

    //override if needed
    protected open fun setupViewModel() {}

    //override if needed
    protected open fun bindWithModel() {}

}