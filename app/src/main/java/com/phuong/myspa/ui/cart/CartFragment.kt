package com.phuong.myspa.ui.cart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paypal.android.sdk.payments.*
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentCartBinding
import com.phuong.myspa.ui.detail_shop.ShopFragmentDirections
import com.phuong.myspa.ui.dialog.DialogConfirmPayment
import com.phuong.myspa.ui.shop_service.DialogDetailService
import com.phuong.myspa.utils.BottomToastUtils
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SnapbarBottom
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.ViewComponentManager
import java.math.BigDecimal


@AndroidEntryPoint
class CartFragment:AbsBaseFragment<FragmentCartBinding>() {
//    private val mViewModel  by viewModels<CartViewModel>()
    private lateinit var mViewModel : CartViewModel
    private val mAdapter  by lazy { CartAdapter() }
    private var totalPrice =0
    private lateinit var configuration: PayPalConfiguration
    override fun getLayout(): Int {
        return  R.layout.fragment_cart
    }



    override fun initView() {
        configuration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Constants.CLIENT_ID)
        mViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
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
                    .setContent(mAdapter.mapToService(mAdapter.liveSelect.value!![0])).show()
                   dialogConfirmSave.listener = object : DialogConfirmPayment.IConfirmSave{
                       override fun onExit() {
                           totalPrice = 0
                       }

                       override fun onPositive() {
                           val payment = PayPalPayment(
                               BigDecimal(java.lang.String.valueOf(1)),"USD","Thanh toan",
                               PayPalPayment.PAYMENT_INTENT_SALE)
                           val intent = Intent(requireContext(), PaymentActivity::class.java)
                           intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration)
                           intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
                           resultLauncher.launch(intent)
                       }

                   }
                dialogConfirmSave.show(childFragmentManager, DialogConfirmPayment.TAG)

            }
        }
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val snapbarBottom = SnapbarBottom(requireContext())

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val pay : PaymentConfirmation? = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
            if (pay != null){
                try {
                    val json = pay!!.toJSONObject().toString()
                    snapbarBottom.showToast(SnapbarBottom.MessageType.Success
                        ,resources.getString(R.string.success),resources.getString(R.string.payment_successfull))

                    mViewModel.addHistory(mAdapter.liveSelect.value!!)

                }catch (e:Exception){
                  e.printStackTrace()
                    snapbarBottom.showToast(SnapbarBottom.MessageType.Error
                        ,resources.getString(R.string.payment_failed),resources.getString(R.string.payment_successfull))
                }
            }

        }
        else if(result.resultCode == Activity.RESULT_CANCELED){
            snapbarBottom.showToast(SnapbarBottom.MessageType.Error
                ,resources.getString(R.string.payment_failed),resources.getString(R.string.error_pro_payment))
        }
        else if(result.resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            snapbarBottom.showToast(SnapbarBottom.MessageType.Error
                ,resources.getString(R.string.payment_failed),resources.getString(R.string.error_pro_payment))
        }
    }
    override fun onDestroy() {
        requireActivity().stopService(Intent(requireContext(), PayPalService::class.java))
        super.onDestroy()
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
    private fun activityContext(context: Context): Context {
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        }
        else context
    }




}