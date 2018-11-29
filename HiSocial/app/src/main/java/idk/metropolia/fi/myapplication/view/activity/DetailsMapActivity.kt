package idk.metropolia.fi.myapplication.view.activity

import android.content.Intent
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
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.R.color.*
import idk.metropolia.fi.myapplication.fragment.RouteDetailsFragment
import idk.metropolia.fi.myapplication.view.fragment.RouteFragment
import idk.metropolia.fi.myapplication.utils.PolylineUtils
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.view.widget.ItineraryHolder
import kotlinx.android.synthetic.main.activity_details_map.*
import kotlinx.android.synthetic.main.sheet_map.*

class DetailsMapActivity : AppCompatActivity(), RouteFragment.OnItemClickListener,
        RouteDetailsFragment.OnItemClickListener {
    companion object {
        var detailsMapDestLat: Double = 0.0
        var detailsMapDestLng: Double = 0.0
        var detailsMapFromLng: Double = 0.0
        var detailsMapFromLat: Double = 0.0
        var titleStr: String? = null
    }

    private lateinit var routeFragment: RouteFragment
    private lateinit var routeDetailsFragment: RouteDetailsFragment
    private lateinit var tempFragment: Fragment
    private var mMap: GoogleMap? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)   // 动画的设置
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        setContentView(R.layout.activity_details_map)

        initToolbar()
        initMapFragment(detailsMapDestLat, detailsMapDestLng)
        initComponents()
        initListeners()
    }

    private fun initListeners() {
        (findViewById<View>(R.id.fab_directions) as FloatingActionButton).setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            try {
                mMap!!.animateCamera(zoomingLocation(detailsMapDestLat, detailsMapDestLng))
            } catch (e: Exception) {
            }
        }

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) { }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        })
    }

    /**
     * 路线列表 -> 路线详情列表
     * 并且显示路线在地图上
     */
    override fun onItemClick() {
        switchFragment(routeDetailsFragment)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        typeLayout.visibility = View.VISIBLE

        drawPolylineOnMap()
    }

    /**
     * 路线详情列表 -> 路线列表
     */
    override fun onSwapItemClick() {
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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponents() {
        val llBottomSheet = findViewById<View>(R.id.bottom_sheet) as LinearLayout
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        routeFragment = RouteFragment()
        routeFragment.setOnItemClickListener(this)

        routeDetailsFragment = RouteDetailsFragment()
        routeDetailsFragment.setOnItemClickListener(this)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, routeFragment)
                .commit()
        tempFragment = routeFragment

        if (titleStr != null) {
            tv_sheet_map_title.text = titleStr
        }
    }

    private fun drawPolylineOnMap() {
        mMap?.clear()
        ItineraryHolder.get()?.run {
            val boundsBuilder = LatLngBounds.Builder()
            legs().forEach {
                val polylineOptions = PolylineOptions()

                when(it.mode().toString()) {

                    "BICYCLE" -> { polylineOptions.color(resources.getColor(R.color.colorBike, null))
                    }
                    "BUS"-> { polylineOptions.color(resources.getColor(R.color.colorBus, null))
                    }
                    getString(R.string.ferry)-> { polylineOptions.color(resources.getColor(R.color.colorFerry, null))
                    }
                    "FERRY"-> { polylineOptions.color(resources.getColor(R.color.colorTrain, null))
                    }
                    "RAIL"-> { polylineOptions.color(resources.getColor(R.color.colorMetro, null))
                    }
                    "TRAM" -> { polylineOptions.color(resources.getColor(R.color.colorTram, null))
                    }
                    "WALK" -> { polylineOptions.color(resources.getColor(R.color.colorWalk, null))
                    }
                }
                it.legGeometry()?.points()?.run {
                    val coordinates = PolylineUtils.decode(this)
                    coordinates.forEach {
                        polylineOptions.add(LatLng(it.latitude, it.longitude))
                        boundsBuilder.include(LatLng(it.latitude, it.longitude))
                    }
                }
                mMap?.addPolyline(polylineOptions)
            }

            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (0.12 * width).toInt()
            val cameraUpate = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), width, height, padding)
            mMap?.animateCamera(cameraUpate)
        }
        val destMarker = MarkerOptions().position(LatLng(detailsMapDestLat, detailsMapDestLng))
        val fromMarker = MarkerOptions().position(LatLng(detailsMapFromLat, detailsMapFromLng))
        destMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dest))
        fromMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location))
        mMap?.addMarker(destMarker)
        mMap?.addMarker(fromMarker)
    }

    private fun initMapFragment(lat: Double, lng: Double) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = Tools.configActivityMaps(googleMap)
            val markerOptions = MarkerOptions().position(LatLng(lat, lng))
            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(zoomingLocation(lat, lng))
            mMap!!.setOnMarkerClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_details_map, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            finish()
//        } else {
//            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }

    /**
     * 这里有一个坑, 必须在这里传递onActivityResult给newEventFragment否则
     * 直接在newEventFragment中的onActivityResult被拦截,获取不到!
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        routeFragment.onActivityResult(requestCode, resultCode, data)
    }
}
