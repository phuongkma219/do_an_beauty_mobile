package com.phuong.myspa.ui.detail_shop

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.ui.comment.CommentFragment
import com.phuong.myspa.ui.shop_service.ServiceFragment

class FragmentPageAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle){
    private var data : ShopInfor? = null
    override fun getItemCount(): Int {
        return 3
    }
    fun setData(shop:ShopInfor){
        data = shop
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            DetailShopFragment.newInstance(data!!)
        } else if(position ==1){
            ServiceFragment.newInstance(data!!)
        }
        else{
            CommentFragment()
        }
    }
}