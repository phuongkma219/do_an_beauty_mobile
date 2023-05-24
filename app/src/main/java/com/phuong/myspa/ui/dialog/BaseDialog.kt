package com.phuong.myspa.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.MainActivity
import com.phuong.myspa.utils.Utils

abstract class BaseDialog<V: ViewDataBinding> : DialogFragment() {

    protected lateinit var binding: V
    private val isBindingInitialized get() = this::binding.isInitialized
      var listener: BaseListener? = null
    protected val mainActivity by lazy {
        (requireActivity() as MainActivity)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.lifecycleOwner = this
       if (listener!= null){
           initView()
       }else{
           dismiss()
       }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            (Utils.getScreenWidth() * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isBindingInitialized){
            binding.unbind()
        }
    }

    abstract fun initViewModel()

    abstract fun initView(

    )

    abstract fun getLayout() : Int
}