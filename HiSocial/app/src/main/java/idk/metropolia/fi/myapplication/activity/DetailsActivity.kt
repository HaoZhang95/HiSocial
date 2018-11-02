package idk.metropolia.fi.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_details.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions


/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 22:21 2018/10/30
 * @ Description：Build for Metropolia project
 */
class DetailsActivity: AppCompatActivity(), OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap?) {

        gmap = googleMap
        gmap?.setMinZoomPreference(12f)

        val markerOptions = MarkerOptions().position(LatLng(lat, lng))
        gmap?.addMarker(markerOptions)
        gmap?.moveCamera(zoomingLocation(lat, lng))
        gmap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))

        gmap?.setOnMarkerClickListener {
            try {
                gmap?.animateCamera(zoomingLocation(lat, lng))
            } catch (e: Exception) {
            }

            true
        }

    }

    private var mapView: MapView? = null
    private var gmap: GoogleMap? = null
    private var parent_view: View? = null
    private var iv_details: ImageView? = null
    private var lat: Double = 37.76496792
    private var lng: Double = -122.42206407
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        parent_view = findViewById(R.id.parent_view)

        iv_details = findViewById(R.id.iv_details)
        initToolbar()
        initComponent()


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView = findViewById(R.id.map)
        mapView?.onCreate(mapViewBundle)
        mapView?.getMapAsync(this)
        initMapView(lat, lng)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState?.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState?.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView?.onSaveInstanceState(mapViewBundle)
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initMapView(lat: Double, lng: Double) {


    }

    private fun zoomingLocation(lat: Double, lng: Double): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 13f)
    }

    private fun initComponent() {
        val obj = intent.extras.get("obj") as SingleBeanInSearch
        if (obj.images.isNotEmpty()) {
            Tools.displayImageOriginal(this, iv_details, obj.images.get(0).url)
        } else {
            Tools.displayImageOriginal(this, iv_details, R.drawable.not_found)
        }
        tv_title.text = obj.name.fi

        tv_location.text = "Helsinki"
        tv_price.text = "Free"

        tv_subtitle.text = obj.shortDescription.fi
        if (obj.score != 0.0) {
            tv_score.text = "${obj.score.toString().slice(0..3)} / 10"
            rb_score.rating = obj.score.toFloat() / 2
        } else {
            tv_score.text = "un-known"
            rb_score.rating = 0.0f
        }
        tv_desc.text = obj.description.toString()

        // 设置mapview
        ib_map_zoom.setOnClickListener {

            val intent = Intent(this, DetailsMapActivity::class.java)
            intent.putExtra("Lat", lat)
            intent.putExtra("Lng", lng)
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());

            // startActivity<DetailsMapActivity>("Lat" to 37.76496792, "Lng" to -122.42206407)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

}