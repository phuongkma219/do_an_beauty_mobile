package com.phuong.myspa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.phuong.myspa.R
import com.phuong.myspa.data.Advertisement
import com.phuong.myspa.databinding.ItemBannerBinding

class BannerApdapter(  private val listBanner: MutableList<Advertisement>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listBanner.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding: ItemBannerBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_banner, container, false)
        binding.data = listBanner[position]
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as (View))
    }
}