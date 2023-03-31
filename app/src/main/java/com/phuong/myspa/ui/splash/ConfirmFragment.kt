package com.phuong.myspa.ui.splash

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentConfirmBinding
import com.phuong.myspa.utils.SharedPreferenceUtils

class ConfirmFragment:AbsBaseFragment<FragmentConfirmBinding>() {
    private lateinit var mText: String


    override fun initView() {
        binding.toolbar.setNavigationOnClickListener { requireActivity().finish() }
        mText = requireContext().resources.getString(R.string.agree_term_of_service_and_policy)

        val spannableString = SpannableString(mText)

        val clickTermsOfService: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action = ConfirmFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(1)
                findNavController().navigate(action)
            }
        }

        val clickPrivacyPolicy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action = ConfirmFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(2)
                findNavController().navigate(action)
            }
        }

        spannableString.setSpan(clickTermsOfService, 50, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickPrivacyPolicy, 70, 84, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvServiceAndPolicy.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.tvServiceAndPolicy.movementMethod = LinkMovementMethod.getInstance()

        binding.btnGetStarted.setOnClickListener {
            SharedPreferenceUtils.getInstance(requireActivity()).putAcceptPolicy(true)
          findNavController().navigate( ConfirmFragmentDirections.actionGlobalMainFragment())
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

    }
    override fun getLayout(): Int = R.layout.fragment_confirm

}