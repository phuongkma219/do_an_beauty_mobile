package com.phuong.myspa.ui.home

import com.phuong.myspa.base.adapter.BaseAdapter
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.data.api.model.Category

class CategoryAdapter: BaseAdapter<Category>(R.layout.item_category) {
    interface IOnClickItemCategory : BaseListener{
        fun onClickItem(item: Category, position:Int)
    }
}