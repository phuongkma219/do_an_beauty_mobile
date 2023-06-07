package com.phuong.myspa.ui.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.slider.RangeSlider
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseBottomSheetDialogFragment
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.databinding.LayoutSearchFilterBinding
import com.phuong.myspa.utils.CacheUtils
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.Utils
import java.text.NumberFormat
import java.util.*

class BottomSheetFilter : BaseBottomSheetDialogFragment<LayoutSearchFilterBinding>() {
    private val mAdapter by lazy { CateAdapter() }
     var listener : ISearchFilter? = null
    private var price: Pair<Int, Int> =Pair(0,2000000)
    private var rate = 0f
    override fun onDismiss() {

    }


    override fun initBinding() {
        super.initBinding()
        binding.rvCategory.layoutManager = GridLayoutManager(requireContext(),2)
        binding.rvCategory.adapter = mAdapter
        val data = CacheUtils.readSaveCache(requireContext())
        mAdapter.submit(data?.data)
        mAdapter.listener = object :CateAdapter.IClickFilter{
            override fun onClickCate(position: Int, item: Category) {
                mAdapter.select(item)
            }
        }
        onSelectPrice()
        onClickRate()
        binding.btnApply.setOnClickListener {
          if (mAdapter.liveSelect.value == null){
              listener?.getFilter(data?.data,price,rate)

          }
            else{
              listener?.getFilter(mAdapter.liveSelect.value,price,rate)

          }
            dismiss()
        }

    }
    private fun onSelectPrice(){
        binding.rangSlider.stepSize = 100000F
        binding.rangSlider.valueFrom =0F
        binding.rangSlider.valueTo = 2000000F
        binding.rangSlider.setValues(0f,200000F)
        binding.rangSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("VND")
            format.format(value.toDouble())
        }
        binding.tvPMin.setText(Utils.getPrice("0"))
        binding.tvPMax.setText(Utils.getPrice("200000"))
        binding.rangSlider.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.tvPMin.setText(Utils.getPrice(rangeSlider.values[0].toInt().toString()))
            binding.tvPMax.setText(Utils.getPrice(rangeSlider.values[1].toInt().toString()))

        }
        binding.rangSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                price = Pair(slider.valueFrom.toInt(),slider.valueTo.toInt())
            }
        })
    }
    private fun onClickRate(){
        binding.rate1.setOnClickListener {
            rate = 1f
            binding.rate1.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate2.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate2.setOnClickListener {
            rate = 2f
            binding.rate2.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate3.setOnClickListener {
            rate = 3f
            binding.rate3.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate2.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate4.setOnClickListener {
            rate = 4f
            binding.rate4.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate2.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate5.setOnClickListener {
            rate = 5f
            binding.rate5.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate2.setBackgroundResource(R.color.white)
        }
    }
    interface ISearchFilter{
        fun getFilter(cates:MutableList<Category>?,price:Pair<Int,Int>?,rate:Float?)
    }

    override fun getLayoutId(): Int {
        return  R.layout.layout_search_filter
    }
    companion object{
         const val TAG = "BottomSheetFilter"
    }
}