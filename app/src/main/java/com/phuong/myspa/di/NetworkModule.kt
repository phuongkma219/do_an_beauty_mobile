package com.phuong.myspa.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.utils.Constants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
     fun provideHttpLoggingInterceptor():HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
      return interceptor
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor):OkHttpClient{
        val builder = OkHttpClient.Builder()
            .writeTimeout(6 * 1000.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(6 * 1000.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }
    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        GsonBuilder().setLenient().setLenient()
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @Named("BeautyBE")
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient).build()
    }
    

    @Provides
    fun provideRemoteAPI(@Named("BeautyBE")  retrofit: Retrofit): RemoteServices {
        return retrofit.create(RemoteServices::class.java)
    }

    @Provides
    fun provideCompletableJob(): CompletableJob = Job()
}