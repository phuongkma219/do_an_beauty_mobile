package com.phuong.myspa.ui.cart

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentCartBinding
import com.phuong.myspa.ui.comment.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment:AbsBaseFragment<FragmentCartBinding>() {
    private val mViewModel  by viewModels<CartViewModel>()
    private val mAdapter by lazy { CartAdapter() }
    override fun getLayout(): Int {
        return  R.layout.fragment_cart
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        mViewModel.getListCart()
        binding.rvCart.adapter = mAdapter
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.listCart.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                mAdapter.submit(body)
            }
        }
    }
}