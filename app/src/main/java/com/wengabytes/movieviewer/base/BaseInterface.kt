package com.wengabytes.movieviewer.base

interface BaseInterface
{
    fun onLoading(isLoading: Boolean)
    fun onError(errorMessage: String)
}