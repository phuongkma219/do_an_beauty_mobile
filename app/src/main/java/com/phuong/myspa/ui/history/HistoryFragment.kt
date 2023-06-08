package com.phuong.myspa.ui.history

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentCartBinding
import com.phuong.myspa.databinding.FragmentHistoryBinding
import com.phuong.myspa.ui.cart.CartAdapter
import com.phuong.myspa.ui.cart.CartViewModel
import com.phuong.myspa.ui.detail_shop.ShopFragmentDirections
import com.phuong.myspa.ui.detail_shop.ShopViewModel
import com.phuong.myspa.ui.shop_service.DialogDetailService
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment: AbsBaseFragment<FragmentHistoryBinding>() {
    private val mViewModel  by viewModels<HistoryViewModel>()
    private val shopViewModel  by viewModels<ShopViewModel>()
    private val mAdapter  by lazy { HistoryAdapter().apply {
        listener = object : HistoryAdapter.ItemHistoryListener{
            override fun onClickItem(position: Int,item: History) {
                findNavController().navigate(HistoryFragmentDirections.actionGlobalDetailHistoryFragment(item))
            }

            override fun onDeleteItem(position: Int,item: History) {
            }

            override fun onBuyItem(position: Int,item: History) {
                shopViewModel.addCart(CartDTO(item.service.shop_id,item.service._id))

            }


        }
    } }
    override fun getLayout(): Int {
        return  R.layout.fragment_history
    }

    override fun initView() {
        binding.viewModel = mViewModel
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        mViewModel.getListHistory()
        binding.rvCart.adapter = mAdapter
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun initViewModel() {
        super.initViewModel()
        mViewModel.dataLiveData.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                mAdapter.submit(body)
            }
        }
        mViewModel.isDelete.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.delete_success))
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
        shopViewModel.isSucess.observe(this){
            if (it){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.add_to_cart))
                findNavController().navigate(HistoryFragmentDirections.actionGlobalCartFragment())
            }
            else{
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }

    }

}