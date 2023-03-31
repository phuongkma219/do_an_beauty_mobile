package com.phuong.myspa.ui.splash

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentSplashBinding
import com.phuong.myspa.utils.SharedPreferenceUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : AbsBaseFragment<FragmentSplashBinding>(), Animation.AnimationListener {
    override fun getLayout(): Int = R.layout.fragment_splash

    override fun initView() {
        val animMoveToTop = AnimationUtils.loadAnimation(requireContext(),R.anim.move)
        binding.ivLogo.startAnimation(animMoveToTop)
        animMoveToTop.setAnimationListener(this)
    }


    override fun onAnimationStart(p0: Animation?) {

    }

    override fun onAnimationEnd(p0: Animation?) {
        if (SharedPreferenceUtils.getInstance(requireContext()).getData() == null) {
            findNavController().navigate(SplashFragmentDirections.actionGlobalLoginFragment())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionGlobalMainFragment())
        }
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }
}