package com.phuong.myspa.ui.shop_service

import android.location.*
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.phuong.myspa.R
import com.phuong.myspa.base.BaseFullDialog
import com.phuong.myspa.databinding.FragmentGetLocationBinding
import java.util.*


class GetAddressFragment:BaseFullDialog<FragmentGetLocationBinding>(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener {
    override fun getLayout(): Int = R.layout.fragment_get_location
    private lateinit var mMap: GoogleMap
    var listener : ISaveAddress? = null
    private lateinit var mLatLng:LatLng
    override fun initView() {
        if (listener == null){
            dismiss()
        }
        binding!!.btnGetLocation.setOnClickListener {
            getLocation()
        }
        binding!!.btnSave.setOnClickListener {
           val address = getAddress(mLatLng.latitude,mLatLng.longitude)
            listener!!.getAddress(address)
            dismiss()
        }
        val map =childFragmentManager
            .findFragmentById(R.id.ggMap) as SupportMapFragment
        map.getMapAsync(this)
    }
   private fun getLocation() {

        val locationManager = getSystemService(requireContext(),LocationManager::class.java)

        val locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                mMap.clear()
                val shop = LatLng(p0.latitude,p0.longitude)
                mLatLng = shop
                mMap.addMarker(MarkerOptions().position(shop).title("Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop,16f))
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
        }

        try {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex:SecurityException) {
            ex.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(15.0f)
        mMap.setMaxZoomPreference(20.0f)
        val shop = LatLng(21.028511,105.804817)
        mLatLng = shop
        mMap.addMarker(MarkerOptions().position(shop).title("Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop,16f))
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng) {
        mMap.clear()
        mLatLng = p0
        mMap.addMarker(MarkerOptions().position(p0).title("Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p0,16f))
    }

    override fun onDismiss() {

    }
    interface ISaveAddress{
        fun getAddress(address:String)
    }
    fun getAddress(latitude:Double,longitude:Double):String{
       val geocoder = Geocoder(requireContext(), Locale.getDefault())

        var addresses: List<Address>? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude,
                longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(add: MutableList<Address>) {
                        addresses = add
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)

                    }

                })
        }
        else{
          addresses=  geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        }

        val address: Address?
        var fulladdress = ""
        if (addresses!!.isNotEmpty()) {
            address = addresses!![0]
            fulladdress = address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            var city = address.getLocality();
            var state = address.getAdminArea();
            var country = address.getCountryName();
            var postalCode = address.getPostalCode();
            var knownName = address.getFeatureName(); // Only if available else return NULL
        } else{
            fulladdress = "Location not found"
        }
        return fulladdress
    }
}