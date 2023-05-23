package com.phuong.myspa.ui.history

import android.util.Log
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.base.adapter.touch.ItemTouchSwipe
import com.phuong.myspa.base.adapter.touch.SwipeMode
import com.phuong.myspa.base.adapter.viewholder.BaseViewHolder
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.databinding.ItemHistoryBinding

class HistoryAdapter :BaseAdapter<History>(R.layout.item_history){
    interface ItemHistoryListener : BaseListener {
        fun onClickItem(position:Int,item: History)
        fun onDeleteItem(position:Int,item: History)
        fun onBuyItem(position:Int,item: History)
    }

}

