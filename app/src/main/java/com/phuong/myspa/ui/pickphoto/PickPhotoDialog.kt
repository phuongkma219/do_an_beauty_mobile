package com.phuong.myspa.ui.pickphoto

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.hola360.lwac.ui.pickphoto.data.PickModelDataType
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseFullDialog
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.DialogPickPhotoBinding
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class PickPhotoDialog:BaseFullDialog<DialogPickPhotoBinding>(), PhotoAdapter.ListenerClickItem {
    private lateinit var adapter: PhotoAdapter
    private val mViewModel by viewModels<PickPhotoViewModel> ()
    private lateinit var mLayoutManager: GridLayoutManager
    var albumState: Parcelable? = null
     var listener : PickPhotoDialog.OnPhotoPickListener? = null
    override fun initView() {
        if (listener == null){
            dismiss()
        }
        adapter = PhotoAdapter(viewLifecycleOwner,this)
        mLayoutManager = GridLayoutManager(requireContext(), AT_LEAST_COLUMN)

        initViewModel()
        initRecycleView()
        onClickDone()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPress()
            }
            true
        }
    }
    private fun onBackPress() {
        mViewModel.onClose()
    }

    override fun getLayout(): Int  = R.layout.dialog_pick_photo

    override fun onDismiss() {
    }
    private fun initViewModel() {

        mViewModel.loadingALl(-1L)
        mViewModel.mPickPhotoModel.observe(viewLifecycleOwner) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Success) {

                    val body = (it as DataResponse.DataSuccess).body
                    mLayoutManager = GridLayoutManager(
                        requireActivity(),
                        if (body.pickModelDataType == PickModelDataType.LoadAlbum) {
                            1
                        } else {
                            AT_LEAST_COLUMN
                        }
                    )
                    binding!!.rcPhoto.layoutManager = mLayoutManager
                    if (body.pickModelDataType == PickModelDataType.LoadAlbum){
                        binding!!.rcPhoto.layoutManager!!.onRestoreInstanceState(albumState)
                    }
                    adapter.update(body)
                }

            }
        }
        binding!!.viewModel = mViewModel
        binding!!.mAdapter = adapter
        mViewModel.isExitDialog.observe(this) {
            if (it) {
                adapter.cleanAll()
                dismiss()
            }
        }
    }

    private fun initRecycleView() {
        binding!!.apply {
            rcPhoto.adapter = adapter
            rcPhoto.layoutManager = mLayoutManager
        }

    }
    private fun onClickDone() {
        binding!!.tvDone.setOnClickListener {
            val data = adapter.liveSelect.value
            if (data?.empty() == true) {
                mViewModel.onClose()
            } else {
               listener!!.onPicked(data!![0].filePath)
                dismiss()
            }
        }
        adapter.liveSelect.observe(this){
            if (it.empty()){
                binding!!.tvDone.visibility = View.GONE
            }
            else{
                binding!!.tvDone.visibility = View.VISIBLE

            }
        }
    }

    override fun onClickFileItem(
        position: Int,
        pickModelDataType: PickModelDataType,
        photoModel: PhotoModel
    ) {
        if (pickModelDataType == PickModelDataType.LoadAlbum) {
            binding!!.tvAlbum.text = photoModel.albumName
            albumState =  binding!!.rcPhoto.layoutManager!!.onSaveInstanceState()
            mViewModel.loadingALl(photoModel.albumId)
        } else {
            if (Utils.checkImage(photoModel.uri, mainActivity)) {
               adapter.cleanAll()
                adapter.select(photoModel)

            } else {
                ToastUtils.getInstance(requireContext()).showToast(getString(R.string.image_error))
            }
        }
    }
    interface OnPhotoPickListener : BaseListener {
        fun onPicked(filePath:String)
    }
    companion object {
        const val AT_LEAST_COLUMN = 3
        fun create( ): PickPhotoDialog {
                return PickPhotoDialog()
        }

    }

}