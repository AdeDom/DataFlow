package com.adedom.dataflow

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("SharedPreferencesFile", Activity.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSharedPref(sharedPreferences: SharedPreferences): SharedPref =
        SharedPrefImpl(sharedPreferences)

    @Singleton
    @Provides
    fun provideDefaultRepository(sharedPref: SharedPref): DefaultRepository =
        DefaultRepositoryImpl(sharedPref)
}