package com.badzohugues.staticlbcapp.misc

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

object NetworkHelper : LiveData<Boolean>() {
    private lateinit var application: Application
    private lateinit var networkRequest: NetworkRequest
    private lateinit var cm: ConnectivityManager

    fun init(application: Application) {
        this.application = application
        cm =
            NetworkHelper.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    @Suppress("DEPRECATION")
    // To check if network is available without observe live data
    fun isNetworkAvailable(): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.activeNetwork ?: return false
            val activeNetworks = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetworks.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetworks.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            cm.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
        return result
    }

    override fun onActive() {
        super.onActive()
        if (!isNetworkAvailable()) postValue(false)
        else getNetworkDetails()
    }

    private fun getNetworkDetails() {
        cm.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(
                    network: Network
                ) {
                    super.onAvailable(network)
                    postValue(true)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    postValue(false)
                }
            }
        )
    }
}
