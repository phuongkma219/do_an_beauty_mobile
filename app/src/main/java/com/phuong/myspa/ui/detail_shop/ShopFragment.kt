package com.phuong.myspa.ui.detail_shop

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentShopBinding
import com.phuong.myspa.ui.favorite.FavoriteViewModel
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class ShopFragment:AbsBaseFragment<FragmentShopBinding>() {
    private lateinit var mAdapter: FragmentPageAdapter
    private val args : ShopFragmentArgs by navArgs()
    private lateinit var mViewModel:ShopViewModel
    private var isFavorite  = false
    private val favoriteViewModel by viewModels<FavoriteViewModel>()


    override fun initView() {
       setUpToolBar()
        mAdapter = FragmentPageAdapter(requireActivity().supportFragmentManager,requireActivity().lifecycle)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.infor)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.service)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.comment)))
        binding.toolbar.title = args.shop.name

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
        favoriteViewModel.isSuccess.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                if (body) {
                    isFavorite = true
                    binding.toolbar.menu.findItem(R.id.icFavorite).setIcon(R.drawable.ic_baseline_favorite_24)
                } else {
                    isFavorite = false
                    binding.toolbar.menu.findItem(R.id.icFavorite).setIcon(R.drawable.ic_baseline_favorite_border_24)
                }
            }

        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]
        mViewModel.getDetailShop(args.shop._id)
        binding.viewModel = mViewModel
        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success) {
                val body = (it as DataResponse.DataSuccess).body.data
                if (body != null){
                    updateToolBar(body.isFavorite)
                }
            }
        }

    }

    private fun setUpToolBar() {

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener(object :Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.icFavorite){
                    favoriteViewModel.addFavorite(args.shop._id,isFavorite)
                }
                return true
            }

        })
    }
    private fun updateToolBar(isFavorite:Boolean) {
        this.isFavorite = isFavorite
        if (isFavorite) {
            binding.toolbar.menu.findItem(R.id.icFavorite).setIcon(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.toolbar.menu.findItem(R.id.icFavorite).setIcon(R.drawable.ic_baseline_favorite_border_24)
        }

    }

    override fun getLayout(): Int = R.layout.fragment_shop
}