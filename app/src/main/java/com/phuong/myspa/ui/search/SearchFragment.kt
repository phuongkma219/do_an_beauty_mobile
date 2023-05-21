package com.phuong.myspa.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentSearchBinding
import com.phuong.myspa.databinding.FragmentServiceBinding
import com.phuong.myspa.ui.detail_category.DetailCategoryFragmentDirections
import com.phuong.myspa.ui.detail_category.DetailCategoryViewModel
import com.phuong.myspa.ui.detail_category.ShopAdapter
import com.phuong.myspa.utils.showKeyBoard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : AbsBaseFragment<FragmentSearchBinding>() {
    private val mAdapter by lazy { ShopAdapter(false) }
    private val mViewModel by viewModels<SearchViewModel>()
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 1
    private var mLayoutManager : LinearLayoutManager? = null
    override fun getLayout(): Int  = R.layout.fragment_search

    override fun initView() {
      setupToolbar()
        binding.viewModel = mViewModel
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
                            mViewModel.fetchData( true)
                        }
                    }
                }
            })
        }
        binding.rvShop.adapter = mAdapter
        mAdapter.listener = object :ShopAdapter.IOnItemClickShop{
            override fun onItemClick(item: ShopInfor, position: Int) {
                findNavController().navigate(DetailCategoryFragmentDirections.actionGlobalShopFragment(item._id))
            }

            override fun onItemMoreAction(view: View, item: ShopInfor, position: Int) {

            }

        }
    }

    override fun initViewModel() {
        super.initViewModel()

        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
               if (body != null){
                   mAdapter.submitList(mViewModel.getPage()>0,body)
               }
                if (isBindingInitialized){
                    binding.searchView.clearFocus()
                }
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                if (isBindingInitialized){
                    binding.searchView.clearFocus()
                }
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvShop.visibility = View.VISIBLE
                mViewModel.dataVM = null
                mViewModel.fetchData(false)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")){
                    binding.rvShop.visibility = View.INVISIBLE
                    mAdapter.clearData()
                }
                newText?.let { mViewModel.setKeyword(newText.trim()) }
                return false
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.requestFocus()
        binding.searchView.setOnQueryTextFocusChangeListener { view, b ->
            if (b){
                showKeyBoard(requireContext(),view.findFocus())
            }
        }
    }

}