package com.phuong.myspa.ui.report

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.comment.Content
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.databinding.FragmentReportBinding
import com.phuong.myspa.ui.detail_shop.ShopFragmentArgs
import com.phuong.myspa.ui.detail_shop.ShopViewModel
import com.phuong.myspa.utils.DataUtils
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : AbsBaseFragment<FragmentReportBinding>(){
    private val mAdapter by lazy { ReportAdapter() }
    private val args : ReportFragmentArgs by navArgs()

    private val mViewModel by viewModels<ShopViewModel>()
    override fun getLayout(): Int {
        return R.layout.fragment_report
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
       binding.rcReport.layoutManager = LinearLayoutManager(requireContext())
        binding.rcReport.adapter = mAdapter
        mAdapter.submit(DataUtils.listReport)
        mAdapter.listener = object :ReportAdapter.ISelect{
            override fun onSelectItem(position: Int, item: Content) {
                mAdapter.cleanAll()
                mAdapter.select(item)
            }
        }
        mAdapter.liveSelect.observe(this){
            if(!it.empty()){
                binding.edtContent.setText( it[0].text)

            }
        }
        binding.btnSend.setOnClickListener {
        val content = binding.edtContent.text?.toString()?.trim()
            if (content != null){
                mViewModel.postReport(UploadComment(args.shopID,Content(text = content)))
            }
        }
        mViewModel.isReport.observe(this){
            if (it){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.report_success))
                findNavController().navigateUp()
            }
            else{
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
    }
}