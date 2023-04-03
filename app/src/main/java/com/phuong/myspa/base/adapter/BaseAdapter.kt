package com.phuong.myspa.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.hola.ringtonmaker.ui.base.adapter.base.BaseViewHolder
import com.hola.ringtonmaker.ui.base.adapter.base.touch.DragItemTouchListener
import com.hola.ringtonmaker.ui.base.adapter.base.touch.DragVerticalTouchHelper
import com.hola.ringtonmaker.ui.base.adapter.base.touch.ItemTouchDrag
import java.util.*
import com.phuong.myspa.BR
import dagger.hilt.android.internal.managers.ViewComponentManager

abstract class BaseAdapter<T : Any>(
    private val layout: Int
) : RecyclerView.Adapter<BaseViewHolder>(), DragItemTouchListener {

    private lateinit var inflater: LayoutInflater
    private var annotationDrag: ItemTouchDrag? = null
    protected var list = mutableListOf<T>()
    var listener: BaseListener? = null
    init {
        val annotations = this::class.java.declaredAnnotations
        for (annotation in annotations) {
            when (annotation) {
                is ItemTouchDrag -> {
                    annotationDrag = annotation
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            layout,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.binding.apply {
            setVariable(BR.item, list[position])
            setVariable(BR.itemPosition, position)
            setVariable(BR.itemListener, listener)
            try {
                lifecycleOwner=activityContext(root) as LifecycleOwner
                executePendingBindings()
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    private fun activityContext(view: View): Context {
        val context = view.context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        }
        else context
    }


    override fun getItemCount(): Int = list.size ?: 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // drag drop item
        annotationDrag?.let {
            val callback = DragVerticalTouchHelper(this)
            ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
        }
    }
    override fun onMove(from: Int, to: Int) {
        list.let { list ->
            if (from < to) {
                for (i in from until to) {
                    Collections.swap(list, i, i + 1)
                    notifyItemChanged(i, list[i])
                    notifyItemChanged(i + 1, list[i])
                }
            } else {
                for (i in from downTo to + 1) {
                    Collections.swap(list, i, i - 1)
                    notifyItemChanged(i, list[i])
                    notifyItemChanged(i - 1, list[i])
                }
            }
            notifyItemMoved(from, to)
        }
    }
    fun submit(newData : List<T>?){
        val new = newData?.toMutableList()
        this.list = new!!
        notifyDataSetChanged()

    }

}
