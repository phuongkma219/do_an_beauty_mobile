package com.phuong.myspa

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.phuong.myspa.base.BaseResource
import com.phuong.myspa.utils.RuntimeLocaleChanger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApp : Application()  {
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