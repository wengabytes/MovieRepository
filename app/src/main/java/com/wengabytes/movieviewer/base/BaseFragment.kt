package com.wengabytes.movieviewer.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wengabytes.movieviewer.components.MainVMFactory
import org.koin.android.ext.android.inject

abstract class BaseFragment<T : BaseVM> : Fragment() {
    protected var baseInterface: BaseInterface? = null

    protected val factory: MainVMFactory by inject()

    protected lateinit var vm: T

    abstract fun provideVM(): T

    // START: Callbacks
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm = provideVM()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is BaseInterface)
            baseInterface = context
        else
            throw ClassCastException("$context must implement ${BaseInterface::class.java}")
    }

    // END  : Callbacks
}