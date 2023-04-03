package com.phuong.myspa.ui.detail_category

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentDetailCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCategoryFragment:AbsBaseFragment<FragmentDetailCategoryBinding>() {
    private val args :DetailCategoryFragmentArgs by navArgs()
    private val mAdapter by lazy { ShopAdapter(false) }
    private val mViewModel by viewModels<DetailCategoryViewModel>()
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 1
    private var mLayoutManager :LinearLayoutManager? = null
    override fun initView() {
        binding.toolbar.title = args.category.name
        binding.viewModel = mViewModel
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(DetailCategoryFragmentDirections.actionGlobalMainFragment())

        }
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.rvShop.setHasFixedSize(true)
        binding.rvShop.apply {
            layoutManager = mLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recyclerView.childCount
                totalItemCount = mLayoutManager!!.itemCount
                firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()
                if (dy > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    if (mViewModel.dataVM != null){
                        mViewModel.fetchData(QueryCategory(mutableListOf(args.category._id)), true)
                    }
                }
            }
        })
        }
        binding.rvShop.adapter = mAdapter
        mAdapter.listener = object :ShopAdapter.IOnItemClickShop{
            override fun onItemClick(item: ShopInfor, position: Int) {
                findNavController().navigate(DetailCategoryFragmentDirections.actionGlobalShopFragment(item))
            }

            override fun onItemMoreAction(view: View, item: ShopInfor, position: Int) {

            }

        }
        binding.swipeRefresh.setOnRefreshListener {
            mViewModel.fetchData(QueryCategory(mutableListOf(args.category._id)),false)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.fetchData(QueryCategory(mutableListOf(args.category._id)),false)
        mViewModel.dataLiveData.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                if(binding.swipeRefresh.isRefreshing){
                    binding.swipeRefresh.isRefreshing = false
                }
                mAdapter.submitList(mViewModel.getPage()>0,body)
            }
        }
    }
    override fun getLayout(): Int = R.layout.fragment_detail_category
}