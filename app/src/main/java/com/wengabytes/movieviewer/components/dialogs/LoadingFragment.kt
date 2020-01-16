package com.wengabytes.movieviewer.components.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import com.wengabytes.movieviewer.R

class LoadingFragment : AppCompatDialogFragment() {
    // START: Callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.f_loading, container, false)
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return view
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }
    // END  : Callbacks

    // START: Custom Methods
    fun getUniqueIdentifier(): String? {
        return LOADING_DIALOG_IDENTIFIER
    }
    // END  : Custom Methods

    companion object {
        const val LOADING_DIALOG_IDENTIFIER = "uniqueLoadingIdentifier"

        fun newInstance(): LoadingFragment {
            return LoadingFragment()
        }
    }
}