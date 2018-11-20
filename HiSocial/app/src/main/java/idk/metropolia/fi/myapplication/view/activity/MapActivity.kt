package idk.metropolia.fi.myapplication.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.transition.Explode
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SingleBeanData
import idk.metropolia.fi.myapplication.utils.LocationUtils
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.view.fragment.NearByFragment
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.include_search_bar_in_map.*
import org.jetbrains.anko.startActivity
import java.io.Serializable

private const val HOME_FRAGMENT = 0
private const val NEAR_BY_FRAGMENT = 1
class MapActivity : BaseActivity() {

    private var index = 0
    private var mMap: GoogleMap? = null
    private var lat: Double = LocationUtils.lat
    private var lng: Double = LocationUtils.lng

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
            initMapFragmentBasedOnHomeFragment(lat, lng)
        } else if (index == NEAR_BY_FRAGMENT){
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
            mMap!!.animateCamera(zoomingLocation(lat, lng))
            mMap!!.setOnMarkerClickListener {
                try {
                    // mMap!!.animateCamera(zoomingLocation(lat, lng))
                    // showBottomSheetDialog(null)
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

            for (temp in NearByFragment.NEARBY_DATA_LIST) {
                val lat = temp.location.position.coordinates[1]
                val lng = temp.location.position.coordinates[0]

                LogUtils.e("lat: $lat --> lng: $lng")
                val markerOptions = MarkerOptions().position(LatLng(lat, lng)).title(temp.name?.fi ?: Tools.UN_KNOWN)
                val marker = mMap!!.addMarker(markerOptions)
                marker.tag = temp
            }

            mMap!!.animateCamera(zoomingLocation(lat, lng))        // 默认显示helsinki church
            mMap!!.setOnMarkerClickListener {
                try {
                    // mMap!!.animateCamera(zoomingLocation(lat, lng))
                    showBottomSheetDialog(it.tag as SingleBeanData)
                } catch (e: Exception) {
                }
                true
            }
        }
    }

    private fun zoomingLocation(lat: Double, lng: Double, level: Float = 13f): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), level)
    }

    private fun showBottomSheetDialog(obj: SingleBeanData) {
        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.sheet_event_info_floating, null)
        (view.findViewById(R.id.name) as TextView).text = obj.name?.fi
        (view.findViewById(R.id.brief) as TextView).text = Tools.getFormattedDateEvent(Tools.convertDateToLong(obj.startTime))
        (view.findViewById(R.id.description) as TextView).text = obj.shortDescription?.fi ?: "No Short Description"
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)

        if (obj.offers.isNotEmpty()) {
            tvPrice.text = if (obj.offers[0].isFree) {
                "Free"
            } else {
                obj.offers[0].price?.fi ?: Tools.UN_KNOWN
            }
        } else {
            tvPrice.text = Tools.UN_KNOWN
        }

        view.findViewById<ImageButton>(R.id.bt_close).setOnClickListener {
            mBottomSheetDialog?.hide()
        }

        view.findViewById<AppCompatButton>(R.id.submit_rating).setOnClickListener {
            // MyToast.show(this, "More Info")
            startActivity<DetailsActivity>("obj" to (obj as Serializable))
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

    private val requestPermissionsList = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private var myLocation:Location? = null
    private fun initListeners() {

        fabInMap.setOnClickListener {

            myRequestPermissions(this, requestPermissionsList, object : RequestPermissionCallBack {
                override fun granted() {
                    val location = LocationUtils.getInstance(this@MapActivity).location
                    if (location != null) {
                        try {
                            mMap?.isMyLocationEnabled = true
                            mMap?.uiSettings?.isMyLocationButtonEnabled = false
                            myLocation = location
                            updateCircle()
                        } catch (e: SecurityException) {
                            LogUtils.e("Exception: ${e.message}")
                            mMap?.isMyLocationEnabled = false
                            mMap?.uiSettings?.isMyLocationButtonEnabled = false
                        }
                    } else {
                        LogUtils.e("Location permission acquisition failed, normal function is affected！")
                    }
                }

                @SuppressLint("MissingPermission")
                override fun denied() {
                    MyToast.show(this@MapActivity, "Location permission acquisition failed, normal function is affected！")
                    mMap?.isMyLocationEnabled = false;
                    mMap?.uiSettings?.isMyLocationButtonEnabled = false;
                }
            })
        }

        ib_radius_settings.setOnClickListener {
            showRadiusChoiceDialog()
        }
    }

    private val raidusList = arrayOf(1000.0,
            3000.0, 5000.0, 7000.0, 10000.0,"Remove Radius?")
    private val raidusString = arrayOf("1K meters",
            "3K meters", "5k meters", "7K meters", "10K meters","Remove Radius?")
    private var selectedIndex = 2
    private fun showRadiusChoiceDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Radius Settings")
        builder.setSingleChoiceItems(raidusString, selectedIndex) { _, i ->
            selectedIndex = i
        }
        builder.setPositiveButton("Okay") { dialogInterface, i ->
            updateCircle()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private var circle:Circle? = null
    private fun updateCircle() {
        myLocation?.let {
            circle?.remove()
            circle = mMap?.addCircle(CircleOptions()
                    .center(LatLng(it.latitude, it.longitude))
                    .strokeColor(Color.parseColor("#03A9F4"))
                    .fillColor(Color.parseColor("#1A000000"))
            )

            if (selectedIndex == 5) {
                circle?.remove()
            } else {
                circle?.radius = raidusList[selectedIndex] as Double
            }

            mMap!!.animateCamera(zoomingLocation(it.latitude, it.longitude, 10f))        // 应当移动到用户的
        }
    }
}