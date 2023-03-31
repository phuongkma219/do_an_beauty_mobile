package com.phuong.myspa.ui.pickphoto

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.hola360.lwac.ui.pickphoto.data.PickModelDataType
import com.hola360.lwac.ui.pickphoto.data.PickPhotoModel
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.databinding.ItemAlbumBinding
import com.phuong.myspa.databinding.ItemPhotoBinding
import dagger.hilt.android.internal.managers.ViewComponentManager
import java.util.*

class PhotoAdapter(private val lifecycle: LifecycleOwner, private val listener : ListenerClickItem):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var pickPhotoModel: PickPhotoModel? = null
    fun update(pickPhotoModel: PickPhotoModel) {
        this.pickPhotoModel = pickPhotoModel
        notifyDataSetChanged()

    }
    var liveSelect = MutableLiveData(Stack<PhotoModel>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PickModelDataType.LoadAlbum.ordinal) {
            val itemBinding = ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AlbumViewHolder(itemBinding)
        } else {
            val itemBinding = ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            PhotoViewHolder(itemBinding)
        }

    }


    override fun getItemCount(): Int = if (pickPhotoModel != null) {
        pickPhotoModel!!.photoList.size
    } else {
        0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumViewHolder -> {
                holder.bind(position)
            }
            is PhotoViewHolder -> {
                holder.bind(position)
            }
        }

    }


    inner class AlbumViewHolder(itemView: ItemAlbumBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        internal val binding = itemView
        fun bind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoList[position]
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener.onClickFileItem(
                        position,
                        pickPhotoModel!!.pickModelDataType,
                        pickPhotoModel!!.photoList[position]
                    )
                }
            }
        }
    }


    inner class PhotoViewHolder(itemView: ItemPhotoBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        internal val binding = itemView
        fun bind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoList[position]
            binding.liveSelect = liveSelect
            binding.apply {
                lifecycleOwner=lifecycle
                executePendingBindings()
            }
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener.onClickFileItem(
                        position,
                        pickPhotoModel!!.pickModelDataType,
                        pickPhotoModel!!.photoList[position]
                    )
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (pickPhotoModel != null) {
            pickPhotoModel!!.pickModelDataType.ordinal
        } else {
            PickModelDataType.LoadAlbum.ordinal
        }
    }

    interface ListenerClickItem {
        fun onClickFileItem(
            position: Int,
            pickModelDataType: PickModelDataType,
            photoModel: PhotoModel
        )
    }
    fun select(item: PhotoModel){
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
