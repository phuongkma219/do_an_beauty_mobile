package com.phuong.myspa.ui.comment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.comment.Content
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentCommentBinding
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.ToastUtils
import com.phuong.myspa.utils.hideKeyBoard
import com.phuong.myspa.utils.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentFragment : AbsBaseFragment<FragmentCommentBinding>() {
    private val mAdapter by lazy { CommentAdapter() }
    private val mViewModel by viewModels<CommentViewModel>()
    private var data:ShopInfor? = null
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 1
    private var mLayoutManager : LinearLayoutManager? = null
    override fun getLayout(): Int = R.layout.fragment_comment

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = arguments?.getParcelable(DATA, ShopInfor::class.java)
        } else {
            data = arguments?.getParcelable(DATA)
        }
        val user = SharedPreferenceUtils.getInstance(requireContext()).getData()
        if (user != null){
            binding.ivAvatar.loadImageFromUrl(user.user!!.avatar!!)
        }
        else{
            binding.constEdt.visibility = View.GONE
        }
        binding.viewModel = mViewModel
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.rvShop.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()
                    if (dy > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                        if (mViewModel.dataVM != null){
                            mViewModel.fetchData( data!!._id,true)
                        }
                    }
                }
            })
        }
        binding.edtContent.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (!binding.edtContent.text!!.isEmpty()){
                        uploadComment()
                    }

                    hideKeyBoard(requireContext(),binding.edtContent)
                    return true
                }
                return false
            }

        })

    }

    private fun uploadComment() {
        val uploadComment = UploadComment(data!!._id, Content(text = binding.edtContent.text.toString().trim()))
        mViewModel.uploadComment(uploadComment)
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.fetchData(data!!._id, false)
        mViewModel.dataLiveData.observe(viewLifecycleOwner) {
            if (it.loadingStatus == LoadingStatus.Success) {
                val body = (it as DataResponse.DataSuccess).body
                mAdapter.submitList(mViewModel.getPage(data!!._id) > 0, body)
            }
        }
        mViewModel.isComment.observe(viewLifecycleOwner){
            if (it.loadingStatus == LoadingStatus.Success) {
//                binding.layoutLoading.root.visibility = View.GONE
                binding.edtContent.setText("")
                mAdapter.clearData()
                mViewModel.fetchData(data!!._id, false)
            }
            else if (it.loadingStatus == LoadingStatus.Loading){
                binding.layoutLoading.root.visibility = View.VISIBLE
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                ToastUtils.getInstance(requireContext()).showToast(resources.getString(R.string.error_please_try_again))
            }
        }
    }

    companion object {
        const val DATA = "KEY_DATA"
        @JvmStatic
        fun newInstance(data: ShopInfor) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
    }

}