package com.android.imagegallery.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun <reified T : AppCompatActivity> getTargetIntent(context: Context): Intent {
    return Intent(context, T::class.java)
}

inline fun FragmentManager.inTransaction(addToBackStack: Boolean, func: FragmentTransaction.() -> FragmentTransaction) {
    if (addToBackStack) {
        beginTransaction().func().addToBackStack(null).commit()
    } else {
        beginTransaction().func().commit()
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String, addToBackStack: Boolean){
    supportFragmentManager.inTransaction(addToBackStack) { add(frameId, fragment, tag) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String, addToBackStack: Boolean) {
    supportFragmentManager.inTransaction(addToBackStack){replace(frameId, fragment, tag)}
}