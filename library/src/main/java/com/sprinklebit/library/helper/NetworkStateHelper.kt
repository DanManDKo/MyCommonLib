package com.sprinklebit.library.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import timber.log.Timber
import java.util.concurrent.ConcurrentLinkedQueue

object NetworkStateHelper {

    private lateinit var connectivityManager: ConnectivityManager
    private var isConnected: Boolean? = null
    private val listeners = ConcurrentLinkedQueue<ChangeListener>()

    fun subscribe(listener: ChangeListener) {
        if (isConnected == null) error("You must to make initialization NetworkStateHelper in base Application class")
        listeners.add(listener)
        listener.onChange(isConnected!!)
    }

    fun unsubscribe(listener: ChangeListener) {
        listeners.remove(listener)
    }

    fun init(context: Context) {
        connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val state = isNetworkConnected()
        Timber.d("init; isNetworkConnected = $state")
        isConnected = state
        val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build()
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                isConnected = isNetworkConnected()
                listeners.forEach { it.onChange(isConnected!!) }
                Timber.d("onAvailable; isNetworkConnected = $isConnected")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                isConnected = false
                listeners.forEach { it.onChange(isConnected!!) }
                Timber.d("onUnavailable; isNetworkConnected = $isConnected")
            }

            override fun onLost(network: Network?) {
                super.onLost(network)
                isConnected = isNetworkConnected(network)
                listeners.forEach { it.onChange(isConnected!!) }
                Timber.d("onLost; isNetworkConnected = $isConnected")
            }
        })
    }

    private fun isNetworkConnected(network: Network? = null): Boolean {
        return if (network == null) {
            val netInfo = connectivityManager.activeNetworkInfo
            netInfo != null && netInfo.isConnected
        } else {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
    }

    interface ChangeListener {
        fun onChange(isConnected: Boolean)
    }
}