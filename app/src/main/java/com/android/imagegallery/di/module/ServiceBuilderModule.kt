package com.android.imagegallery.di.module

import com.android.imagegallery.service.DownloadService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeDownloadService(): DownloadService

}