package com.phuong.myspa.ui.shop_service

import android.os.Build
import android.os.Bundle
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseFullDialog
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.databinding.DialogShopServiceBinding
import com.phuong.myspa.utils.loadImageFromUrl
import com.phuong.myspa.utils.setPrice
import com.phuong.myspa.utils.setTime

class DialogDetailService:BaseFullDialog<DialogShopServiceBinding>() {
    private var shopService:ShopService? = null
    override fun initView() {
        shopService = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(KEY_DATA, ShopService::class.java)
        } else {
            arguments?.getParcelable(KEY_DATA)
        }
        if (shopService == null) {
            dismiss()
        }
            binding!!.item = shopService
        binding!!.ivClose.setOnClickListener { dismiss() }

    }

    override fun getLayout(): Int = R.layout.dialog_shop_service

    override fun onDismiss() {
    }
    companion object{
        private const val KEY_DATA = "Data"
        const val TAG = "FadeEditorBottomSheet"
        fun create(shop:ShopService): DialogDetailService {
            val dialog = DialogDetailService()
            val bundle = Bundle()
            bundle.putParcelable(KEY_DATA, shop)
            dialog.arguments = bundle
            return dialog
        }
    }
}