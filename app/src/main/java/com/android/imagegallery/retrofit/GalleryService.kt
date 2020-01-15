package com.android.imagegallery.retrofit

import com.android.imagegallery.model.Images
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url


/**
 * REST API access points
 */
interface GalleryService {

    companion object {
        const val BASE_URL = "https://pixabay.com/api/"

    }

    @GET(BASE_URL)
    fun getImages(@Query("key") key: String,
                  @Query("page") page: Int): Call<Images>

    @GET
    @Streaming
    fun downloadImage(@Url fileUrl: String): Call<ResponseBody>

}
