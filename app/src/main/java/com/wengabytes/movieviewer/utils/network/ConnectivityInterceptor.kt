package com.wengabytes.movieviewer.utils.network

import android.content.Context
import android.net.ConnectivityManager
import com.wengabytes.movieviewer.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor
{
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response
    {
        if (!isOnline(context))
        {
            throw NoConnectivityException(context.getString(R.string.errmsg_no_internet_connectivity))
        }
        val builder = chain.request().newBuilder()

        return chain.proceed(builder.build())
    }

    companion object
    {
        fun isOnline(context: Context): Boolean
        {
            val connectivityManager =
                    context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
    }

}

class NoConnectivityException(message: String?) : IOException(message)