package com.phuong.myspa.ui.shop_service

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.shop.AddCart
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentServiceBinding
import com.phuong.myspa.ui.detail_shop.ShopViewModel
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment : AbsBaseFragment<FragmentServiceBinding>() {
    private  var shopInfor:ShopInfor? = null
    private lateinit var mViewModel: ShopViewModel
    private val mAdapter by lazy { ShopServiceAdapter() }

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shopInfor = arguments?.getParcelable(ServiceFragment.DATA, ShopInfor::class.java)
        } else {
            shopInfor = arguments?.getParcelable(ServiceFragment.DATA)
        }
        mViewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
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
                val dialogDetailService = DialogDetailService.create(item)
                dialogDetailService.show(childFragmentManager,DialogDetailService.TAG)
            }

            override fun onSelectService(item: ShopService, position: Int) {
                mViewModel.addCart(AddCart(shopInfor!!._id,item._id))
            }

        }

    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.dataLiveData.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body.data!!.service
                mAdapter.submit(body)
                mAdapter.notifyDataSetChanged()
            }
        }
        mViewModel.isSucess.observe(viewLifecycleOwner){
            if (it){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.add_to_cart))
            }
            else{
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
    }
    override fun getLayout(): Int = R.layout.fragment_service
    companion object {
        const val DATA = "KEY_DATA"
        @JvmStatic
        fun newInstance(data: ShopInfor) =
            ServiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
    }
}