package com.phuong.myspa.ui.report

import androidx.lifecycle.MutableLiveData
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.base.adapter.viewholder.BaseViewHolder
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.data.api.model.comment.Content
import com.phuong.myspa.databinding.ItemContentReportBinding
import java.util.*

class ReportAdapter : BaseAdapter<Content>(R.layout.item_content_report) {
    var liveSelect = MutableLiveData(Stack<Content>())
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = (holder.binding as ItemContentReportBinding)
        item.liveSelect = liveSelect
    }
    interface ISelect:BaseListener{
        fun onSelectItem(position: Int,item:Content)
    }
    fun select(item: Content){
        val select = liveSelect.value ?: Stack()
        if (select.search(item) != -1){
            select.remove(item)
        }
        else{
            select.add(item)
        }
        liveSelect.postValue(liveSelect.value)

    }
    fun cleanAll(){
        val select = liveSelect.value ?: Stack()
        select.clear()
        liveSelect.postValue(liveSelect.value)

    }
}