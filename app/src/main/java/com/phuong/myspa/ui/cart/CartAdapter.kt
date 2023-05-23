package com.phuong.myspa.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.data.api.model.cart.DataCart
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.databinding.ItemHeaderBinding
import com.phuong.myspa.databinding.ItemProductCartBinding
import com.phuong.myspa.utils.loadImageFromUrl
import com.phuong.myspa.utils.postSelf
import java.util.*

class CartAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val TYPE_HEADER =1
    private val TYPE_CONTENT=2
    var listData = mutableListOf<DataModel>()

    var liveSelect = MutableLiveData(Stack<DataModel.DataItem>())

    lateinit var inner : ItemCartListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> {
                val itemHeaderBinding = ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemHeaderViewHolder(itemHeaderBinding, inner)
            }

            else -> {

                val mbinding = ItemProductCartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemContentViewHolder(mbinding,inner)


            }
        }
    }
    class ItemHeaderViewHolder(val binding:ItemHeaderBinding,val inner:ItemCartListener):RecyclerView.ViewHolder(binding.root){
        fun bind(model: DataModel.DataHeader){
            binding.tvName.text = model.name
            binding.root.setOnClickListener { inner.onClickTitle(model) }
        }

    }
    class ItemContentViewHolder(val binding:ItemProductCartBinding,val inner:ItemCartListener):RecyclerView.ViewHolder(binding.root){

        fun bind(model: DataModel.DataItem){
            binding.item = model
            binding.ivAvatar.loadImageFromUrl(model.avatar)
            binding.btnDelete.setOnClickListener { inner.onDeleteItem(model) }
            binding.root.setOnClickListener { inner.onClickItemService(model) }
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (TYPE_HEADER == holder.itemViewType) {
            if (holder is ItemHeaderViewHolder) {
                holder.bind(listData[position] as DataModel.DataHeader)
            }
        }
        if (TYPE_CONTENT == holder.itemViewType) {
            if (holder is ItemContentViewHolder) {
                holder.bind(listData[position] as DataModel.DataItem)
                holder.binding.liveSelect = liveSelect
                holder.binding.ivSelect.setOnClickListener {
                    select(listData[position] as DataModel.DataItem)
                }
            }
        }
    }



    override fun getItemCount(): Int {
        return listData.size ?:0
    }

    override fun getItemViewType(position: Int): Int {
         return  when(listData[position]){
             is DataModel.DataHeader -> TYPE_HEADER
             is DataModel.DataItem -> TYPE_CONTENT
             else -> {0}
         }

    }
//    var __v: Int,
//    var _id: String,
//    var address: String,
//    var avatar: String,
//    var category: List<String>,
//    var created_at: String,
//    var description: String,
//    var email: String,
//    var end_time: String,
//    var name: String,
//    var phone_number: String,
//    var rate: Double,
//    var start_time: String,
//    var updated_at: String
    fun submit(newData : MutableList<DataCart>?){
        val newList = mutableListOf<DataModel>()
        newData?.forEach {
            newList.add(DataModel.DataHeader(0,it.shop._id,it.shop.address,it.shop.avatar,
                it.shop.category,it.shop.created_at,it.shop.description,it.shop.email,it.shop.end_time
                ,it.shop.name,it.shop.phone_number,it.shop.rate,it.shop.start_time,it.shop.updated_at))
            it.items.forEach {
                newList.add(DataModel.DataItem(it.serviceDetail._id,it.serviceDetail.avatar,
                    it.serviceDetail.name,it.serviceDetail.price,
                    it.serviceDetail.shop_id,it.serviceDetail.time,it.serviceDetail.description,it.serviceDetail.created_at))
            }
        }
        listData = newList
        notifyDataSetChanged()

    }
    fun select(item: DataModel.DataItem) {
        val select = liveSelect.value ?: Stack()
        if (select.search(item) != -1) {
            select.remove(item)
        } else {
            select.add(item)
        }
        liveSelect.postSelf()
        notifyDataSetChanged()

    }
    fun removeItem(item: DataModel.DataItem){
        val select = liveSelect.value ?: Stack()
        select.remove(item)
        liveSelect.postSelf()
    }
    fun clearAll() {
        val select = liveSelect.value ?: Stack()
        select.clear()
        liveSelect.postSelf()
    }
    interface ItemCartListener : BaseListener{
        fun onClickItemService(item: DataModel.DataItem)
        fun onDeleteItem(item: DataModel.DataItem)
        fun onClickTitle(item:DataModel.DataHeader)
    }
}