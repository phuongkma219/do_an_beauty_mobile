package com.phuong.myspa.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.ui.detail_shop.DetailShopFragment
import com.phuong.myspa.ui.favourite.FavouriteFragment
import com.phuong.myspa.ui.home.HomeFragment
import com.phuong.myspa.ui.setting.SettingFragment
import com.phuong.myspa.ui.shop_service.ServiceFragment

class ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }
            1 -> {
                FavouriteFragment()
            }

            else -> {
                SettingFragment()
            }
        }
    }
}