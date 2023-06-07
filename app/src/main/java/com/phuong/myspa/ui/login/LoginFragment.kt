package com.phuong.myspa.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentLoginBinding
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.hideKeyBoard
import com.phuong.myspa.utils.onClickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment:AbsBaseFragment<FragmentLoginBinding>() {
    private val mViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun initView() {
        binding.viewModel = mViewModel
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
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               if (s.isEmpty()){
                   binding.textLayoutPass.isErrorEnabled = true
                   binding.textLayoutPass.error = getString(R.string.plase_enter_your_password)
               }
                else{
                   binding.textLayoutPass.isErrorEnabled = false
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
                    binding.textLayoutPass.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.btnLogin.onClickWithDebounce {
           loginUser()
        }
        binding.btnSignUp.setOnClickListener {

            findNavController().navigate(LoginFragmentDirections.actionGlobalSignUpFragment())
        }

        binding.edtPassword.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    loginUser()
                    return true
                }
                return false
            }

        })
    }
    override fun initViewModel(){
        mViewModel.dataLiveData.observe(this){
           when(it.loadingStatus){
               LoadingStatus.Success ->{
                   val body = (it as DataResponse.DataSuccess).body
                   SharedPreferenceUtils.getInstance(requireContext()).putData(body)
                   findNavController().navigate(LoginFragmentDirections.actionGlobalMainFragment())
               }
               LoadingStatus.Error ->{
                   ToastUtils.getInstance(requireContext()).showToast(getString(
                                  R.string.incorrect_account_or_password))
               }
               else ->{}
           }
        }
    }
    private fun loginUser(){
        val emailAddress: String = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        if (password.isEmpty()){
            binding.textLayoutPass.isErrorEnabled = true
            binding.textLayoutPass.error = getString(R.string.plase_enter_your_password)
        }
        if (emailAddress.isEmpty() ){
            binding.textLayoutEmail.isErrorEnabled = true
            binding.textLayoutEmail.error = getString(R.string.plase_enter_your_mail)
        }
        else{
            if ( password.isNotEmpty()) {
                binding.textLayoutEmail.isErrorEnabled = false
                mViewModel.loginUser(UserDTO(emailAddress,password))

            }
        }
        hideKeyBoard(requireContext(),binding.edtPassword)
    }

    override fun onResume() {
        super.onResume()
        val emailAddress: String = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        if (password.isEmpty()){
            binding.textLayoutPass.isErrorEnabled = false
            binding.textLayoutPass.error = null
        }
        if (emailAddress.isEmpty() ){
            binding.textLayoutEmail.isErrorEnabled = false
            binding.textLayoutEmail.error = null
        }
    }
    override fun getLayout(): Int  = R.layout.fragment_login

}