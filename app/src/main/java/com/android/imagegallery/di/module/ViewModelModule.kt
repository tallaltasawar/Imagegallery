package com.android.imagegallery.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.imagegallery.di.ViewModelKey
import com.android.imagegallery.ui.MainViewModel
import com.android.imagegallery.vm.GalleryViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: GalleryViewModelFactory): ViewModelProvider.Factory
}
