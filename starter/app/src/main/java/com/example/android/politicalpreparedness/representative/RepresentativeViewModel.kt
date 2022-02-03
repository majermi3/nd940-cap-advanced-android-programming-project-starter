package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.representative.model.Representative
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RepresentativeViewModel(app: Application): BaseViewModel(app) {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    fun findRepresentatives(address: String, callback: (() -> Unit)? = null) {
        if (hasInternetConnection()) {
            CivicsApi.retrofitService.getRepresentatives(address).enqueue(object : Callback<RepresentativeResponse> {
                override fun onResponse(call: Call<RepresentativeResponse>, response: Response<RepresentativeResponse>) {
                    response.body()?.let { representativesResponse ->
                        _representatives.value = representativesResponse.offices.flatMap { office ->
                            office.getRepresentatives(representativesResponse.officials)
                        }
                        callback?.let { it() }
                    }
                }

                override fun onFailure(call: Call<RepresentativeResponse>, t: Throwable) {
                    Timber.e(t)
                }
            })
        }
    }
}
