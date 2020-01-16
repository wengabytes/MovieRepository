package com.wengabytes.movieviewer.utils.network

sealed class NetworkResult<out T : Any>
{
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error<out T : Any>(val errorMessage: String) : NetworkResult<T>()

    override fun toString(): String
    {
        return when (this)
        {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[errorMessage=$errorMessage]"
        }
    }
}