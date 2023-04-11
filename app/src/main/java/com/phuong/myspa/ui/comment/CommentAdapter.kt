package com.phuong.myspa.ui.comment

import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.data.api.model.comment.Comment

class CommentAdapter : BaseAdapter<Comment>(R.layout.item_comment) {
    fun submitList(isAddMore: Boolean, newData: MutableList<Comment>) {
        if (isAddMore) {
            this.list.addAll(newData)
            notifyItemInserted(list.size)
        } else {
            this.list = newData.toMutableList()
            notifyDataSetChanged()
        }

    }
    fun clearData(){
        list.clear()
        notifyDataSetChanged()
    }
}