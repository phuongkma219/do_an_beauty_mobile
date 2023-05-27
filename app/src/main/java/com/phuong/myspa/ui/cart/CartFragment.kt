package com.phuong.myspa.ui.cart

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentCartBinding
import com.phuong.myspa.ui.activity.MainActivity
import com.phuong.myspa.ui.detail_shop.ShopFragmentDirections
import com.phuong.myspa.ui.dialog.DialogConfirmPayment
import com.phuong.myspa.ui.shop_service.DialogDetailService
import com.phuong.myspa.utils.ShareViewModel
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.zalo.AppInfo
import com.phuong.myspa.utils.zalo.CreateOrder
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener


@AndroidEntryPoint
class CartFragment:AbsBaseFragment<FragmentCartBinding>() {
    private val mViewModel  by viewModels<CartViewModel>()
    private val mAdapter  by lazy { CartAdapter() }
    private var totalPrice =0
    ///
    private var amount: String? = "10000"
    private val fee = "0"
    var environment = 0 //developer default

    private val merchantName = "Demo SDK"
    private val merchantCode = "SCB01"
    private val merchantNameLabel = "Nhà cung cấp"
    private val description = "Thanh toán dịch vụ ABC"
    override fun getLayout(): Int {
        return  R.layout.fragment_cart
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        mViewModel.getListCart()
        binding.rvCart.adapter = mAdapter
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        mAdapter.inner = object :CartAdapter.ItemCartListener{
            override fun onClickItemService(item: DataModel.DataItem) {
                val dialogDetailService = DialogDetailService.create(item._id,false)
                dialogDetailService.show(childFragmentManager, DialogDetailService.TAG)
            }

            override fun onDeleteItem(item: DataModel.DataItem) {
                mAdapter.removeItem(item)
                mViewModel.deleteCart(CartDTO(item.shop_id,item._id))
            }

            override fun onClickTitle(item: DataModel.DataHeader) {
                val shop = ShopInfor(0,item._id,item.address,item.avatar,
                    item.category,item.created_at,item.description,item.email,item.end_time
                    ,item.name,item.phone_number,item.rate,item.start_time,item.updated_at)
              findNavController().navigate(ShopFragmentDirections.actionGlobalShopFragment(shop))
            }

        }
        initViewOnClick()
    }
    private fun initViewOnClick(){
        mAdapter.liveSelect.observe(this){
            var sumPrice = 0
            it.forEach {
                sumPrice += it.price.toInt()
            }
            totalPrice =sumPrice
            binding.tvSumPrice.text = getPrice(totalPrice.toString())
        }
        binding.btnBuy.setOnClickListener {
            if (mAdapter.liveSelect.value?.isNotEmpty() == true){
                val dialogConfirmSave = DialogConfirmPayment
                    .setContent(getPrice(totalPrice.toString())).show()
                   dialogConfirmSave.listener = object : DialogConfirmPayment.IConfirmSave{
                       override fun onExit() {
                           totalPrice = 0
                       }

                       override fun onPositive() {
                            val amount = 10000.toString()
                           val orderApi = CreateOrder()
                           try {
                               val data: JSONObject = orderApi.createOrder(amount)
                               val code = data.getString("returncode")
                               if (code == "1") {
                                   val token = data.getString("zptranstoken")
                                   ZaloPaySDK.getInstance().payOrder(
                                       requireActivity(),
                                       token,
                                       "demozpdk://app",
                                       object : PayOrderListener {
                                           override fun onPaymentSucceeded(
                                               transactionId: String,
                                               transToken: String,
                                               appTransID: String
                                           ) {
                                               mViewModel.addHistory(mAdapter.liveSelect.value!!)

                                               Toast.makeText(
                                                   requireContext(),
                                                   "Thanh toán thành công",
                                                   Toast.LENGTH_SHORT
                                               ).show()
                                           }

                                           override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                                               Toast.makeText(
                                                   requireContext(),
                                                   "Thanh toán bị hủy",
                                                   Toast.LENGTH_SHORT
                                               ).show()
                                           }

                                           override fun onPaymentError(
                                               zaloPayError: ZaloPayError,
                                               zpTransToken: String,
                                               appTransID: String
                                           ) {
                                               Toast.makeText(
                                                   requireContext(),
                                                   "Thanh toán thất bại",
                                                   Toast.LENGTH_SHORT
                                               ).show()
                                           }
                                       })
                               }
                           } catch (e: Exception) {
                               e.printStackTrace()
                           }
                       }

                   }
                dialogConfirmSave.show(childFragmentManager, DialogConfirmPayment.TAG)

            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.listCart.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                mAdapter.submit(body)
            }
        }
        mViewModel.isDelete.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.delete_success))
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
        mViewModel.liveDataAddHistory.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                mViewModel.deleteListCart(mAdapter.liveSelect.value!!)
                mAdapter.clearAll()
                findNavController().navigate(CartFragmentDirections.actionGlobalHistoryFragment())
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
        ShareViewModel.getInstance((requireContext() as MainActivity).application).get.observe(this){
            ZaloPaySDK.getInstance().onResult(it)

        }
    }
    private fun getPrice(time:String) : String{
        var price = ""
        val test = time.reversed()
        for (i in test.length-1 downTo 0) {
            if (i%3 ==0 && i != 0){
                price += "${test[i]}."
            }
            else{
                price += test[i]
            }
        }
        return price + " VND"
    }


}