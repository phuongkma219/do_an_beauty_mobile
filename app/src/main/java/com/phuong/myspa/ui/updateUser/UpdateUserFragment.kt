package com.phuong.myspa.ui.updateUser

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.user.User
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentUpdateUserBinding
import com.phuong.myspa.ui.main.MainFragmentDirections
import com.phuong.myspa.ui.pickphoto.PickPhotoDialog
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.Utils
import com.phuong.myspa.utils.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateUserFragment:AbsBaseFragment<FragmentUpdateUserBinding>() {
private val mViewModel by viewModels<UpdateUserViewModel>()
    private var gender:String =""
    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivAvatar.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Change Avatar")
            alertDialog.setMessage("Do you want to confirm avatar change?")
            alertDialog.setNegativeButton("No"
                ) { p0, p1 ->
                    p0.dismiss()
                }
            alertDialog.setPositiveButton("Yes"
            ) { p0, p1 ->
                checkAndOpenPickDialog()
            }
            alertDialog.show()
        }
        binding.edtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutPhone.isErrorEnabled = true
                    binding.textLayoutPhone.error = getString(R.string.plase_enter_your_phone_number)
                }
                else{
                    if (s.length!= 10){
                        binding.textLayoutPhone.isErrorEnabled = true
                        binding.textLayoutPhone.error =getString(R.string.mobile_number_must_have_10_digits)
                    }
                    else{
                        binding.textLayoutPhone.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutFirstName.isErrorEnabled = true
                    binding.textLayoutFirstName.error = getString(R.string.plase_enter_your_first_name)
                }
                else{
                        binding.textLayoutFirstName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtBirDay.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                binding.edtBirDay.setText(dayOfMonth.toString() + "/" + monthOfYear + "/" + year)

            }, year, month, day)
            dpd.datePicker.maxDate = c.timeInMillis
            dpd.show()
        }
        binding.btnUpdate.setOnClickListener {
            gender = if(binding.radioButtonMale.isChecked){
                "male"
            }
            else if (binding.radioButtonFemale.isChecked){
                "female"
            }
            else{
                "other"
            }
            val firstName = binding.edtFirstName.text.toString()
            val lastName = binding.edtLastName.text.toString()
            val phone = binding.edtPhone.text.toString()
            val date = binding.edtBirDay.text.toString()
            var user :User? = null
           if (date.isNotEmpty()){
               val birthDay =  SimpleDateFormat("dd/MM/yyyy").parse(date)
                user = User(gender = gender, phone_number = phone, first_name = firstName, last_name = lastName, birthday = birthDay)

           }
            else{
                user = User(gender = gender, phone_number = phone, first_name = firstName, last_name = lastName)
           }
            mViewModel.updateUser(user!!)
        }

    }

    override fun initViewModel() {
        super.initViewModel()
        binding.viewModel = mViewModel
        mViewModel.getMyUser()
        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = ( it as DataResponse.DataSuccess).body
                binding.item = body.data
                binding.edtBirDay.setText(Utils.getTime(body.data?.birthday.toString()))
              val dtUser =  SharedPreferenceUtils.getInstance(requireContext()).getData()
                dtUser!!.user!!.full_name = body.data!!.full_name
                dtUser!!.user!!.avatar = body.data!!.avatar
                SharedPreferenceUtils.getInstance(requireContext()).putData(dtUser)
                binding.ivAvatar.loadImageFromUrl(body.data!!.avatar!!)
                gender = body.data!!.gender!!

                if (body.data!!.gender == "other"){
                    binding.radioButtonOther.isChecked = true
                }
                else if (body.data!!.gender == "male"){
                    binding.radioButtonMale.isChecked = true
                }
                else{
                    binding.radioButtonFemale.isChecked = true
                }
                binding.btnUpdate.visibility = View.VISIBLE
            }
            else{
                binding.btnUpdate.visibility = View.GONE
            }
        }
        mViewModel.isUploadAvt.observe(this){
            when (it.loadingStatus){
                LoadingStatus.Success -> {
                    binding.rlLoading.visibility = View.GONE
                    val body = (it as DataResponse.DataSuccess).body
                    binding.ivAvatar.loadImageFromUrl(body)
                    ToastUtils.getInstance(requireContext()).showToast("Compelete")

                }
                LoadingStatus.Error ->{
                    binding.rlLoading.visibility = View.GONE
                    ToastUtils.getInstance(requireContext()).showToast("Error")

                }
                LoadingStatus.Loading ->{
                    binding.rlLoading.visibility = View.VISIBLE

                }
                else ->{}
            }

        }
        mViewModel.updateUser.observe(this){
            when (it.loadingStatus){
                LoadingStatus.Success -> {
                    binding.rlLoading.visibility = View.GONE
                    ToastUtils.getInstance(requireContext()).showToast("Compelete")

                }
                LoadingStatus.Error ->{
                    binding.rlLoading.visibility = View.GONE
                    ToastUtils.getInstance(requireContext()).showToast("Error")

                }
                LoadingStatus.Loading ->{
                    binding.rlLoading.visibility = View.VISIBLE
                }
                else ->{}
            }

        }
    }
    private fun requestStoragePermission() {
        resultLauncher.launch(
            Utils.getStoragePermissions()
        )

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (Utils.storagePermissionGrant(requireContext())
            ) {
                setupWhenStoragePermissionGranted()
            } else {
                Utils.showAlertPermissionNotGrant(binding.root, requireActivity())
            }
        }

    private fun setupWhenStoragePermissionGranted() {

        val pickPhoto = PickPhotoDialog.create()
        pickPhoto.listener = object :PickPhotoDialog.OnPhotoPickListener{
            override fun onPicked(filePath:String) {
                val id = SharedPreferenceUtils.getInstance(requireContext()).getData()!!.user!!.id
                mViewModel.uploadAvatar(id!!,filePath)
            }

        }

            pickPhoto.show(requireActivity().supportFragmentManager, "PickImage")


    }

    private fun checkAndOpenPickDialog(){
        if (Utils.storagePermissionGrant(requireContext())) {
            setupWhenStoragePermissionGranted()
        } else {
            requestStoragePermission()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_update_user

}