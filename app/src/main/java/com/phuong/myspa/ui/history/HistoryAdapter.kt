package com.phuong.myspa.ui.history

import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.data.api.model.history.History

class HistoryAdapter :BaseAdapter<History>(R.layout.item_history){
    interface ItemHistoryListener : BaseListener {
        fun onClickItem(position:Int,item: History)
        fun onDeleteItem(position:Int,item: History)
        fun onBuyItem(position:Int,item: History)
    }

}

