package com.android.imagegallery.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class BaseHelper : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true /* The magic. Once this is set, onCreate would only be called once, and the helper
         would stay alive long enough to receive the callback */
    }

    /**
     * This must be called after the helper is instantiated.
     * @param parent the calling activity.
     * @param singleton If set to true, only attaches the helper if another of the same class is not already attached.
     */
    open fun attachTo(activity: FragmentActivity, singleton: Boolean = false) {
        if(singleton && activity.supportFragmentManager.fragments.any { this::class.isInstance(it) }) return
        activity.supportFragmentManager.beginTransaction().add(this, null).commit()
    }

    /**
     * Call this when you're sure the helper is not needed any more, to detach it from the parent
     * activity.
     */
    open fun detach() {
        val act = activity
        act?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}