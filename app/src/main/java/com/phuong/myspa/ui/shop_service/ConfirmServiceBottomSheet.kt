package com.phuong.myspa.ui.shop_service

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseBottomSheetDialogFragment
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.databinding.BottomsheetConfirmServiceBinding
import com.phuong.myspa.ui.detail_category.DetailCategoryViewModel
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class ConfirmServiceBottomSheet:BaseBottomSheetDialogFragment<BottomsheetConfirmServiceBinding>() {
    private var hours :Int? = null
    private var minute :Int? = null
    private var day:Int? = null
    private var month:Int? = null
    private var shopInfor:ShopInfor? = null
    private var shopService:ShopService? = null
    override fun initBinding() {
        super.initBinding()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shopInfor = arguments?.getParcelable(ConfirmServiceBottomSheet.DATA, ShopInfor::class.java)
            shopService = arguments?.getParcelable(ConfirmServiceBottomSheet.KEY_DATA, ShopService::class.java)
        } else {
            shopInfor = arguments?.getParcelable(ConfirmServiceBottomSheet.DATA)
            shopService =arguments?.getParcelable(ConfirmServiceBottomSheet.KEY_DATA)
        }
        binding.rlDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                this.day = dayOfMonth
                this.month = monthOfYear
                // Display Selected date in textbox
                binding.tvDate.text = dayOfMonth.toString() + "/" + (monthOfYear +1) + "/" + year

            }, year, month, day)
            dpd.datePicker.minDate = c.timeInMillis

            dpd.show()
        }
        binding.rlTime.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                hours = hour
                this.minute = minute
              binding.tvTime.text =  SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

        }
        binding.rlAddress.setOnClickListener {
            if (Utils.locationPermissionGrant(requireContext())){
                showDialog()
            }else{
                requestStoragePermission()
            }
        }
        binding.btnConfirm.setOnClickListener {
            if (hours== null|| minute == null){
                ToastUtils.getInstance(requireContext()).showToast("Select time")
            }
            if (day == null || month == null){
                ToastUtils.getInstance(requireContext()).showToast("Select day")
            }
            if (hours!= null && minute!= null &&day != null && month != null){
                val content = shopInfor?.name +" : "+ shopService?.name
                Utils.startAlarm(requireContext(),minute!!,hours!!,day!!,month!!,content)

            }
            else{
                ToastUtils.getInstance(requireContext()).showToast("Error")

            }

        }
    }
    override fun getLayoutId(): Int = R.layout.bottomsheet_confirm_service
    override fun onDismiss() {

    }
    private fun showDialog(){
        val dialog = GetAddressFragment()
        dialog.listener = object :GetAddressFragment.ISaveAddress{
            override fun getAddress(address: String) {
                binding.tvAddress.text = address
            }

        }
        dialog.show(childFragmentManager,"GetAddressFragment")
    }
    private fun requestStoragePermission() {
        resultLauncherRecord.launch(
            Utils.getLocationPermissions()
        )
    }

    private val resultLauncherRecord =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (Utils.locationPermissionGrant(requireContext())) {
                showDialog()
            } else {

                Utils.showAlertPermissionNotGrant(binding.root, requireActivity())
            }
        }
    companion object{
        const val KEY_DATA ="KEY_DATA"
        const val DATA="DATA"
         const val TAG = "ConfirmServiceBottomShe"
        fun create(shopService:ShopService,shopInfor: ShopInfor): ConfirmServiceBottomSheet {
            val dialog = ConfirmServiceBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(KEY_DATA, shopService)
            bundle.putParcelable(DATA,shopInfor)
            dialog.arguments = bundle
            return dialog
        }
    }
}