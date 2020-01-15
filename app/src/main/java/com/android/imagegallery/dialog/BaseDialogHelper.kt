package com.android.imagegallery.dialog

import android.os.Bundle
import com.android.imagegallery.helper.BaseHelper
import com.android.imagegallery.util.SingleLiveEvent

abstract class BaseDialogHelper(dialogFragment: BaseDialogFragment<*>) : BaseHelper() {
    private var df: BaseDialogFragment<*>? = dialogFragment
    val dismissEvent = SingleLiveEvent<Nothing>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val act = activity ?: return

        df?.setTargetFragment(this, 0)
        @Suppress("DEPRECATION_ERROR")
        df?.show(act.supportFragmentManager, null)

        df = null /* Dereference the fragment past this point, cause it might reset and leak. Any future
        commands to the fragment must be done using LiveData. */
    }

    fun dismissDialog() {
        dismissEvent.call()
    }
}