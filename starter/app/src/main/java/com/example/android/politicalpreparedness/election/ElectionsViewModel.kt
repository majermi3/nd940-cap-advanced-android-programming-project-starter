package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.database.ElectionsRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ElectionsViewModel(
        app: Application,
        private val repository: ElectionsRepository): BaseViewModel(app) {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    init {
        loadUpcomingElections()
    }

    private fun loadUpcomingElections() {
        if (hasInternetConnection()) {
            CivicsApi.retrofitService.getElections().enqueue(object : Callback<ElectionResponse> {
                override fun onResponse(call: Call<ElectionResponse>, response: Response<ElectionResponse>) {
                    _upcomingElections.value = response.body()?.elections
                }

                override fun onFailure(call: Call<ElectionResponse>, t: Throwable) {
                    Timber.e(t)
                }
            })
        }
    }

    fun loadSavedElections() {
        viewModelScope.launch {
            _savedElections.value = repository.getAll()
        }
    }
}