package com.android.imagegallery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.imagegallery.model.Images
import com.android.imagegallery.retrofit.GalleryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val galleryService: GalleryService) {

    companion object {
        const val TAG = "Repository"
        const val KEY = "10961674-bf47eb00b05f514cdd08f6e11"
    }

    /**
     * Loads the images from url
     * @param page - Number of page loaded.
     */
    fun getImages(page: Int): MutableLiveData<Images?> {
        val images = MutableLiveData<Images?>()
        galleryService.getImages(KEY, page).enqueue(object : Callback<Images> {
            override fun onResponse(call: Call<Images>, response: Response<Images>) {
                Log.d(TAG, response.body().toString())
                images.postValue(response.body())
            }

            override fun onFailure(call: Call<Images>, t: Throwable) {
                Log.e(TAG, t.message)
            }
        })
        return images
    }
}