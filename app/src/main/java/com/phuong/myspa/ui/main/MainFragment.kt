package com.phuong.myspa.ui.main

import androidx.core.view.get
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : AbsBaseFragment<FragmentMainBinding>() {
    override fun getLayout(): Int = R.layout.fragment_main

    override fun initView() {
        val mAdapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager,requireActivity().lifecycle)
        binding.viewPager.adapter = mAdapter
        binding.navBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    binding.viewPager.currentItem = 0
                }
                R.id.favoriteFragment -> {
                    binding.viewPager.currentItem = 1
                }
                R.id.settingFragment -> {
                    binding.viewPager.currentItem = 2
                }

            }
            true
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.navBottom.menu.findItem(R.id.homeFragment)
                    }
                    1 -> {
                        mAdapter.refreshFragment(position)
                        binding.navBottom.menu.findItem(R.id.favoriteFragment)
                    }
                    2 -> {
                        binding.navBottom.menu.findItem(R.id.settingFragment)
                    }
                }

            }
        })

    }
}