package com.phuong.myspa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phuong.myspa.base.BaseActivity
import com.phuong.myspa.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getFragmentID(): Int = R.id.navContainerViewMain

    override fun getLayoutId(): Int  = R.layout.activity_main
}