package com.android.imagegallery.di.module

import android.content.Context
import com.android.imagegallery.preference.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideSharedPreferenceManager(): SharedPreferenceManager {
        val sharedPreferences = context.getSharedPreferences(
            SharedPreferenceManager.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return SharedPreferenceManager(sharedPreferences)
    }
}
