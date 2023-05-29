package com.phuong.myspa.ui.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.data.Advertisement
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.databinding.ItemCategoryBinding
import com.phuong.myspa.databinding.LayoutBannerBinding
import com.phuong.myspa.utils.DataUtils
import com.phuong.myspa.utils.ZoomOutPageTransformer
import kotlinx.coroutines.Runnable

class HomeAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_BANNER = 1
    private val TYPE_CATEGORY = 2
    private var listCategory = mutableListOf<Category>()
    private var currentItem =0
    private val  handler  = Handler(Looper.getMainLooper())
    private lateinit var  runnable :Runnable
    lateinit var inner: IOnClickItem


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_BANNER -> {
                val layoutBannerBinding =
                    LayoutBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BannerViewHolder(layoutBannerBinding,inner)
            }

            else ->{
                val mbinding = ItemCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CategoryViewHolder(mbinding,inner)
            }
        }
    }

    class CategoryViewHolder(val binding :ItemCategoryBinding,val inner:IOnClickItem):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Category, itemPosition: Int){
            binding.item = item
            binding.itemPosition = itemPosition
            binding.itemListener = inner
        }
    }
    class BannerViewHolder( val binding:LayoutBannerBinding,val inner:IOnClickItem):RecyclerView.ViewHolder(binding.root){
       fun bind(position: Int){
           binding.viewPager.adapter = BannerApdapter(DataUtils.listBanner)
           binding.viewPager.setPageTransformer(true,
               ZoomOutPageTransformer())
           binding.viewPager.setCurrentItem(position,true)
           binding.dotsIndicator.attachTo(binding.viewPager)
           binding.rlRoot.setOnClickListener {
               inner.onClickItemBanner(DataUtils.listBanner[position])
           }
       }

        fun getCurrentItem():Int = binding.viewPager.currentItem
        fun getCount():Int = DataUtils.listBanner.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (TYPE_BANNER == holder.itemViewType){
            if (holder is BannerViewHolder) {
                runnable = Runnable {
                    currentItem = holder.getCurrentItem()
                    currentItem++
                    if (currentItem>=  holder.getCount()){
                          currentItem  =0
                    }
                    holder.bind(currentItem)
                handler.postDelayed(this.runnable,8000)
                }
                handler.postDelayed(runnable,8000)
            }
        }
        if (TYPE_CATEGORY == holder.itemViewType){
            if (holder is CategoryViewHolder){
                holder.bind(listCategory[position-1],position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position <1){
            TYPE_BANNER
        }else{
            TYPE_CATEGORY
        }
    }

    override fun getItemCount(): Int {
    return listCategory.size + 1
    }
    fun submitData(newData:MutableList<Category>?){
            this.listCategory = newData!!
            notifyDataSetChanged()
    }
    interface IOnClickItem{
        fun onClickItemBanner(item:Advertisement)
        fun onClickItemCategory(item: Category, position: Int)
    }

}