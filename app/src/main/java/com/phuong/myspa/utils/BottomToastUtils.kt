package com.phuong.myspa.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class BottomToastUtils constructor(context : Context,layout:Int){
    private var toast: Toast? = null
    private  var mContext :Context? = null
    private lateinit var binding: ViewDataBinding
    private lateinit var inflater: LayoutInflater
    init {
        mContext = context
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(mContext)
        }
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layout,null, false)
        toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT)
        toast!!.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast!!.setView(binding!!.getRoot())
        toast!!.setDuration(Toast.LENGTH_SHORT)
    }

    fun showToast() {
        toast!!.show()
    }
    fun release() {
        if (instance != null) {
            instance = null
        }
    }
    companion object{
        private var instance: BottomToastUtils? = null

        fun getInstance(context: Context,layout:Int): BottomToastUtils{
            if (instance == null) {
                instance = BottomToastUtils(context,layout)
            }
            return instance!!
        }
    }
}