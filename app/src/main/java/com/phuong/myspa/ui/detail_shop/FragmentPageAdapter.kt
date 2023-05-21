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
    private var shopId : String? = null
    override fun getItemCount(): Int {
        return 3
    }

    fun setData(shopId:String){
        this.shopId = shopId
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            DetailShopFragment.newInstance(shopId!!)
        } else if(position ==1){
            ServiceFragment.newInstance(shopId!!)
        }
        else{
            CommentFragment.newInstance((shopId!!))
        }
    }
    fun refreshFragment(position: Int){
        notifyItemChanged(position)
    }
}