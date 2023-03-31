package com.phuong.soundeditor23.di

import android.content.Context
import com.phuong.myspa.MyApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {
    @AppContext
    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context) = context as MyApp



}