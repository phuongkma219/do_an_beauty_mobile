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
    private var price: Pair<Int, Int> =Pair(0,500000)
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
        binding.rangSlider.stepSize = 500000F
        binding.rangSlider.valueFrom =0F
        binding.rangSlider.valueTo = 5000000F
        binding.rangSlider.setValues(0f,500000F)
        binding.rangSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("VND")
            format.format(value.toDouble())
        }
        binding.tvPMin.setText(Utils.getPrice("0"))
        binding.tvPMax.setText(Utils.getPrice("5000000"))
        binding.rangSlider.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.tvPMin.setText(Utils.getPrice(rangeSlider.values[0].toInt().toString()))
            binding.tvPMax.setText(Utils.getPrice(rangeSlider.values[1].toInt().toString()))
            price = Pair(rangeSlider.values[0].toInt(),rangeSlider.values[1].toInt())
        }
        binding.rangSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
            }
        })
    }
    private fun onClickRate(){
        binding.rate1.setOnClickListener {
            rate = 0.9f
            binding.rate1.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate2.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate2.setOnClickListener {
            rate = 1.9f
            binding.rate2.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate3.setOnClickListener {
            rate = 2.9f
            binding.rate3.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate2.setBackgroundResource(R.color.white)
            binding.rate4.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate4.setOnClickListener {
            rate = 3.9f
            binding.rate4.setBackgroundResource(R.drawable.bg_btn_rate)
            binding.rate1.setBackgroundResource(R.color.white)
            binding.rate3.setBackgroundResource(R.color.white)
            binding.rate2.setBackgroundResource(R.color.white)
            binding.rate5.setBackgroundResource(R.color.white)
        }
        binding.rate5.setOnClickListener {
            rate = 4.9f
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