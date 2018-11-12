package idk.metropolia.fi.myapplication.view.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import idk.metropolia.fi.myapplication.R
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.jetbrains.anko.support.v4.toast

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var myView: View
    private var TAG = "hero"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_map,container,false)
        myView.mapView.onCreate(savedInstanceState)
        myView.mapView.getMapAsync(this)
        return myView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        try {
            mMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            Log.e(TAG, e.message)
        }
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        //  mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true

        getCurrentLocation()
    }

    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        try {
            val sydney = LatLng(60.221651, 24.805174)
            mMap.addMarker(MarkerOptions().position(LatLng(60.223192, 24.813059)).title("Event 1"))
            mMap.addMarker(MarkerOptions().position(LatLng(60.223380, 24.802485)).title("Event 2"))
            mMap.addMarker(MarkerOptions().position(LatLng(60.216529, 24.796765)).title("Event 3"))
            mMap.addMarker(MarkerOptions().position(LatLng(60.217885, 24.813816)).title("Event 4"))
            mMap.addMarker(MarkerOptions().position(LatLng(60.217945, 24.821462)).title("Event 5"))
            mMap.addMarker(MarkerOptions().position(LatLng(60.218307, 24.793366)).title("Event 6"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        } catch(e: SecurityException)  {
            Log.e(TAG, e.message)
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        toast(marker?.title.toString())
        return true
    }

    override fun onMyLocationClick(location: Location) {
        Log.e(TAG, "latitude: ${location.latitude} --- longitude: ${location.longitude}")
    }

    /**
     * Return false so that we don't consume the event and the default behavior still occurs
     * (the camera animates to the user's current position).
     */
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}