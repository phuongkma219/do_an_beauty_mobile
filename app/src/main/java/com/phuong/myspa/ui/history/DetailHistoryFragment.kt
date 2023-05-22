package com.phuong.myspa.ui.history

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentHistoryDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailHistoryFragment : AbsBaseFragment<FragmentHistoryDetailBinding>() {
    private val mViewModel  by viewModels<DetailHistoryViewModel>()
    private val args : DetailHistoryFragmentArgs by navArgs()

    override fun getLayout(): Int {
        return R.layout.fragment_history_detail

    }

    override fun initView() {
        mViewModel.getHistoryDetail(args.history._id, HistoryDTO(args.history.service.shop_id,args.history.service._id))
        mViewModel.historyLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                binding.item = body
            }
        }
    }
}