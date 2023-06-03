package com.phuong.myspa.ui.search

import androidx.lifecycle.MutableLiveData
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.base.adapter.BaseViewHolder
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.databinding.ItemCateFilterBinding
import com.phuong.myspa.utils.postSelf
import java.util.*

class CateAdapter : BaseAdapter<Category>(R.layout.item_cate_filter) {
    var liveSelect = MutableLiveData(Stack<Category>())

    interface IClickFilter: BaseListener{
        fun onClickCate(position:Int, item : Category)
    }
    fun select(item: Category) {
        val select = liveSelect.value ?: Stack()
        if (select.search(item) != -1) {
            select.remove(item)
        } else {
            select.add(item)
        }
        liveSelect.postSelf()
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = (holder.binding as ItemCateFilterBinding)
        item.liveSelect = liveSelect
    }
}