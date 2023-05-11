package com.phuong.myspa.ui.cart

import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentCartBinding

class CartFragment:AbsBaseFragment<FragmentCartBinding>() {
    override fun getLayout(): Int {
        return  R.layout.fragment_cart
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}