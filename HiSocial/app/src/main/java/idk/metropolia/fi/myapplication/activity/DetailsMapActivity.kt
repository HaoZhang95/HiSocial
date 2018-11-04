package idk.metropolia.fi.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.sheet_map.*

class DetailsMapActivity : AppCompatActivity() {

    private var mMap: GoogleMap? = null
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    private lateinit var parent_view: View
    private val REQUEST_CODE_ORIGIN = 1
    private val REQUEST_CODE_DEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)   // 动画的设置
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        setContentView(R.layout.activity_details_map)

        parent_view = findViewById(android.R.id.content)  // 自动完成的搜索框

        lat = intent.extras["Lat"] as Double
        lng = intent.extras["Lng"] as Double

        initToolbar()
        initMapFragment(lat,lng)
        initComponent()
        Toast.makeText(this, "Swipe up bottom sheet", Toast.LENGTH_SHORT).show()
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponent() {
        // get the bottom sheet view
        val llBottomSheet = findViewById<View>(R.id.bottom_sheet) as LinearLayout

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)

        // change the state of the bottom sheet
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        // set callback for changes
        bottomSheetBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {



            }
        })

        (findViewById<View>(R.id.fab_directions) as FloatingActionButton).setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            try {
                mMap!!.animateCamera(zoomingLocation(lat, lng))
            } catch (e: Exception) {
            }
        }

        // 添加搜索框的自动完成
        tv_origin.setOnClickListener { openAutocompleteActivity(REQUEST_CODE_ORIGIN) }
        tv_destination.setOnClickListener { openAutocompleteActivity(REQUEST_CODE_DEST) }

        ib_swap.setOnClickListener { MyToast.show(this,"swap") }
        ib_more.setOnClickListener { MyToast.show(this,"more") }
        ib_addFilter.setOnClickListener { MyToast.show(this,"add filter") }
        tv_time.setOnClickListener { MyToast.show(this,"time") }
        tv_date.setOnClickListener { MyToast.show(this,"date") }

        textView3.setOnClickListener { switchFrameLayout() }

    }

    private fun switchFrameLayout() {
        MyToast.show(this,"switchFrameLayout")
    }

    private fun openAutocompleteActivity(request_code: Int) {
        try {
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this)
            startActivityForResult(intent, request_code)
        } catch (e: GooglePlayServicesRepairableException) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.connectionStatusCode, 0).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)
            Snackbar.make(parent_view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ORIGIN) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                (findViewById<TextView>(R.id.tv_origin)).text = place.name
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQUEST_CODE_DEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                (findViewById<TextView>(R.id.tv_destination)).text = place.name
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
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
