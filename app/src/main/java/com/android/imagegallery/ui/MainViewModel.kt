package com.android.imagegallery.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.imagegallery.model.Images
import com.android.imagegallery.repository.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val images = MediatorLiveData<Images>()

    private var page: Int = 1

    fun getImages() {
        images.addSource(repository.getImages(page)) {
            it?.let {
                page++
                images.value = it
            }
        }
    }
}