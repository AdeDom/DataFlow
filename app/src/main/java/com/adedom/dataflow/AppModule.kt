package com.adedom.dataflow

import android.content.Context
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
    fun provideSharedPref(@ApplicationContext context: Context): SharedPref =
        SharedPrefImpl(context)

    @Singleton
    @Provides
    fun provideDefaultRepository(sharedPref: SharedPref): DefaultRepository =
        DefaultRepositoryImpl(sharedPref)
}