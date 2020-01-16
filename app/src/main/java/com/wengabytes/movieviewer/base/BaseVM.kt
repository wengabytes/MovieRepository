package com.wengabytes.movieviewer.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wengabytes.movieviewer.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseVM(protected val context: Context) : ViewModel() {
    protected val jobParent = Job()
    protected val scope = CoroutineScope(Dispatchers.IO + jobParent)
    protected var job: Job? = null

    val ldLoading = MutableLiveData<Boolean>().apply {
        value = false
    }

    val ldError = MutableLiveData<Event<String>?>()

    override fun onCleared() {
        super.onCleared()
        jobParent.cancel()
    }
}