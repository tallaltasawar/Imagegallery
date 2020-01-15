package com.android.imagegallery.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {

    /**
     * Loads the image asynchronously and display in View
     * @param url - Url of the image.
     * @param listener - Callbacks
     */
    @BindingAdapter(value = ["imageUrl", "imageRequestListener"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String, listener: RequestListener<Drawable?>?) {
        Glide.with(fragment).load(url).apply(RequestOptions().override(600, 200)).listener(listener).into(imageView)
    }
}

