package com.example.android.politicalpreparedness.base

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel(private val app: Application): AndroidViewModel(app) {

    protected val _noInternetAccess = MutableLiveData<Boolean>()
    val noInternetAccess: LiveData<Boolean>
        get() = _noInternetAccess

    fun hasInternetConnection(): Boolean {
        val connectivityManager = app.getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        val hasConnection =  caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

        if (!hasConnection) {
            _noInternetAccess.value = true
        }
        return hasConnection
    }
}