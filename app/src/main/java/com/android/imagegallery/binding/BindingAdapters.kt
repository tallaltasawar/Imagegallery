package com.android.imagegallery.binding

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView


object BindingAdapters {

    /**
     * Loads the image asynchronously and display in View
     * @param url - Url of the image.
     */
    @BindingAdapter("url")
    @JvmStatic
    fun bindImage(view: PhotoView, url: String) {
        Glide.with(view.context)
            .load(url)
            .into(view)
    }
}