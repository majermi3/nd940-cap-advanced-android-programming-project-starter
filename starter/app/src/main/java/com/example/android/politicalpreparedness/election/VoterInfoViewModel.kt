package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.database.ElectionsRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class VoterInfoViewModel(app: Application,
                         private val repository: ElectionsRepository) : BaseViewModel(app) {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _savedElection = MutableLiveData<Election>()
    val savedElection: LiveData<Election>
        get() = _savedElection

    fun loadVoterInfo(electionId: Int, address: Address) {
        if (hasInternetConnection()) {
            CivicsApi.retrofitService.getVoterInfo(getFormattedAddress(address), electionId).enqueue(object : Callback<VoterInfoResponse> {
                override fun onResponse(call: Call<VoterInfoResponse>, response: Response<VoterInfoResponse>) {
                    val vi = response.body()
                    _voterInfo.value = vi
                }

                override fun onFailure(call: Call<VoterInfoResponse>, t: Throwable) {
                    Timber.e(t)
                }
            })
            viewModelScope.launch {
                _savedElection.value = repository.get(electionId)
            }
        }
    }

    private fun getFormattedAddress(address: Address): String {
        return "${address.line1}, ${address.zip} ${address.city} ${address.state}"
    }

    fun saveElection() {
        viewModelScope.launch {
            if (_savedElection.value != null) {
                repository.delete(_savedElection.value!!.id)
                _savedElection.value = null
            } else {
                _voterInfo.value?.let {
                    repository.insert(it.election)
                    _savedElection.value = it.election
                }
            }
        }
    }
}