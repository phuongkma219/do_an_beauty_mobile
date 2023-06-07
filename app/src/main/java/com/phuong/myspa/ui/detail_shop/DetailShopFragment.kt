package com.phuong.myspa.ui.detail_shop

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.phuong.myspa.R
import com.phuong.myspa.base.AbsBaseFragment
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.databinding.FragmentDetailShopBinding
import com.phuong.myspa.ui.report.ReportFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class DetailShopFragment:AbsBaseFragment<FragmentDetailShopBinding>(), OnMapReadyCallback {
    private var data: ShopInfor? = null
    private lateinit var mMap: GoogleMap
    private  var supportMapFragment: SupportMapFragment ? = null
    private lateinit var mViewModel:ShopViewModel

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = arguments?.getParcelable(DATA, ShopInfor::class.java)
        } else {
            data = arguments?.getParcelable(DATA)
        }
        binding.item = data
        binding.tvContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${binding.item!!.phone_number}"))
            startActivity(intent)
        }
        binding.tvReport.setOnClickListener {
            findNavController().navigate(ReportFragmentDirections.actionGlobalReportFragment(data!!._id))
        }
        supportMapFragment =childFragmentManager
            .findFragmentById(R.id.ggMap) as SupportMapFragment
        supportMapFragment!!.getMapAsync(this)
    }
    override fun getLayout(): Int = R.layout.fragment_detail_shop
    override fun initViewModel() {
        super.initViewModel()
        mViewModel = ViewModelProvider(requireActivity())[ShopViewModel::class.java]

        mViewModel.getDetailShop(data!!._id)
        mViewModel.dataLiveData.observe(this){
            if (it.loadingStatus == LoadingStatus.Success) {
                val body = (it as DataResponse.DataSuccess).body.data?.infor
                if (body != null){
                    binding.item = body
                }
                else{
                    binding.item = data
                }
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        try {
            val shop = getLocationFromAddress(requireContext(),data!!.address)
            mMap.addMarker(MarkerOptions().position(shop).title(data!!.name))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop,16f))
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        }
        catch (e:Exception){
            supportMapFragment!!.requireView().visibility = View.INVISIBLE
        }



    }
    fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {
        val coder = Geocoder(context)
        var address: List<Address>? = null
        var p1: LatLng? = null
        try {
            // May throw an IOException
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                coder.getFromLocationName(strAddress,5,object :Geocoder.GeocodeListener{
                    override fun onGeocode(p0: MutableList<Address>) {
                       address =p0
                    }

                })
            }
            else{
                address =  coder.getFromLocationName(strAddress, 5)

            }
            if (address == null) {
                return null
            }
            val location = address!![0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }
    companion object {
         const val DATA = "DATA"
        @JvmStatic
        fun newInstance(shopInfor: ShopInfor) =
            DetailShopFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, shopInfor)
                }
            }
    }
}