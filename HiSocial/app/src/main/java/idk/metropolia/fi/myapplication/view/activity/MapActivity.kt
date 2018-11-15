package idk.metropolia.fi.myapplication.view.activity

import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.transition.Explode
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MyNearByRVAdapter
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import idk.metropolia.fi.myapplication.utils.Tools

private const val HOME_FRAGMENT = 0
private const val NEAR_BY_FRAGMENT = 1
class MapActivity : AppCompatActivity() {

    private var index = 0
    private var mMap: GoogleMap? = null
    private var lat: Double = 60.170377     // default coordinate is helsinki church
    private var lng: Double = 24.952229

    private lateinit var mBehavior: BottomSheetBehavior<View>
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var bottom_sheet: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)   // 动画的设置,需要使用特定的Intent
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        setContentView(R.layout.activity_map)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        index = intent.extras["index"] as Int
        if (index == HOME_FRAGMENT) {
            // MyToast.show(this,"显示Home相关的地图")
            initMapFragmentBasedOnHomeFragment(lat, lng)
        } else if (index == NEAR_BY_FRAGMENT){
            // MyToast.show(this,"显示nearby相关的地图")
            initMapFragmentBasedOnNearByFragment()
        }

        bottom_sheet = findViewById(R.id.bottom_sheet)
        mBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
    }

    private fun initMapFragmentBasedOnHomeFragment(lat: Double, lng: Double) {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = Tools.configActivityMaps(googleMap)
            val markerOptions = MarkerOptions().position(LatLng(lat, lng))
            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(zoomingLocation(lat, lng))
            mMap!!.setOnMarkerClickListener {
                try {
                    // mMap!!.animateCamera(zoomingLocation(lat, lng))
                    showBottomSheetDialog(null)
                } catch (e: Exception) {
                }
                true
            }
        }
    }

    private fun initMapFragmentBasedOnNearByFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = Tools.configActivityMaps(googleMap)

            for (temp in MyNearByRVAdapter.LOCATION_DATA_LIST) {
                val lat = temp.position.coordinates[1]
                val lng = temp.position.coordinates[0]
                val markerOptions = MarkerOptions().position(LatLng(lat, lng)).title(temp.name.fi)
                val marker = mMap!!.addMarker(markerOptions)
                marker.tag = temp
            }

            mMap!!.moveCamera(zoomingLocation(lat, lng))        // 应当移动到用户的
            mMap!!.setOnMarkerClickListener {
                try {
                    // mMap!!.animateCamera(zoomingLocation(lat, lng))
                    showBottomSheetDialog(it.tag as SingleEventLocationObject)
                } catch (e: Exception) {
                }
                true
            }
        }
    }

    private fun zoomingLocation(lat: Double, lng: Double): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 13f)
    }

    private fun showBottomSheetDialog(obj: SingleEventLocationObject?) {
        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.sheet_event_info_floating, null)
        (view.findViewById(R.id.name) as TextView).text = obj?.streetAddress?.fi
        (view.findViewById(R.id.brief) as TextView).text = "Subtitle is here"
        (view.findViewById(R.id.description) as TextView).setText(R.string.lorem_ipsum)

        view.findViewById<ImageButton>(R.id.bt_close).setOnClickListener {
            mBottomSheetDialog?.hide()
        }

        view.findViewById<AppCompatButton>(R.id.submit_rating).setOnClickListener {
            MyToast.show(this, "More Info")
        }

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog?.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog?.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        // set background transparent
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        mBottomSheetDialog?.show()
        mBottomSheetDialog?.setOnDismissListener { mBottomSheetDialog = null }
    }

    private fun initListeners() {

    }
}