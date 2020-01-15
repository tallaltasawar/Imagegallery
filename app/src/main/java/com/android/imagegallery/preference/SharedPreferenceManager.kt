package com.android.imagegallery.preference

import android.content.SharedPreferences
import com.android.imagegallery.util.GalleryConstants.PACKAGE_NAME

class SharedPreferenceManager(private val sharedPreferences: SharedPreferences) {

    companion object Keys {
        const val SHARED_PREFERENCE_NAME = "$PACKAGE_NAME.SC_SHARED_PREFERENCES"
    }
}