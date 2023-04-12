package com.phuong.myspa

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.text.layoutDirection
import com.phuong.myspa.base.BaseActivity
import com.phuong.myspa.databinding.ActivityMainBinding
import com.phuong.myspa.utils.RuntimeLocaleChanger
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Will give the direction of the layout depending of the Locale you've just set
        window.decorView.layoutDirection = Locale.getDefault().layoutDirection
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(RuntimeLocaleChanger.wrapContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        RuntimeLocaleChanger.overrideLocale(this)
    }
    override fun getFragmentID(): Int = R.id.navContainerViewMain

    override fun getLayoutId(): Int  = R.layout.activity_main
}