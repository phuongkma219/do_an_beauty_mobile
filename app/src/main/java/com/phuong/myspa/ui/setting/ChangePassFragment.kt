package com.phuong.myspa.ui.setting

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.ChangePassDTO
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentChangePassBinding
import com.phuong.myspa.ui.updateUser.UpdateUserViewModel
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePassFragment :AbsBaseFragment<FragmentChangePassBinding>() {
    private val mViewModel by viewModels<UpdateUserViewModel>()
    override fun getLayout(): Int {
        return R.layout.fragment_change_pass
    }

    override fun initView() {
        initEdtView()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSignUp.setOnClickListener {
            changePass()
        }
        mViewModel.isChangePass.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                binding.layoutLoading.root.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_global_loginFragment)
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                binding.layoutLoading.root.visibility = View.INVISIBLE
                ToastUtils.getInstance(requireContext()).showToast(getString(R.string.error_please_try_again))
            }
        }
    }
    private fun changePass(){
        val old_password = binding.edtPassword.text.toString().trim()
        val new_password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtCofirmPassword.text.toString().trim()
        if (old_password.isEmpty()){
            binding.textLayoutPassOld.isErrorEnabled = true
            binding.textLayoutPassOld.error =getString(R.string.plase_enter_your_password)
        }
        if (new_password.isEmpty()){
            binding.textLayoutPass.isErrorEnabled = true
            binding.textLayoutPass.error =getString(R.string.plase_enter_your_password)
        }
        if (confirmPassword.isEmpty()){
            binding.textLayoutConfirmPass.isErrorEnabled = true
            binding.textLayoutConfirmPass.error =getString(R.string.plase_enter_your_confirm_pass)
        }
        if (!binding.textLayoutPassOld.isErrorEnabled && !binding.textLayoutPass.isErrorEnabled
            &&  !binding.textLayoutConfirmPass.isErrorEnabled){
            binding.layoutLoading.root.visibility = View.VISIBLE

            mViewModel.changePass(ChangePassDTO(old_password,new_password,confirmPassword))
        }

    }
    private fun initEdtView(){
        binding.edtPassOld.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutPassOld.isErrorEnabled = true
                    binding.textLayoutPassOld.error = getString(R.string.plase_enter_your_password_old)
                }
                else{
                    if (s.length<8){
                        binding.textLayoutPassOld.isErrorEnabled = true
                        binding.textLayoutPassOld.error =getString(R.string.password_must_be_at_least_8_characters)
                    }
                    else{
                        binding.textLayoutPassOld.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutPass.isErrorEnabled = true
                    binding.textLayoutPass.error = getString(R.string.plase_enter_your_password)
                }
                else{
                    if (s.length<8){
                        binding.textLayoutPass.isErrorEnabled = true
                        binding.textLayoutPass.error =getString(R.string.password_must_be_at_least_8_characters)
                    }
                    else{
                        binding.textLayoutPass.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtCofirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutConfirmPass.isErrorEnabled = true
                    binding.textLayoutConfirmPass.error = getString(R.string.plase_enter_your_confirm_pass)
                }
                else{
                    if (s.toString().equals(binding.edtPassword.text.toString().trim())){
                        binding.textLayoutConfirmPass.isErrorEnabled = false
                        binding.textLayoutConfirmPass.error =""
                    }
                    else{
                        binding.textLayoutConfirmPass.isErrorEnabled = true
                        binding.textLayoutConfirmPass.error =getString(R.string.confirm_password_is_incorrect)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s.trim().equals(binding.edtPassword.text.toString().trim())){
                    binding.textLayoutConfirmPass.isErrorEnabled = false
                }
            }
        })
    }
}