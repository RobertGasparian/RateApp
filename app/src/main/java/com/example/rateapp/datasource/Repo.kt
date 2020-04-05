package com.example.rateapp.datasource

import androidx.lifecycle.LiveData
import com.example.rateapp.ServiceLocator
import com.example.rateapp.datasource.domainmodel.Branch
import com.example.rateapp.datasource.domainmodel.Bank
import com.example.rateapp.network.ApiService
import com.example.rateapp.network.NetworkBoundResource
import com.example.rateapp.network.Resource
import com.example.rateapp.network.response.BankResponse
import com.example.rateapp.network.response.BranchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
object RepoImpl : Repo {

    private val apiService: ApiService by lazy {
        ServiceLocator.apiService
    }


    override fun getBankList(): LiveData<Resource<List<Bank>>> {
        return object : NetworkBoundResource<List<Bank>>() {
            override fun createCall() {
                apiService.getBanks().enqueue(object : Callback<BankResponse> {
                    override fun onResponse(
                        call: Call<BankResponse>,
                        response: Response<BankResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data =
                                BankListMapper.map(response.body()?.banks ?: emptyList())
                            setValue(Resource.Success(data = data))
                        } else {
                            setValue(Resource.Error(data = null, message = response.message()))
                        }
                    }

                    override fun onFailure(call: Call<BankResponse>, t: Throwable) {
                        setValue(
                            Resource.Error(
                                data = null,
                                message = t.message ?: "unknown error"
                            )
                        )
                    }
                })
            }

        }.asLiveData
    }

    override fun getBranchList(id: String): LiveData<Resource<List<Branch>>> {
        return object : NetworkBoundResource<List<Branch>>() {
            override fun createCall() {
                apiService.getBranches(id).enqueue(object : Callback<BranchResponse> {
                    override fun onResponse(
                        call: Call<BranchResponse>,
                        response: Response<BranchResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data =
                                BranchListMapper.map(response.body()?.branches ?: emptyList())
                            setValue(Resource.Success(data = data))
                        } else {
                            setValue(Resource.Error(data = null, message = response.message()))
                        }
                    }

                    override fun onFailure(call: Call<BranchResponse>, t: Throwable) {
                        setValue(
                            Resource.Error(
                                data = null,
                                message = t.message ?: "unknown error"
                            )
                        )
                    }

                })
            }
        }.asLiveData
    }


}


interface Repo {
    fun getBankList(): LiveData<Resource<List<Bank>>>

    fun getBranchList(id: String): LiveData<Resource<List<Branch>>>
}