package com.example.myapplication.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.Constants.Progress
import com.example.myapplication.Constants.RouteDecode
import com.example.myapplication.Model.Route
import com.example.myapplication.Model.Step
import com.example.myapplication.R
import com.example.myapplication.ViewModel.RouteViewModel
import com.example.myapplication.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    var mMap: GoogleMap? = null
    var address: List<Address>? = null
    var latLng: LatLng? = null
    var searched_latLng : LatLng? = null
    var zoomLevel = 15.0f
    var PERMISSION_ID = 7
    var locationManager: LocationManager? = null
    var latitudePlace : Double? = null
    var longitudePlace : Double? = null
    var searched_latitude : Double? = null
    var searched_longitude : Double? = null
    var addresstype: String? = null
    var jsons = "application/json"
    var lat_map : String? = null
    var long_map : String? = null
    var pincode : String? = null
    var marker1 : Marker? = null
    var marker2 : Marker? = null
    lateinit var alertdialog : Dialog
    var progress: Progress? = null
    var count = 0

    var routeViewModel : RouteViewModel? = null
    var url : String? = null

    private lateinit var binding : ActivityMapsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgress()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.cvAddress.setBackgroundResource(R.drawable.card_view_bg)

        val supportMapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        routeViewModel = ViewModelProviders.of(this).get(RouteViewModel::class.java)

        binding.changeAddress.setOnClickListener {
            this.binding.tvAddressDetails.requestFocus()
            this.binding.tvAddressDetails.setSelection(binding.tvAddressDetails.text.toString().length)
        }

        binding.deliverylocation.isCursorVisible = false

        onclicks()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

    fun locationManager(){
        locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0f, this)

    }

    fun onclicks(){

        binding.layNext.setOnClickListener {
            url()
            drawRoute()
        }

        binding.deliverylocation.setOnClickListener {
            binding.deliverylocation.isCursorVisible = true
        }

        binding.deliverylocation.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            hideKeyboard(v!!)

                            // Perform action on key press
                            val coder = Geocoder(applicationContext)
                            try {
                                val adresses = coder.getFromLocationName(
                                    "" + binding.deliverylocation.text.toString(),
                                    50
                                ) as ArrayList<Address>
                                for (add in adresses) {
                                    if (add != null) { //Controls to ensure it is right address such as country etc.
                                        val longitude = add.longitude
                                        val latitude = add.latitude
                                        searched_latitude = latitude
                                        searched_longitude = longitude
                                        getLastLocation()
                                        setMap(1)
                                    }
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            return true
                        }
                        else -> {
                        }
                    }
                }
                return false
            }
        })
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        setMap(0)
    }

    fun getAddress(latitude: Double, longitude: Double) {
        lat_map = latitude.toString()
        long_map = longitude.toString()
//        city.isEnabled = false

        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var fulladdress = ""
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                address = addresses[0]
                fulladdress = address.getAddressLine(0)
                var cities = address.getLocality()
                var state = address.getAdminArea()
                var country = address.getCountryName()
                var postalCode = address.getPostalCode()
                var knownName = address.getFeatureName()
                // this.address.text = fulladdress
                this.binding.tvAddressDetails.setText(fulladdress)
                pincode = postalCode
                if(cities == null){
                    this.binding.city.setText(state)
                }
                else{
                    this.binding.city.setText(cities)
                }

            } else {
                fulladdress = "Location not found"
            }
        }
        catch (e: IOException){
        }

    }

    private fun checkPermissions(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }


    fun setMap(value: Int){

        if (value == 0){
            if(latLng != null) {
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                marker1 = mMap!!.addMarker(MarkerOptions().position(latLng!!))
                marker1!!.isDraggable = true
                getAddress(latLng!!.latitude, latLng!!.longitude)
                mMap!!.setOnMarkerDragListener(object : OnMarkerDragListener {
                    override fun onMarkerDragStart(arg0: Marker) {

                    }
                    override fun onMarkerDragEnd(arg0: Marker) {
                        Log.d("System out", "onMarkerDragEnd...")
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
                        getAddress(arg0.position!!.latitude, arg0.position!!.longitude)
                    }

                    override fun onMarkerDrag(arg0: Marker) {

                    }
                })
            }
        }else if (value == 1){
            if (searched_latLng != null) {
                if (marker2 != null) {
                    marker2!!.remove()
                    marker2 = mMap!!.addMarker(
                        MarkerOptions().position(searched_latLng!!).title("Indore")
                    )
                    binding.layNext.visibility = View.VISIBLE
                } else {
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(searched_latLng, zoomLevel))
                    marker2 = mMap!!.addMarker(
                        MarkerOptions().position(searched_latLng!!).title("Indore")
                    )
                    binding.layNext.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        count = count+1
        if (checkPermissions()) {
            cancelProgress()
            if (isLocationEnabled()) {
                if (count == 1){
                    dialogNote()
                }
                showProgress()
                if (latitudePlace != null && longitudePlace != null){
                    cancelProgress()
                    getAddress(latitudePlace!!, longitudePlace!!)
                    latLng = LatLng(latitudePlace!!, longitudePlace!!)
                }else if (searched_latitude != null && searched_longitude != null) {
                    cancelProgress()
                    getAddress(searched_latitude!!, searched_longitude!!)
                    searched_latLng = LatLng(searched_latitude!!, searched_longitude!!)
                }else {
                    cancelProgress()
                    locationManager()
                }

            } else {
                Toast.makeText(applicationContext, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            cancelProgress()
            requestPermissions()
        }
    }

    override fun onLocationChanged(location: Location) {
        getAddress(location!!.latitude, location.longitude)
        latLng = LatLng(location.latitude, location.longitude)
        setMap(0)
        locationManager!!.removeUpdates(this)
    }

    public fun drawRoute(){
        routeViewModel!!.getroutedetails(url)
        routeViewModel!!.getLiveData()!!.observe(this, { it ->
            if (it != null) {
                var routelist = ArrayList<LatLng?>()
                if (it.status.equals("OK")) {
                    var decodelist: ArrayList<LatLng?>
                    val routeA: Route = it.routes!!.get(0)
                    Log.i("zacharia", "Legs length : " + routeA.legs!!.size)
                    if (routeA.legs!!.size > 0) {
                        val steps: List<Step>? = routeA!!.legs!!.get(0).steps
                        Log.i("zacharia", "Steps size :" + steps!!.size)
                        var step: Step
                        var location: Location
                        var polyline: String
                        for (i in steps.indices) {
                            step = steps[i]
                            routelist.add(
                                LatLng(
                                    step.start_location!!.lat,
                                    step.start_location!!.lng
                                )
                            )
                            Log.i(
                                "zacharia",
                                "Start Location :" + step.start_location!!.lat
                                    .toString() + ", " + step.start_location!!.lng
                            )
                            polyline = step.polyline!!.points!!
                            decodelist = RouteDecode.decodePoly(polyline)
                            routelist.addAll(decodelist)
                            routelist.add(LatLng(step.end_location!!.lat, step.end_location!!.lng))
                            Log.i(
                                "zacharia",
                                "End Location :" + step.end_location!!.lat
                                    .toString() + ", " + step.end_location!!.lng
                            )
                        }
                    }
                }
                Log.i("zacharia", "routelist size : " + routelist.size)
                if (routelist.size > 0) {
                    val rectLine = PolylineOptions().width(10f).color(
                        Color.RED
                    )
                    for (i in 0 until routelist.size) {
                        rectLine.add(routelist[i])
                    }
                    // Adding route on the map
                    mMap!!.addPolyline(rectLine)
                }
                Toast.makeText(applicationContext, "" + it.status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun url(){

        // Origin of route
        val str_origin =
            "origin=" + latLng!!.latitude + "," + latLng!!.longitude
        // Destination of route
        val str_dest =
            "destination=" + searched_latLng!!.latitude + "," +searched_latLng!!.longitude
        // Mode
        var directionMode = "driving"
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        url ="maps/api/directions/$output?$parameters&key=" + "AIzaSyAM8Jf37lwVj1C-rKlspoUQP6EIzqg22Xs"
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showProgress() {
        progress = Progress(this)
        progress!!.show()
    }

    private fun cancelProgress() {
        if (progress != null) {
            progress!!.dismiss()
        }
    }

    fun dialogNote(){
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        val inflater =
            LayoutInflater.from(this)
        var layout : View = inflater.inflate(R.layout.dialog_notes, null)

        var tv_notes : TextView = layout.findViewById(R.id.tv_notes)
        var tv_ok : TextView = layout.findViewById(R.id.tv_ok)

        builder.setView(layout)
        alertdialog = builder.create()
        alertdialog.setCancelable(false)

        tv_ok.setOnClickListener {
            alertdialog!!.dismiss()
        }

        alertdialog!!.show()
    }
}