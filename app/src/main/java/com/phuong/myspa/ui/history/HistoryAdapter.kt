package com.phuong.myspa.ui.history

import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.base.adapter.touch.ItemTouchSwipe
import com.phuong.myspa.base.adapter.touch.SwipeMode
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.shop.DataModel

@ItemTouchSwipe(mode = SwipeMode.RIGHT_TO_LEFT,isSpring = true)
class HistoryAdapter :BaseAdapter<History>(R.layout.item_history){
    interface ItemHistoryListener : BaseListener {
        fun onClickItem(item: History)
        fun onDeleteItem(item: History)
        fun onBuyItem(item: History)
    }
}

