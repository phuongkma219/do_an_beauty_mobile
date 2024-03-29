package com.phuong.myspa.ui.setting

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.BuildConfig
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentSettingBinding
import com.phuong.myspa.ui.history.HistoryFragmentDirections
import com.phuong.myspa.ui.login.LoginFragmentDirections
import com.phuong.myspa.ui.login.SignUpFragmentDirections
import com.phuong.myspa.ui.main.MainFragment
import com.phuong.myspa.ui.updateUser.UpdateUserViewModel
import com.phuong.myspa.utils.ShareViewModel
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment:AbsBaseFragment<FragmentSettingBinding>() {
    private val mViewModel by viewModels<UpdateUserViewModel>()

    override fun initView() {
        ShareViewModel.getInstance(requireActivity().application).get.observe(this){
            if (it == LoadingStatus.Loading){
                mViewModel.getMyUser()
            }
        }
        val user = SharedPreferenceUtils.getInstance(requireContext()).getData()
        mViewModel.getMyUser()
        if ( user== null){
            binding.layoutUser.visibility = View.GONE
        }
        else {
            mViewModel.dataLiveData.observe(this) {
                if (it.loadingStatus == LoadingStatus.Success) {
                    val body = (it as DataResponse.DataSuccess).body
                   if(body != null){
                       binding.tvName.text = body.data?.full_name ?: "Loading"
                       binding.ivAvatar.loadImageFromUrl(body.data?.avatar)
                   }

                }
                else if (it.loadingStatus == LoadingStatus.Error){
                    binding.item = user
                    binding.ivAvatar.loadImageFromUrl(user.user!!.avatar!!)
                }
            }
        }
        setViewOnClick()
    }

    private fun setViewOnClick() {
        binding.btnEditProfile.setOnClickListener {
                findNavController().navigate(SettingFragmentDirections.actionGlobalUpdateUserFragment())
        }
        binding.layoutUser.setOnClickListener {
            findNavController().navigate(SettingFragmentDirections.actionGlobalInforUserFragment())
        }
        binding.tvLanguage.setOnClickListener {
            val languageDialog = LanguageDialog.onCreate()
            languageDialog.show(childFragmentManager,LanguageDialog.TAG)
        }
        binding.tvContactUs.setOnClickListener {
            findNavController().navigate(SettingFragmentDirections.actionGlobalContactUsFragment())
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            val action = SignUpFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(2)
            findNavController()
                .navigate(action)
        }
        binding.tvTermOfService.setOnClickListener {
            val action = SignUpFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(1)
          findNavController()
                .navigate(action)
        }
        binding.tvShareApp.setOnClickListener {
            val  sendIntent =  Intent()
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            sendIntent.type= "text/plain"
            startActivity(sendIntent)
        }
        binding.btnLogOut.setOnClickListener {
            SharedPreferenceUtils.getInstance(requireContext()).clearData()
            findNavController().navigate(SettingFragmentDirections.actionGlobalLoginFragment())
        }
        binding.tvHistory.setOnClickListener {
            findNavController().navigate(HistoryFragmentDirections.actionGlobalHistoryFragment())

        }
        binding.tvForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_global_changePassFragment)
        }

    }

    override fun getLayout(): Int = R.layout.fragment_setting

}