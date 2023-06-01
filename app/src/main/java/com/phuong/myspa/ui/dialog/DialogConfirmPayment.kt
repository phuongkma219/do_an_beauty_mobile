package com.phuong.myspa.ui.dialog

import android.os.Build
import android.os.Bundle
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseBottomSheetDialogFragment
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.databinding.DialogConfirmPaymentBinding
import com.phuong.myspa.ui.detail_shop.DetailShopFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogConfirmPayment :   BaseBottomSheetDialogFragment<DialogConfirmPaymentBinding>()  {

    lateinit var listener : IConfirmSave
    override fun initBinding() {
        super.initBinding()
        var data:ShopService? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = arguments?.getParcelable(Key_Content, ShopService::class.java)
        } else {
            data = arguments?.getParcelable(Key_Content)
        }
        data?.let { binding.item = it }
        binding.btnPay.setOnClickListener {
            dismiss()
        }
        binding.btnPay.setOnClickListener {
            listener.onPositive()
            dismiss()
        }
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

        fun setContent(shopService: ShopService):Companion{
            bundle.putParcelable(Key_Content,shopService)
            return this
        }

    }

    override fun onDismiss() {

    }

    override fun getLayoutId(): Int {
        return  R.layout.dialog_confirm_payment

    }

}