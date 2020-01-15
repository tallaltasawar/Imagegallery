package com.android.imagegallery

import android.app.Activity
import android.app.Application
import android.app.Service
import com.android.imagegallery.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

class GalleryApplication : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    protected lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun serviceInjector() = dispatchingServiceInjector

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
    }
}