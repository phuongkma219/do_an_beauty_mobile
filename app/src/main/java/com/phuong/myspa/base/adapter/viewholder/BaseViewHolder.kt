package com.phuong.myspa.base.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.touch.Extension
import com.phuong.myspa.base.adapter.touch.ItemTouchSwipe

open class BaseViewHolder(val binding: ViewDataBinding,
                          private val annotationSwipe: ItemTouchSwipe?
) : RecyclerView.ViewHolder(binding.root), Extension {

    fun getLayoutMenu(): ViewGroup {
        val id = annotationSwipe?.let {
            if (it.menuId == View.NO_ID) {
                R.id.itemMenu
            } else {
                it.menuId
            }
        } ?: run {
            View.NO_ID
        }
        return binding.root.findViewById(id)
    }

    fun getLayoutContent(): ViewGroup {
        val id = annotationSwipe?.let {
            if (it.contentId == View.NO_ID) {
                R.id.itemContent
            } else {
                it.contentId
            }
        } ?: run {
            View.NO_ID
        }
        return binding.root.findViewById(id)
    }

    override fun getActionWidth(): Float {
        return getLayoutMenu().width.toFloat()
    }
}
