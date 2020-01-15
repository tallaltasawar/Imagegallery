package com.android.imagegallery.di.module

import com.android.imagegallery.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("unused")
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityFragmentBuilder::class])
    abstract fun contributeMainActivity(): MainActivity
}