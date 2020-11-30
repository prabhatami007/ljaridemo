package com.vision2020
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
class AppApplication : Application() {
    init {
        instance = this
    }
    fun checkIfHasNetwork(): Boolean {
        var result = false
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        result = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        result = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        result = true
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        result = true
                    }
                    ConnectivityManager.TYPE_VPN -> {
                        return true
                    }
                }
            }
        }
        return result
    }
    companion object {
        private var instance: AppApplication? = null

        fun getInstance(): AppApplication? {
            return instance
        }
        fun hasNetwork(): Boolean {
            return instance!!.checkIfHasNetwork()
        }
    }
}