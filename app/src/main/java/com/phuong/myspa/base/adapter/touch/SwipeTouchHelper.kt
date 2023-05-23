package com.phuong.myspa.base.adapter.touch

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.base.adapter.viewholder.BaseViewHolder

class SwipeTouchHelper(private val mode: SwipeMode,
                       private val isSpring: Boolean
) : ItemTouchHelperExtension.Callback() {

        override fun isLongPressDragEnabled() = false
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag = when (mode) {
            SwipeMode.RIGHT_TO_LEFT -> {
                ItemTouchHelper.START
            }
            SwipeMode.LEFT_TO_RIGHT -> {
                ItemTouchHelper.END
            }
            SwipeMode.BOTH -> {
                ItemTouchHelper.START or ItemTouchHelper.END
            }
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (dY != 0f && dX == 0f) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        val holder = viewHolder as BaseViewHolder
        if (!isSpring) {
            val deltaDx = if (dX < -holder.actionWidth) {
                -holder.actionWidth
            } else {
                dX
            }
            holder.getLayoutContent().translationX = deltaDx
        } else {
            holder.getLayoutContent().translationX = dX
        }
    }

}
