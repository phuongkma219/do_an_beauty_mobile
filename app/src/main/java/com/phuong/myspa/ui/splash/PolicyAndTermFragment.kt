package com.phuong.myspa.ui.splash

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentPolicyAndTermBinding

class PolicyAndTermFragment: AbsBaseFragment<FragmentPolicyAndTermBinding>() {
    private val args: PolicyAndTermFragmentArgs by navArgs()


    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (args.param == 1) {
            binding.toolbar.title = requireContext().resources.getString(R.string.term_of_service)
            binding.wvPolicy.loadUrl("file:///android_asset/term_of_service.html")
        }
        else {
            binding.toolbar.title = requireContext().resources.getString(R.string.privacy_policy)
            binding.wvPolicy.loadUrl("file:///android_asset/policy.html")
        }
    }
    override fun getLayout(): Int  = R.layout.fragment_policy_and_term
}