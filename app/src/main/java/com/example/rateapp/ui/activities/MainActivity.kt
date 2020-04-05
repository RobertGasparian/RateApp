package com.example.rateapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rateapp.R
import com.example.rateapp.network.ApiService
import com.example.rateapp.network.response.BankResponse
import com.example.rateapp.network.response.BranchResponse
import com.example.rateapp.ui.base.BaseFragment
import com.example.rateapp.ui.detail.DetailFragment
import com.example.rateapp.ui.master.MasterFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment(MasterFragment.newInstance(), false)
    }

    override fun openFragment(fragment: BaseFragment<*>, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out,  R.anim.slide_right_out, R.anim.slide_right_in)
            .replace(R.id.root, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        if (!supportFragmentManager.isStateSaved) {
            transaction.commit()
        } else {
            transaction.commitAllowingStateLoss()
        }
    }
}

//TODO: need to replace
interface Navigator {
    fun openFragment(fragment: BaseFragment<*>, addToBackStack: Boolean = true)
}
