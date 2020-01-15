package com.android.imagegallery.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer

abstract class BaseDialogFragment<out H : BaseDialogHelper> : DialogFragment() {
    companion object {
        private const val NO_DIRECT_SHOW = "Do not instantiate a BaseDialogFragment directly! Use the helper instead"
    }

    val helper: H get() = targetFragment as H

    override fun onCreate(savedInstanceState: Bundle?) {
        try { helper } catch (e: Exception) {
            error("Can't access helper")
        }
        super.onCreate(savedInstanceState)

        helper.dismissEvent.observe(this, Observer {
            dismiss()
        })
    }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    @Deprecated(NO_DIRECT_SHOW, level = DeprecationLevel.ERROR)
    override fun show(manager: FragmentManager, tag: String?) = super.show(manager, tag)

    @Deprecated(NO_DIRECT_SHOW, level = DeprecationLevel.ERROR)
    override fun show(transaction: FragmentTransaction, tag: String?): Int = super.show(transaction, tag)

    @Deprecated(NO_DIRECT_SHOW, level = DeprecationLevel.ERROR)
    override fun showNow(manager: FragmentManager, tag: String?) = super.showNow(manager, tag)

    //    override fun onCancel(dialog: DialogInterface?) {
//        super.onCancel(dialog)
//        Log.d("BaseDialogFragment", "onCancel called")
//    }
//
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
//        Log.d(TAG, "onDismiss called")
//        Log.d(TAG, "getDialog is null: " + (this.dialog == null))
        /**
         * On a "real dismiss", getDialog will not be null, because super.onDestroyView won't be called
         * On an activity refresh, getDialog will be null, because super.onDestroyView will be called
         */
        if(this.dialog != null) {
//            Log.d(TAG, "detaching helper")
            helper.detach()
        }
    }
}