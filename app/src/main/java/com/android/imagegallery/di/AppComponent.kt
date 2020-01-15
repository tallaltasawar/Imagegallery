package com.android.imagegallery.di

import android.app.Application
import com.android.imagegallery.GalleryApplication
import com.android.imagegallery.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        ServiceBuilderModule::class,
        ViewModelModule::class,
        ActivityBindingModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(galleryApplication: GalleryApplication)
}
