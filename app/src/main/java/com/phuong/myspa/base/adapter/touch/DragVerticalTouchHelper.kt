package com.hola.ringtonmaker.ui.base.adapter.base.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragVerticalTouchHelper(private val listener: DragItemTouchListener) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled() = false

    override fun isLongPressDragEnabled() = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag: Int = ItemTouchHelper.START
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        listener.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        listener.onMoved(fromPos, toPos)
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}