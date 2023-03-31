package com.phuong.myspa.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val topPadding: Int, private val bottomPadding: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter!!.itemCount

        if (position == itemCount - 1) {
            outRect.bottom = bottomPadding
        }
        if (position == 0) {
            outRect.top = topPadding
        }
    }
}