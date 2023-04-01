package com.phuong.myspa.ui.detail_shop

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentShopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopFragment:AbsBaseFragment<FragmentShopBinding>() {
    private lateinit var mAdapter: FragmentPageAdapter
    private val args : ShopFragmentArgs by navArgs()
    private lateinit var mViewModel:ShopViewModel

    override fun initView() {
        mViewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        mViewModel.getDetailShop(args.shop._id)
        binding.toolbar.title = args.shop.name
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        mAdapter = FragmentPageAdapter(requireActivity().supportFragmentManager,requireActivity().lifecycle)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Infor"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Service"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Comment"))
        mAdapter.setData(args.shop)
        binding.viewPager.adapter = mAdapter
        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        binding.viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))

            }
        })
    }

    override fun getLayout(): Int = R.layout.fragment_shop
}