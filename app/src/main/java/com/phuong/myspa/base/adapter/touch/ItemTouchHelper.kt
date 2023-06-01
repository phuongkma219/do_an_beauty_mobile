package com.phuong.myspa.base.adapter.touch

import android.view.View
import androidx.annotation.IdRes

interface DragItemTouchListener {
    fun onMove(from: Int, to: Int) {}
    fun onMoved(position: Int, direction: Int) {}
}
annotation class ItemTouchDrag

