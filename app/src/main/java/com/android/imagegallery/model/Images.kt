package com.android.imagegallery.model

data class Images(
    val hits: List<Image>,
    val total: Int,
    val totalHits: Int
)