package com.phuong.myspa.ui.shop_service

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseFullDialog
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.DialogShopServiceBinding
import com.phuong.myspa.ui.detail_shop.ShopViewModel
import com.phuong.myspa.ui.login.LoginViewModel
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.loadImageFromUrl
import com.phuong.myspa.utils.setPrice
import com.phuong.myspa.utils.setTime
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class DialogDetailService:BaseFullDialog<DialogShopServiceBinding>() {
    private val mViewModel by viewModels<ShopViewModel>()
    private var serviceId : String? = null
    override fun initView() {

        serviceId =    arguments?.getString(KEY_DATA)
        val isShow = arguments?.getBoolean(KEY_SHOW)
        if (serviceId == null) {
            dismiss()
        }
        if (isShow == false){
            binding!!.btnAddCart.visibility = View.INVISIBLE
        }

        mViewModel.getDetailService(serviceId!!)
        binding!!.ivClose.setOnClickListener { dismiss() }
        binding!!.btnAddCart.setOnClickListener {
            mViewModel.addCart(CartDTO(binding!!.item!!.shop_id,binding!!.item!!._id))
        }
    mViewModel.serviceLiveData.observe(this){
        if (it.loadingStatus == LoadingStatus.Success){
            val body = (it as DataResponse.DataSuccess)?.body
            if (body!= null){
                binding!!.item = body

            }
            binding!!.layoutLoading.root.visibility = View.INVISIBLE
        }
        else {
            binding!!.layoutLoading.root.visibility = View.VISIBLE
        }
    }
        mViewModel.isSucess.observe(this){
            if (it){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.add_to_cart))
            }
            else{
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
    }

    override fun getLayout(): Int = R.layout.dialog_shop_service

    override fun onDismiss() {
    }
    companion object{
        private const val KEY_DATA = "Data"
        private const val KEY_SHOW = "SHOW"
        const val TAG = "FadeEditorBottomSheet"
        fun create(id:String,showBtn:Boolean= true): DialogDetailService {
            val dialog = DialogDetailService()
            val bundle = Bundle()
            bundle.putString(KEY_DATA, id)
            bundle.putBoolean(KEY_SHOW,showBtn)
            dialog.arguments = bundle
            return dialog
        }
    }
}