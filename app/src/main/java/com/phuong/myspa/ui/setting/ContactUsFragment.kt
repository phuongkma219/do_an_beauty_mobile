package com.phuong.myspa.ui.setting

import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentContactUsBinding

class ContactUsFragment : AbsBaseFragment<FragmentContactUsBinding>() {
    override fun getLayout(): Int = R.layout.fragment_contact_us

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}