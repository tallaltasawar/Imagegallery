package com.android.imagegallery.di.module

import com.android.imagegallery.ui.fragment.ImageListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class MainActivityFragmentBuilder {

    @ContributesAndroidInjector
    abstract fun contributeImageListFragment(): ImageListFragment

}