package com.example.rateapp.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.rateapp.R


class LoadingDialogFragment : DialogFragment() {

    companion object {
        const val LOADING_TAG = "loading_tag"
        fun newInstance(): LoadingDialogFragment {
            return LoadingDialogFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_loading, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = object : Dialog(context!!, theme) {
            override fun onBackPressed() {
                if (activity?.supportFragmentManager
                        ?.findFragmentByTag(LOADING_TAG) != null
                ) {
                    (activity?.supportFragmentManager
                        ?.findFragmentByTag(LOADING_TAG) as LoadingDialogFragment?)?.dismiss()
                    activity?.onBackPressed()
                    return
                }
                super.onBackPressed()
            }
        }
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val windowParams = window?.attributes
            windowParams?.let {
                windowParams.flags =
                    windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
                window.attributes = windowParams
            }
        }
    }
}