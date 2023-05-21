package com.phuong.myspa.ui.history

import androidx.fragment.app.viewModels
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentHistoryDetailBinding

class DetailHistoryFragment : AbsBaseFragment<FragmentHistoryDetailBinding>() {
    private val mViewModel  by viewModels<DetailHistoryViewModel>()

    override fun getLayout(): Int {
        return R.layout.fragment_history_detail

    }

    override fun initView() {
        mViewModel.getDetail()
    }
}