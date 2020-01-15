package com.android.imagegallery.util

import com.android.imagegallery.BuildConfig

object GalleryConstants {

    const val PACKAGE_NAME = BuildConfig.APPLICATION_ID

    /**
     * Retrofit configuration
     */
    const val CONNECT_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L

    const val TAG_IMAGE_LIST_FRAGMENT = "imageListFragment"
}