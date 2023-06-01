package com.phuong.myspa.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.text.layoutDirection
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseActivity
import com.phuong.myspa.databinding.ActivityMainBinding
import com.phuong.myspa.utils.RuntimeLocaleChanger
import com.phuong.myspa.utils.ShareViewModel
import dagger.hilt.android.AndroidEntryPoint
import vn.momo.momo_partner.AppMoMoLib
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)

    }
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
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ShareViewModel.getInstance(this.application).set(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    val token = data.getStringExtra("data") //Token response
                    val phoneNumber = data.getStringExtra("phonenumber")
                    var env = data.getStringExtra("env")
                    if (env == null) {
                        env = "app"
                    }
                    if (token != null && token != "") {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                      ShareViewModel.getInstance(this.application).setSucces(resources.getString(R.string.not_receive_info))
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    val message =
                        if (data.getStringExtra("message") != null) {
                            data.getStringExtra("message")
                        } else "Thất bại"
                    ShareViewModel.getInstance(this.application).setErr(message!!)

                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    ShareViewModel.getInstance(this.application).setErr(resources.getString(R.string.not_receive_info))
                } else {
                    //TOKEN FAIL
                    ShareViewModel.getInstance(this.application).setErr(resources.getString(R.string.not_receive_info))
                }
            } else {
                ShareViewModel.getInstance(this.application).setErr(resources.getString(R.string.not_receive_info))
            }
        } else {
            ShareViewModel.getInstance(this.application).setErr(resources.getString(R.string.not_receive_info))
        }
    }
}