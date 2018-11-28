package idk.metropolia.fi.myapplication.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.*
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
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.utils.LocationUtils
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MapActivity : BaseActivity() {

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

        initToolbar()
        initData()
        initComponents()
        initListeners()
    }

    private fun initToolbar() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.map)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            showRadiusChoiceDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData() {
        loadEventsByPageNumber()
    }

    var test = mutableMapOf<Double,Double>()
    private fun loadEventsByPageNumber() {

        val mDialog = ProgressDialog(this)
        mDialog.setProgressStyle(0)
        mDialog.setCancelable(false)
        mDialog.setMessage(getString(R.string.loading))
        mDialog.show()

        for (i in 0..10) {
            Networking.service.searchEventBySize(page = (i+ 1).toString()).enqueue(object : Callback<SearchEventsResultObject> {

                override fun onFailure(call: Call<SearchEventsResultObject>?, t: Throwable?) {
                    LogUtils.e("loadEventsByKeywordType --> onFailure: ${t?.localizedMessage}")
                    MyToast.show(this@MapActivity, getString(R.string.check_network))
                    mDialog.dismiss()
                }

                override fun onResponse(call: Call<SearchEventsResultObject>?, response: Response<SearchEventsResultObject>?) {
                    response?.let {
                        if (it.isSuccessful) {
                            LogUtils.e("地图界面的获取的事件数量: ${response.body().data.size}")
                            var count = 0


                            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                            mapFragment.getMapAsync { googleMap ->
                                mMap = Tools.configActivityMaps(googleMap)

                                for (temp in response.body().data) {

                                    temp.location.position?.let {
                                        it.coordinates?.let {
                                            val lat = it[1]
                                            val lng = it[0]

                                            test.put(lat,lng)
                                            // LogUtils.e("lat: $lat --> lng: $lng")
                                            val markerOptions = MarkerOptions().position(LatLng(lat, lng)).title(temp.name?.fi ?: Tools.UN_KNOWN)
                                            val marker = mMap!!.addMarker(markerOptions)
                                            marker.tag = temp
                                            count ++
                                        }
                                    }
                                }

                                LogUtils.e("地图界面的marker数量: ${count}")
                                LogUtils.e("地图界面的test数量: ${test.size}")
                                mMap!!.animateCamera(zoomingLocation(lat, lng))        // 默认显示helsinki church
                                mMap!!.setOnMarkerClickListener {
                                    try {
                                        // mMap!!.animateCamera(zoomingLocation(lat, lng))
                                        LogUtils.e("marker点击了")
                                        showBottomSheetDialog(it.tag as SearchEventsResultObject.SingleBeanData)
                                    } catch (e: Exception) {
                                    }
                                    true
                                }
                            }
                        }

                        mDialog.dismiss()
                    }
                }
            })
        }

    }

    private fun initComponents() {
        bottom_sheet = findViewById(R.id.bottom_sheet)
        mBehavior = BottomSheetBehavior.from<View>(bottom_sheet)
    }

    private fun zoomingLocation(lat: Double, lng: Double, level: Float = 13f): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), level)
    }

    private fun showBottomSheetDialog(obj: SearchEventsResultObject.SingleBeanData) {
        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.sheet_event_info_floating, null)
        (view.findViewById(R.id.name) as TextView).text = obj.name?.fi
        (view.findViewById(R.id.brief) as TextView).text = Tools.getFormattedDateEvent(Tools.convertDateToLong(obj.startTime))
        (view.findViewById(R.id.description) as TextView).text = obj.shortDescription?.fi ?: getString(R.string.no_short_desc)
        val tvPrice = view.findViewById<TextView>(R.id.tv_price)

        if (obj.offers.isNotEmpty()) {
            tvPrice.text = if (obj.offers[0].isFree) {
                getString(R.string.free)
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
                        LogUtils.e(getString(R.string.permission_missing_message))
                    }
                }

                @SuppressLint("MissingPermission")
                override fun denied() {
                    MyToast.show(this@MapActivity, getString(R.string.permission_missing_message))
                    mMap?.isMyLocationEnabled = false;
                    mMap?.uiSettings?.isMyLocationButtonEnabled = false;
                }
            })
        }

    }

    private val raidusList = arrayOf(1000.0,
            3000.0, 5000.0, 7000.0, 10000.0,getString(R.string.remove_radius))
    private val raidusString = arrayOf(getString(R.string.one_k_meters),
            getString(R.string.three_k_meters),
            getString(R.string.five_k_meters), getString(R.string.seven_k_meters), getString(R.string.ten_k_meters),getString(R.string.remove_radius))
    private var selectedIndex = 2
    private fun showRadiusChoiceDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.radius_settings))
        builder.setSingleChoiceItems(raidusString, selectedIndex) { _, i ->
            selectedIndex = i
        }
        builder.setPositiveButton(getString(R.string.okay)) { dialogInterface, i ->
            updateCircle()
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
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