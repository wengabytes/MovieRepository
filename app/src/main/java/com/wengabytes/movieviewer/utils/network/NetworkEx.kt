package com.wengabytes.movieviewer.utils.network

import android.content.Context
import com.wengabytes.movieviewer.R
import org.xml.sax.SAXException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

suspend fun <T : Any> callWebservice(context: Context, webservice: suspend () -> T) = try
{
    val response = webservice()
    NetworkResult.Success(response)
}
catch (error: Exception)
{
    val message = when (error)
    {
        is NoConnectivityException, is ConnectException, is UnknownHostException -> error.message ?: context.getString(
            R.string.errmsg_unexpected)
        is SSLHandshakeException                                                 -> context.getString(R.string.errmsg_ssl_handshake)
        is SocketTimeoutException                                                -> context.getString(R.string.errmsg_timeout)
        is SocketException, is SAXException                                      -> context.getString(R.string.errmsg_unavailable)
        else                                                                     -> context.getString(R.string.errmsg_unexpected)
    }

    NetworkResult.Error<T>(message)
}