package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class VoterInfoViewModel(app: Application) : AndroidViewModel(app) {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    fun loadVoterInfo(electionId: Int, address: Address) {
        CivicsApi.retrofitService.getVoterInfo(getFormattedAddress(address), electionId).enqueue(object : Callback<VoterInfoResponse> {
            override fun onResponse(call: Call<VoterInfoResponse>, response: Response<VoterInfoResponse>) {
                val vi = response.body()
                _voterInfo.value = vi
            }

            override fun onFailure(call: Call<VoterInfoResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun getFormattedAddress(address: Address): String {
        return "${address.line1}, ${address.zip} ${address.city} ${address.state}"
    }

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}