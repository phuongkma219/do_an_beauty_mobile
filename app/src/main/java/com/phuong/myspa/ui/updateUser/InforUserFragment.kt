package com.phuong.myspa.ui.updateUser

import android.view.View
import androidx.fragment.app.viewModels
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentInforUserBinding
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.Utils
import com.phuong.myspa.utils.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InforUserFragment : AbsBaseFragment<FragmentInforUserBinding>() {
    private val mViewModel by viewModels<UpdateUserViewModel>()

    override fun getLayout(): Int {
        return  R.layout.fragment_infor_user
    }

    override fun initView() {
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

                if (body.data!!.gender == "other"){
                    binding.radioButtonOther.isChecked = true
                }
                else if (body.data!!.gender == "male"){
                    binding.radioButtonMale.isChecked = true
                }
                else{
                    binding.radioButtonFemale.isChecked = true
                }
            }

        }
    }
}