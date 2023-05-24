package com.phuong.myspa.ui.dialog

import android.os.Bundle
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.databinding.DialogConfirmPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogConfirmPayment :   BaseDialog<DialogConfirmPaymentBinding>()  {
    override fun initViewModel() {

    }

    override fun initView() {
        arguments?.apply {
            getString(Key_Content) ?.let { binding.tvPrice.text= it }}
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnOk.setOnClickListener {
            (listener as IConfirmSave).onPositive()
            dismiss()
        }
    }

    override fun getLayout(): Int {
    return  R.layout.dialog_confirm_payment
    }
    interface IConfirmSave : BaseListener {
        fun onExit()
        fun onPositive()

    }
    companion object{
        const val TAG = "DialogConfirmSave"
        private val bundle = Bundle()
        private val Key_Content ="Content"
        fun show(): DialogConfirmPayment {
            val dialog = DialogConfirmPayment()
            dialog.arguments= bundle
            return dialog
        }

        fun setContent(content:String):Companion{
            bundle.putString(Key_Content,content)
            return this
        }

    }

}