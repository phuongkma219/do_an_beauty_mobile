package com.phuong.myspa.base

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.phuong.myspa.ui.activity.MainActivity

abstract class BaseBottomSheetDialogFragment<VB : ViewDataBinding> : BottomSheetDialogFragment() {
    protected lateinit var binding: VB
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    protected val activityOwner by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomsheet = super.onCreateDialog(savedInstanceState)
        val view = View.inflate(requireActivity(), getLayoutId(), null)
        binding = DataBindingUtil.bind(view)!!
        bottomsheet.setContentView(view)
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        initBinding()
        return bottomsheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onDismiss()
            }
            true
        }
        dialog!!.setCancelable(false)
    }


    abstract fun onDismiss()
    abstract fun getLayoutId(): Int
    open fun initBinding() {}
}