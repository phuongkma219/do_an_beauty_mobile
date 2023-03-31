package com.phuong.myspa

import android.annotation.SuppressLint
import android.app.Application
import com.phuong.myspa.base.BaseResource
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application() {
    private val TAG = "MyApp"

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var resource: BaseResource
        fun resource() = resource
    }

    override fun onCreate() {
        super.onCreate()
        resource = BaseResource(applicationContext)
    }
}