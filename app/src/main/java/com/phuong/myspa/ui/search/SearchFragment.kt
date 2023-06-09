package com.phuong.myspa.ui.search

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.search.Search
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentSearchBinding
import com.phuong.myspa.ui.detail_category.DetailCategoryFragmentDirections
import com.phuong.myspa.ui.detail_category.ShopAdapter
import com.phuong.myspa.utils.showKeyBoard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : AbsBaseFragment<FragmentSearchBinding>() {
    private val mAdapter by lazy { ShopAdapter(false) }
    private val mViewModel by viewModels<SearchViewModel>()
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var visibleThreshold = 1
    private var mLayoutManager : LinearLayoutManager? = null
    private var isFilter = false
    private var listRaw = mutableListOf<Search>()
    val timer = object: CountDownTimer(1000,500) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            mAdapter.clearData()
            binding.rvShop.visibility = View.VISIBLE
            mViewModel.dataVM = null
            mViewModel.setKeyword(binding.searchView.query.toString().trim())
            mViewModel.fetchData(false)
        }
    }
    override fun getLayout(): Int  = R.layout.fragment_search

    override fun initView() {
      setupToolbar()
        binding.viewModel = mViewModel
        mLayoutManager = LinearLayoutManager(requireContext())
        binding.rvShop.setHasFixedSize(true)
        binding.rvShop.apply {
            layoutManager = mLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()
                    if (dy > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                        if (mViewModel.dataVM != null){
                            if (!isFilter){
                                mViewModel.fetchData( true)
                            }
                        }
                    }
                }
            })
        }
        binding.rvShop.adapter = mAdapter
        mAdapter.listener = object :ShopAdapter.IOnItemClickShop{
            override fun onItemClick(item: ShopInfor, position: Int) {
                findNavController().navigate(DetailCategoryFragmentDirections.actionGlobalShopFragment(item))
            }

            override fun onItemMoreAction(view: View, item: ShopInfor, position: Int) {

            }

        }
        binding.ivFilter.setOnClickListener {
            val dialogFilter = BottomSheetFilter()
            dialogFilter.listener = object :BottomSheetFilter.ISearchFilter{
                override fun getFilter(cates: MutableList<Category>?,price:Pair<Int,Int>?, rate: Float?) {
                    val data = mutableListOf<ShopInfor>()

                    listRaw.forEach { list ->
                        if (cates?.size!! > 0) {
                            if ( list.shop.rate >= rate!!
                                && (list.average_price >= price!!.first && list.average_price < price!!.second)){
                                val check = findCommonElements(list.shop.category.toMutableList(),cates)
                                if (check){
                                    data.add(list.shop)
                                }
                            }
                        }
                        else{
                            if ( list.shop.rate >= rate!! &&(list.average_price >= price!!.first && list.average_price < price!!.second)){
                                data.add(list.shop)

                            }
                        }
                    }
                    isFilter = true
                  val a =  data.toList().distinctBy { Pair(it._id, it._id) }
                    mAdapter.submit(a)
                }

            }
            dialogFilter.show(childFragmentManager, BottomSheetFilter.TAG)

        }
    }
   private fun printUnion(A: List<String>, B: MutableList<Category>, n: Int, m: Int):Boolean{
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (A[i] == B[j]._id) {
                    return true
                }
            }
        }
        return false
    }
    fun findCommonElements(
        arr1: MutableList<String>,
        arr2: MutableList<Category>
    ) :Boolean{
        // create hashsets
        val set1: MutableSet<String> = HashSet()
        val set2: MutableSet<String> = HashSet()

        // Adding elements from array1
        for (i in arr1) {
            set1.add(i)
        }

        // Adding elements from array2
        for (i in arr2) {
            set2.add(i._id)
        }

        // use retainAll() method to
        // find common elements
        set1.retainAll(set2)
        return set1.size>0
    }

    override fun initViewModel() {
        super.initViewModel()

        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success){
                val body = (it as DataResponse.DataSuccess).body
               if (body != null && mViewModel.dataVM != null){
                   mAdapter.submitListSearch(mViewModel.getPage()>0,body)
                   body.forEach {
                       listRaw.add(it.copy())
                   }
               }
//                if (isBindingInitialized){
//                    binding.searchView.clearFocus()
//                }
            }
            else if (it.loadingStatus == LoadingStatus.Error){
                if (isBindingInitialized){
                    binding.searchView.clearFocus()
                }
            }
        }
    }



    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter.clearData()
                binding.rvShop.visibility = View.VISIBLE
                mViewModel.dataVM = null
                mViewModel.fetchData(false)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")){
                    mAdapter.clearData()
                    binding.rvShop.visibility = View.INVISIBLE
                }
              else{
                        timer.cancel()
                    timer.start()


                }
                isFilter = false
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