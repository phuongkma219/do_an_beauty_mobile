package com.phuong.myspa.ui.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.Advertisement
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentHomeBinding
import com.phuong.myspa.ui.cart.CartFragmentDirections
import com.phuong.myspa.ui.detail_category.DetailCategoryFragmentDirections
import com.phuong.myspa.ui.search.SearchFragmentDirections
import com.phuong.myspa.ui.setting.SettingFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class HomeFragment:AbsBaseFragment<FragmentHomeBinding>() {
    private val mAdapter by lazy { HomeAdapter() }
    private val mViewModel by viewModels<HomeViewModel>()

    override fun initView() {
        binding.viewModel = mViewModel
        binding.rvCategory.adapter = mAdapter
        val manager = GridLayoutManager(requireContext(), COLUMNS, RecyclerView.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position <1) {
                    COLUMNS
                } else {
                    1
                }
            }

        }
        binding.rvCategory.layoutManager = manager
        mAdapter.inner = object :HomeAdapter.IOnClickItem{
            override fun onClickItemBanner(item: Advertisement) {

            }



            override fun onClickItemCategory(item: Category, position: Int) {
                findNavController()
                    .navigate(DetailCategoryFragmentDirections.actionGlobalDetailCategoryFragment(item))

            }

        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionGlobalSearchFragment())
        }
binding.rlCartHome.setOnClickListener {
    findNavController().navigate(CartFragmentDirections.actionGlobalCartFragment())

}

    }


    override fun initViewModel() {
        super.initViewModel()
        mViewModel.getListCategory()
        binding.layoutNoInternet.btRetry.setOnClickListener {
            mViewModel.getListCategory()
        }
        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                mAdapter.submitData(body.data)
            }
        }
    }
    override fun getLayout(): Int = R.layout.fragment_home
    companion object{
        private val COLUMNS = 2
    }
}