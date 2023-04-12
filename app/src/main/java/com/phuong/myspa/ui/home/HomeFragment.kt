package com.phuong.myspa.ui.home

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.Advertisement
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentHomeBinding
import com.phuong.myspa.ui.detail_category.DetailCategoryFragmentDirections
import com.phuong.myspa.ui.main.MainFragment
import com.phuong.myspa.ui.reminder.ReminderFragmentDirections
import com.phuong.myspa.ui.setting.SettingFragmentDirections
import com.phuong.myspa.utils.CacheUtils
import com.phuong.myspa.utils.DataUtils
import dagger.hilt.android.AndroidEntryPoint

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
            findNavController().navigate(SettingFragmentDirections.actionGlobalSearchFragment())
        }
        binding.ivReminder.setOnClickListener {
            findNavController().navigate(ReminderFragmentDirections.actionGlobalReminderFragment())

        }

    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.getListCategory()
        binding.layoutNoInternet.btRetry.setOnClickListener {
            mViewModel.getListCategory()
        }
        mViewModel.dataLiveData.observe(viewLifecycleOwner){
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