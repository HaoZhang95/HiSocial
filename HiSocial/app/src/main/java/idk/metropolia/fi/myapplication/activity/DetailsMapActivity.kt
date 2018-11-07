package idk.metropolia.fi.myapplication.activity

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.fragment.RouteDetailsFragment
import idk.metropolia.fi.myapplication.fragment.RouteFragment
import idk.metropolia.fi.myapplication.utils.Tools

class DetailsMapActivity : AppCompatActivity(), RouteFragment.OnItemClickListener, RouteDetailsFragment.OnItemClickListener {
    companion object {
        var detailsMapLat: Double = 0.0
        var detailsMapLng: Double = 0.0
    }

    private lateinit var routeFragment: RouteFragment
    private lateinit var routeDetailsFragment: RouteDetailsFragment
    private lateinit var tempFragment: Fragment
    private var mMap: GoogleMap? = null
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)   // 动画的设置
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        setContentView(R.layout.activity_details_map)

        initToolbar()
        initMapFragment(detailsMapLat,detailsMapLng)
        initComponents()
        initListeners()
    }

    private fun initListeners() {
        (findViewById<View>(R.id.fab_directions) as FloatingActionButton).setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            try {
                mMap!!.animateCamera(zoomingLocation(detailsMapLat, detailsMapLng))
            } catch (e: Exception) {
            }
        }

        bottomSheetBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
    }

    override fun onItemClick(view: View, obj: String) {
        MyToast.show(this, obj)
        switchFragment(routeDetailsFragment)
    }

    override fun onSwapItemClick(view: View, obj: String) {
        MyToast.show(this, obj)
        switchFragment(routeFragment)
    }

    private fun switchFragment(fragment: Fragment) {
        if (fragment != tempFragment) {
            if (!fragment.isAdded) {
                supportFragmentManager
                        .beginTransaction()
                        .hide(tempFragment)
                        .add(R.id.container, fragment).commit();
            } else {
                supportFragmentManager
                        .beginTransaction()
                        .hide(tempFragment)
                        .show(fragment).commit();
            }
            tempFragment = fragment;
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponents() {
        val llBottomSheet = findViewById<View>(R.id.bottom_sheet) as LinearLayout
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        routeFragment = RouteFragment()
        routeFragment.setOnItemClickListener(this)

        routeDetailsFragment = RouteDetailsFragment()
        routeDetailsFragment.setOnItemClickListener(this)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, routeFragment)
                .commit()
        tempFragment = routeFragment
    }

    private fun initMapFragment(lat: Double, lng: Double) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = Tools.configActivityMaps(googleMap)
            val markerOptions = MarkerOptions().position(LatLng(lat, lng))
            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(zoomingLocation(lat, lng))
            mMap!!.setOnMarkerClickListener {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                try {
                    mMap!!.animateCamera(zoomingLocation(lat, lng))
                } catch (e: Exception) {
                }
                true
            }
        }
    }

    private fun zoomingLocation(lat: Double, lng: Double): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 13f)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details_map, menu)
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
}
