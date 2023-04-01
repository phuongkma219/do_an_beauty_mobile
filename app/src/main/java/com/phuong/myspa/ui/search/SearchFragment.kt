package com.phuong.myspa.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.databinding.FragmentSearchBinding
import com.phuong.myspa.databinding.FragmentServiceBinding
import com.phuong.myspa.utils.showKeyBoard


class SearchFragment : AbsBaseFragment<FragmentSearchBinding>() {
    override fun getLayout(): Int  = R.layout.fragment_search

    override fun initView() {
      setupToolbar()
    }

    override fun initViewModel() {
        super.initViewModel()

    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.requestFocus()
        binding.searchView.setOnQueryTextFocusChangeListener { view, b ->
            if (b){
                showKeyBoard(requireContext(),view.findFocus())
            }
        }
    }

}