package com.phuong.myspa.ui.setting

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseDialog
import com.phuong.myspa.databinding.DialogLanguageBinding
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.utils.Utils


class LanguageDialog : BaseDialog<DialogLanguageBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        val code = SharedPreferenceUtils.getInstance(requireContext()).readLanguageCode()
        if (code.equals("en")){
            binding.radioButtonEng.isChecked = true
        }
        else{
            binding.radioButtonVN.isChecked = true
        }
        binding.btnOke.setOnClickListener {
            if (binding.radioButtonEng.isChecked){
                SharedPreferenceUtils.getInstance(requireContext()).writeLanguageCode("en")
                dismiss()
            }
            else{
                SharedPreferenceUtils.getInstance(requireContext()).writeLanguageCode("vn")
                dismiss()
            }
            (mainActivity)?.recreate()

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            (Utils.getScreenWidth() * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }
    override fun getLayout(): Int = R.layout.dialog_language
    companion object{
         const val TAG = "LanguageDialog"
        fun onCreate():LanguageDialog{
            return LanguageDialog()
        }
    }

}