package com.phuong.myspa.ui.reminder

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentReminderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderFragment : AbsBaseFragment<FragmentReminderBinding>() {
    private val mAdapter by lazy { ReminderAdapter() }
    override fun getLayout(): Int = R.layout.fragment_reminder
    private val mViewModel by  viewModels<RemindersViewModel>()

    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
      binding.rvShop.adapter = mAdapter
        binding.rvShop.layoutManager = LinearLayoutManager(requireContext())
        mViewModel.getAllReminders()
        mViewModel.reminderList.observe(viewLifecycleOwner){
            if (it.data?.isNotEmpty() == true){
                mAdapter.submit(it.data)
            }
        }
    }
}