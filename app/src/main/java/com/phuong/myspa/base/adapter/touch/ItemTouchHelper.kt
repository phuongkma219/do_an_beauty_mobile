package com.phuong.myspa.base.adapter.touch

import android.view.View
import androidx.annotation.IdRes

interface DragItemTouchListener {
    fun onMove(from: Int, to: Int) {}
    fun onMoved(position: Int, direction: Int) {}
}
annotation class ItemTouchDrag
annotation class ItemTouchSwipe(
    val mode: SwipeMode,
    val isSpring: Boolean,
    @IdRes val menuId: Int = View.NO_ID,
    @IdRes val contentId: Int = View.NO_ID
)
enum class SwipeMode {
    RIGHT_TO_LEFT, LEFT_TO_RIGHT, BOTH
}
