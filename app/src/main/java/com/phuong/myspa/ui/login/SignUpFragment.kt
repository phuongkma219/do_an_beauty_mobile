package com.phuong.myspa.ui.login

import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentSignUpBinding
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment:AbsBaseFragment<FragmentSignUpBinding>() {
private val mViewModel by viewModels<SignUpViewModel>()

    override fun initView() {
        binding.viewModel = mViewModel
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        termAndPolicy()
        initEditText()
        setOnClick()
    }

    override fun initViewModel() {
        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                ToastUtils.getInstance(requireContext()).showToast("Success")
                findNavController().navigateUp()
            }
            if (it.loadingStatus == LoadingStatus.Error){
                val body = (it as DataResponse.DataError).body
                if (body?.message == "AUTH.REGISTER.CONFLICT_USERNAME"){
                    ToastUtils.getInstance(requireContext()).showToast(getString(R.string.user_name_has_been_exist))
                }
                if (body?.message == "AUTH.REGISTER.CONFLICT_EMAIL"){
                    ToastUtils.getInstance(requireContext()).showToast(getString(R.string.email_address_has_been_exist))
                }
                if (body?.message == "AUTH.REGISTER.CONFLICT_PHONENUMBER"){
                    ToastUtils.getInstance(requireContext()).showToast(getString(R.string.phone_number_has_been_exist))
                }
            }
        }
    }

    private fun initEditText() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutEmail.isErrorEnabled = true
                    binding.textLayoutEmail.error = getString(R.string.plase_enter_your_mail)
                }
                else{
                    binding.textLayoutEmail.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    binding.textLayoutName.isErrorEnabled = true
                    binding.textLayoutName.error = getString(R.string.plase_enter_your_name)
                }
                else{
                    binding.textLayoutName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
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

    private fun setOnClick() {
        binding.btnSignUp.setOnClickListener {
          signUpUser()
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun signUpUser(){
        val email = binding.edtEmail.text.toString().trim()
        val name = binding.edtName.text.toString().trim()
        val phoneNumber = binding.edtPhone.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtCofirmPassword.text.toString().trim()
        if (email.isEmpty()){
            binding.textLayoutEmail.isErrorEnabled = true
            binding.textLayoutEmail.error =getString(R.string.plase_enter_your_mail)
        }
        else{
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.textLayoutEmail.isErrorEnabled = false
            } else {
                binding.textLayoutEmail.isErrorEnabled = true
                binding.textLayoutEmail.error = getString(R.string.invalid_email_address)
            }
        }
        if (name.isEmpty()){
            binding.textLayoutName.isErrorEnabled = true
            binding.textLayoutName.error =getString(R.string.plase_enter_your_name)
        }
        if (phoneNumber.isEmpty()){
            binding.textLayoutPhone.isErrorEnabled = true
            binding.textLayoutPhone.error =getString(R.string.plase_enter_your_phone_number)
        }

        if (password.isEmpty()){
            binding.textLayoutPass.isErrorEnabled = true
            binding.textLayoutPass.error =getString(R.string.plase_enter_your_password)
        }

        if (confirmPassword.isEmpty()){
            binding.textLayoutConfirmPass.isErrorEnabled = true
            binding.textLayoutConfirmPass.error =getString(R.string.plase_enter_your_confirm_pass)
        }
        if (!binding.textLayoutEmail.isErrorEnabled && !binding.textLayoutName.isErrorEnabled
            && !binding.textLayoutPhone.isErrorEnabled && !binding.textLayoutPass.isErrorEnabled
            &&  !binding.textLayoutConfirmPass.isErrorEnabled){
            if (binding.cbServiceAndPolicy.isChecked){
                mViewModel.userSignUp(UserSignUp(email,name,name,password,phoneNumber,name))
            }
            else{
                ToastUtils.getInstance(requireContext()).showToast(getString(R.string.i_have_agree_term_of_service_and_policy))
            }
        }

    }

    private fun termAndPolicy(){
        val mText = requireContext().resources.getString(R.string.i_have_agree_term_of_service_and_policy)

        val spannableString = SpannableString(mText)

        val clickTermsOfService: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action = SignUpFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(1)
                findNavController().navigate(action)
            }
        }

        val clickPrivacyPolicy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action = SignUpFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(2)
                findNavController().navigate(action)
            }
        }

        spannableString.setSpan(clickPrivacyPolicy, 47, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickTermsOfService, 26, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvCheck.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.tvCheck.movementMethod = LinkMovementMethod.getInstance()


    }
    override fun getLayout(): Int = R.layout.fragment_sign_up
}