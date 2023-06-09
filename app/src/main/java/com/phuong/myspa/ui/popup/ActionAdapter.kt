package com.phuong.myspa.ui.popup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phuong.myspa.databinding.ItemActionPopupBinding


class ActionAdapter(
    private val actions: MutableList<ActionModel>,
    private val onActionClickListener: OnActionClickListener?
) : android.widget.BaseAdapter() {
    override fun getCount() = actions.size
    override fun getItem(position: Int): ActionModel {
        return actions[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val actionHolder: ActionHolder
        if (convertView == null) {
            val binding =
                ItemActionPopupBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            actionHolder = ActionHolder(binding)
            actionHolder.binding.root.tag = actionHolder
        } else {
            actionHolder = convertView.tag as ActionHolder
        }
        actionHolder.bind(position)
        return actionHolder.binding.root
    }

    inner class ActionHolder(val binding: ItemActionPopupBinding) {
        fun bind(position: Int) {
            binding.item = actions[position]
            binding.myLayoutRoot.setOnClickListener {
                onActionClickListener?.onItemActionClick(position)
            }
        }
    }

    interface OnActionClickListener {
        fun onItemActionClick(position1: Int)
    }

}