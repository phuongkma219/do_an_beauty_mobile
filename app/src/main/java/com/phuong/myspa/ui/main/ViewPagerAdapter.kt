package com.phuong.myspa.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.phuong.myspa.ui.favorite.FavoriteFragment
import com.phuong.myspa.ui.home.HomeFragment
import com.phuong.myspa.ui.setting.SettingFragment

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
                FavoriteFragment()
            }

            else -> {
                SettingFragment()
            }
        }
    }
    fun refreshFragment(position: Int){
        notifyItemChanged(position)
    }
}