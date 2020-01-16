package com.wengabytes.movieviewer.components.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf

/**
 * Created by Mikacuy on 3/14/2017.
 */
class PositiveDialogFragment : AppCompatDialogFragment()
{
    private var dialogId = 0
    private var title: String? = null
    private var message: String? = null

    private var postiveButtonName: String? = null
    private var positiveDialogInterface: PositiveDialogInterface? = null

    val uniqueIdentifier: String = PositiveDialogFragment::class.java.toString()

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        positiveDialogInterface = if (context is PositiveDialogInterface)
        {
            context
        }
        else
        {
            throw RuntimeException("$context must implement PositiveDialogInterface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.run {
            dialogId = getInt(KEY_DIALOG_ID)
            title = getString(KEY_TITLE)
            message = getString(KEY_MESSAGE)
            postiveButtonName = getString(KEY_POSITIVE_BUTTON_LABEL)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(postiveButtonName) { _: DialogInterface?, _: Int ->
            positiveDialogInterface!!.doPositiveDialogClick(dialogId)
            dismiss()
        }
        return builder.create()
    }

    override fun onDetach()
    {
        super.onDetach()
        positiveDialogInterface = null
    }

    companion object
    {
        private const val KEY_DIALOG_ID = "dialogId"
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"
        private const val KEY_POSITIVE_BUTTON_LABEL = "button"

        fun newInstance(id: Int, title: String?, message: String?, postiveButtonName: String?): PositiveDialogFragment =
            PositiveDialogFragment().apply {
                arguments = bundleOf(
                    KEY_DIALOG_ID to id,
                                     KEY_TITLE to title,
                                     KEY_MESSAGE to message,
                                     KEY_POSITIVE_BUTTON_LABEL to postiveButtonName)
            }
    }
}