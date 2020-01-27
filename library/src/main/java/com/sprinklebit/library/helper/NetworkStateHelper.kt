package com.sprinklebit.library.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import java.util.*

object NetworkStateHelper {

    private var isConnected: Boolean? = null
    private val listeners = LinkedList<ChangeListener>()

    fun subscribe(listener: ChangeListener) {
        if (isConnected == null) error("You must to make initialization NetworkStateHelper in base Application class")
        listeners.add(listener)
        listener.onChange(isConnected!!)
    }

    fun unsubscribe(listener: ChangeListener) {
        listeners.remove(listener)
    }

    fun init(context: Context) {
        isConnected = isNetworkConnected(context)
        val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
                .build()
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                isConnected = true
                listeners.forEach { it.onChange(isConnected!!) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                isConnected = false
                listeners.forEach { it.onChange(isConnected!!) }
            }

            override fun onLost(network: Network?) {
                super.onLost(network)
                isConnected = false
                listeners.forEach { it.onChange(isConnected!!) }
            }

        })
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    interface ChangeListener {
        fun onChange(isConnected: Boolean)
    }
}