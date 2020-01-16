package com.wengabytes.movieviewer.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wengabytes.movieviewer.R
import com.wengabytes.movieviewer.base.BaseInterface
import com.wengabytes.movieviewer.components.dialogs.LoadingFragment
import com.wengabytes.movieviewer.components.dialogs.PositiveDialogFragment
import com.wengabytes.movieviewer.components.dialogs.PositiveDialogInterface

class MainActivity : AppCompatActivity(), BaseInterface, PositiveDialogInterface {

    // START: Implement Required Methods

    override fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            if (supportFragmentManager.findFragmentByTag(LoadingFragment.LOADING_DIALOG_IDENTIFIER) == null) {
                val loadingFragment: LoadingFragment = LoadingFragment.newInstance()
                loadingFragment.show(
                    supportFragmentManager,
                    loadingFragment.getUniqueIdentifier()
                )
            }
        } else {
            (supportFragmentManager.findFragmentByTag(LoadingFragment.LOADING_DIALOG_IDENTIFIER) as LoadingFragment?)?.dismiss()
        }
    }

    override fun onError(errorMessage: String) {
        if (errorMessage.isEmpty())
            return

        val dialogFragment: PositiveDialogFragment = PositiveDialogFragment.newInstance(
            100,
            getString(R.string.title_error_dialog),
            errorMessage,
            getString(R.string.button_ok)
        )

        dialogFragment.show(supportFragmentManager, dialogFragment.uniqueIdentifier)
    }

    override fun doPositiveDialogClick(id: Int) {
    }

    // END  : Implement Required Methods

    // START: Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_main)
    }

    // END  : Callbacks
}