package com.phuong.myspa.ui.favorite

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentFavouriteBinding
import com.phuong.myspa.ui.detail_category.DetailCategoryFragmentDirections
import com.phuong.myspa.ui.detail_category.ShopAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : AbsBaseFragment<FragmentFavouriteBinding>(){
    private val mViewModel by viewModels<FavoriteViewModel>()
    private val mAdapter by lazy { ShopAdapter(true) }

    override fun getLayout(): Int = R.layout.fragment_favourite

    override fun initView() {
        binding.rvShop.adapter = mAdapter
        binding.rvShop.layoutManager = LinearLayoutManager(requireContext())
        mAdapter.listener = object : ShopAdapter.IOnItemClickShop{
            override fun onItemClick(item: ShopInfor, position: Int) {
                findNavController().navigate(DetailCategoryFragmentDirections.actionGlobalShopFragment(item))
            }

            override fun onItemMoreAction(view: View, item: ShopInfor, position: Int) {

            }

        }
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.viewModel = mViewModel
        mViewModel.getListFavorite()
        mViewModel.dataLiveData.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                mAdapter.submit(body)
            }
        }
    }

}