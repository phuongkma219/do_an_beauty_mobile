package com.phuong.myspa

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.phuong.myspa.base.BaseResource
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application() {
    private val TAG = "MyApp"

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var resource: BaseResource
        private lateinit var app: Context
        fun resource() = resource
        fun getApplication() = app
    }

    override fun onCreate() {
        super.onCreate()
        resource = BaseResource(applicationContext)
        app = applicationContext
    }


}