package com.phuong.myspa.ui.detail_shop

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentShopBinding
import com.phuong.myspa.ui.favorite.FavoriteViewModel
import com.phuong.myspa.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShopFragment:AbsBaseFragment<FragmentShopBinding>() {
    private lateinit var mAdapter: FragmentPageAdapter
    private val args : ShopFragmentArgs by navArgs()
    private lateinit var mViewModel:ShopViewModel
    private var isFavorite  = false
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun initView() {
        binding.layoutLoading.root.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
           handler.postDelayed( {
            mAdapter = FragmentPageAdapter(requireActivity().supportFragmentManager,requireActivity().lifecycle)
            mAdapter.setData(args.shop)
            binding.viewPager.adapter = mAdapter
               binding.layoutLoading.root.visibility = View.INVISIBLE
        },500)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.infor)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.service)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.comment)))
        binding.toolbar.title = args.shop.name
        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT


        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.setCurrentItem( tab.position,false)
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
        favoriteViewModel.isSuccess.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
                if (body) {
                    isFavorite = true
                    binding.toolbar.menu.findItem(R.id.icFavorite).setIcon(R.drawable.ic_baseline_favorite_24)
                    ToastUtils.getInstance(requireContext()).showToast(getString(R.string.add_to_favorite))
                } else {
                    isFavorite = false
                    binding.toolbar.menu.findItem(R.id.icFavorite).setIcon(R.drawable.ic_baseline_favorite_border_24)
                    ToastUtils.getInstance(requireContext()).showToast(getString(R.string.delete_success))

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
        binding.layoutLoading.root.visibility = View.VISIBLE
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