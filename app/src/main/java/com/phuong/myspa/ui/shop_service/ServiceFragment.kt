package com.phuong.myspa.ui.shop_service

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentServiceBinding
import com.phuong.myspa.ui.comment.CommentFragment
import com.phuong.myspa.ui.detail_shop.ShopViewModel
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : AbsBaseFragment<FragmentServiceBinding>() {
    private var shopId: String? = null
    private lateinit var mViewModel: ShopViewModel
    private val mAdapter by lazy { ShopServiceAdapter() }

    override fun initView() {
        shopId = arguments?.getString(CommentFragment.DATA)

        mViewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        mViewModel.getDetailShop(shopId!!)
        binding.rcService.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            addItemDecoration(
                VerticalSpaceItemDecoration(
                    requireContext().resources.getDimensionPixelSize(R.dimen.dp_8),
                    requireContext().resources.getDimensionPixelSize(R.dimen.dp_8)
                )
            )
        }
        mAdapter.listener = object : ShopServiceAdapter.IOnItemClickService {
            override fun onItemClick(item: ShopService, position: Int) {
                val dialogDetailService = DialogDetailService.create(item._id)
                dialogDetailService.show(childFragmentManager,DialogDetailService.TAG)
            }

        }

    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body.data!!.service
                mAdapter.submit(body)
                mAdapter.notifyDataSetChanged()
            }
        }

    }
    override fun getLayout(): Int = R.layout.fragment_service
    companion object {
        const val DATA = "KEY_DATA"
        @JvmStatic
        fun newInstance(shopId:String) =
            ServiceFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA, shopId)
                }
            }
    }
}