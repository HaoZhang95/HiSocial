package idk.metropolia.fi.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.ahao9.socialevent.httpsService.Service
import com.example.ahao9.socialevent.utils.LogUtils
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_details.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import idk.metropolia.fi.myapplication.fragment.RouteFragment
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import rx.Subscriber

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 22:21 2018/10/30
 * @ Description：Build for Metropolia project
 */
class DetailsActivity: AppCompatActivity(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var gmap: GoogleMap? = null
    private var parent_view: View? = null
    private var iv_details: ImageView? = null
    private var lat: Double = 60.170377     // default coordinate is helsinki church
    private var lng: Double = 24.952229
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private lateinit var obj: SingleBeanInSearch
    private lateinit var mListLocationSubscriber: Subscriber<SingleEventLocationObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        initToolbar()
        initListeners()
        initComponents(savedInstanceState)
    }

    private fun initComponents(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }
        mapView = findViewById(R.id.map)
        mapView?.onCreate(mapViewBundle)
        mapView?.getMapAsync(this)

        parent_view = findViewById(R.id.parent_view)
        iv_details = findViewById(R.id.iv_details)

        obj = intent.extras.get("obj") as SingleBeanInSearch
        tv_title.text = obj.name.fi
        DetailsMapActivity.titleStr = obj.name.fi
        LogUtils.e(obj.name.fi)

        tv_price.text = "Free"
        tv_subtitle.text = obj.shortDescription.fi
        tv_desc.text = obj.description.toString()

        if (obj.images.isNotEmpty()) {
            Tools.displayImageOriginal(this, iv_details, obj.images.get(0).url)
        } else {
            Tools.displayImageOriginal(this, iv_details, R.drawable.not_found)
        }

        if (obj.score != 0.0) {
            tv_score.text = "${obj.score.toString().slice(0..3)} / 10"
            rb_score.rating = obj.score.toFloat() / 2
        } else {
            tv_score.text = "un-known"
            rb_score.rating = 0.0f
        }
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

    override fun onMapReady(googleMap: GoogleMap?) {
        gmap = googleMap
        gmap?.setMinZoomPreference(12f)
        gmap?.setOnMarkerClickListener {
            try {
                gmap?.animateCamera(zoomingLocation(lat, lng))
            } catch (e: Exception) {
                LogUtils.e("onMapReady: ${e.localizedMessage}")
            }
            true
        }

        loadPlaceById(obj.location.id, gmap)
    }

    /**
     * tprek:15490 --> tprek%3A15490
     */
    private fun loadPlaceById(id: String, gmap: GoogleMap?) {
        var id = id
        val splits = id.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        id = splits[splits.size - 1]/*.replace(":", "%3A")*/
        mListLocationSubscriber = object : Subscriber<SingleEventLocationObject>() {
            override fun onCompleted() { }

            override fun onError(e: Throwable) {
                LogUtils.e("loadPlaceById in DetailsActivity: ${e.localizedMessage}")
            }
            override fun onNext(singleEventLocationObject: SingleEventLocationObject?) {
                singleEventLocationObject?.let {
                    if (it.position.coordinates.isNotEmpty()) {
                        RouteFragment.destLng = it.position.coordinates.get(0)
                        RouteFragment.destLat = it.position.coordinates.get(1)
                        RouteFragment.destStr = it.streetAddress.fi

                        DetailsMapActivity.detailsMapDestLng = it.position.coordinates.get(0)
                        DetailsMapActivity.detailsMapDestLat = it.position.coordinates.get(1)

                        lng = it.position.coordinates.get(0)
                        lat = it.position.coordinates.get(1)

                        tv_location.text = it.addressLocality.fi
                        val markerOptions = MarkerOptions().position(LatLng(lat, lng))
                        gmap?.addMarker(markerOptions)
                        gmap?.moveCamera(zoomingLocation(lat, lng))
                        gmap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
                    }
                }
            }
        }

        Service.loadPlaceById(mListLocationSubscriber, id)
    }

    private fun zoomingLocation(lat: Double, lng: Double): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 13f)
    }

    private fun initListeners() {
        ib_map_zoom.setOnClickListener {
            val intent = Intent(this, DetailsMapActivity::class.java)
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
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