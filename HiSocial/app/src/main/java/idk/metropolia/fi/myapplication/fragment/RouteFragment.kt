package idk.metropolia.fi.myapplication.fragment

import ItineraryPlanQuery
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.activity.DetailsMapActivity
import idk.metropolia.fi.myapplication.adapter.Coordinate
import idk.metropolia.fi.myapplication.adapter.ItineraryHolder
import idk.metropolia.fi.myapplication.adapter.ItineraryResultsRecyclerAdapter
import idk.metropolia.fi.myapplication.httpsService.Apollo
import idk.metropolia.fi.myapplication.utils.LocationUtils
import kotlinx.android.synthetic.main.fragment_route.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
private const val REQUEST_CODE_ORIGIN = 1
private const val REQUEST_CODE_DEST = 2
class RouteFragment : Fragment(), ItineraryResultsRecyclerAdapter.ItineraryResultsRecyclerAdapterListener {
    companion object {
        var destLat:Double? = null
        var destLng:Double? = null
    }

    private lateinit var parent_view: View
    private var fromCoordinate: Coordinate? = null
    private var toCoordinate: Coordinate? = null

    private var calendar = GregorianCalendar()
    private var hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH) + 1
    private var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private var arriveBy: Boolean = false
    private var itineraries: MutableList<ItineraryPlanQuery.Itinerary>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_view = activity!!.findViewById(android.R.id.content)  // 自动完成的搜索框

        initParameters()
        initListeners()
    }

    private fun showItinerary() {
        LogUtils.e("fromCoordinate: ${fromCoordinate?.longitude} --> ${fromCoordinate?.latitude}")
        LogUtils.e("toCoordinate: ${toCoordinate?.longitude} --> ${toCoordinate?.latitude}")
        if (fromCoordinate != null && toCoordinate != null) {
            queryItineraryPlan()
        }
    }

    private fun initParameters() {
        if (destLat != null && destLng != null ) {
            toCoordinate = Coordinate(destLng!!, destLat!!)
        }
    }

    /**
     * define a list of listeners
     */
    private fun initListeners() {
        tv_origin.setOnClickListener { openAutocompleteActivity(REQUEST_CODE_ORIGIN) }
        tv_destination.setOnClickListener { openAutocompleteActivity(REQUEST_CODE_DEST) }

        ib_locate.setOnClickListener { getCurrentLocation() }
        ib_swap.setOnClickListener { MyToast.show(context!!,"swap in fragment") }
        tv_time.setOnClickListener { MyToast.show(context!!,"time") }
        tv_date.setOnClickListener { MyToast.show(context!!,"date") }
    }

    private fun getCurrentLocation() {
        val location = LocationUtils.getInstance(context).location
        if (location != null) {
            fromCoordinate = Coordinate(location.longitude, location.latitude)
            tv_origin.text = "My Location √"
            showItinerary()
        } else {
            tv_origin.text = "My Location ❌"
            toast("Get current location failed, Please type your location manually")
        }
    }

    private fun queryItineraryPlan() {
        if (fromCoordinate != null && toCoordinate != null) {
            val mDialog = ProgressDialog(context)
            mDialog.setProgressStyle(0)
            mDialog.setCancelable(false)
            mDialog.setMessage("Loading...")
            mDialog.show()

            Apollo.apolloClient.query(ItineraryPlanQuery
                    .builder()
                    .fromLat(fromCoordinate!!.latitude)
                    .fromLong(fromCoordinate!!.longitude)
                    .toLat(toCoordinate!!.latitude)
                    .toLong(toCoordinate!!.longitude)
                    .date("$year-$month-$dayOfMonth")
                    .time("$hourOfDay:$minute")
                    .arriveBy(arriveBy)
                    .build()
            ).enqueue(object : ApolloCall.Callback<ItineraryPlanQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    errorMessageTextView.visibility = View.VISIBLE
                    errorMessageTextView.text = "Cannot get route"
                    mDialog.dismiss()
                }
                override fun onResponse(response: Response<ItineraryPlanQuery.Data>) {
                    itineraries = response.data()?.plan()?.itineraries()

                    if (itineraries?.size != 0) {
                        this@RouteFragment.activity?.runOnUiThread {
                            errorMessageTextView.visibility = View.GONE
                            itineraryResultView.visibility = View.VISIBLE
                            val adapter = ItineraryResultsRecyclerAdapter(itineraries!!)
                            adapter.listener = this@RouteFragment
                            itineraryResultView.adapter = adapter
                            mDialog.dismiss()
                        }

                    } else {
                        this@RouteFragment.activity?.runOnUiThread {
                            errorMessageTextView.visibility = View.VISIBLE
                            itineraryResultView.visibility = View.GONE
                            errorMessageTextView.text = "Cannot get route"
                            mDialog.dismiss()
                        }
                    }
                }
            })
        }
    }

    override fun onClickItineraryItem(position: Int) {
        val itinerary = itineraries!![position]
        ItineraryHolder.set(itinerary)
        onItemClickListener!!.onItemClick(view!!,"啥也没传" )
    }

    private var onItemClickListener: RouteFragment.OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: String)
    }

    fun setOnItemClickListener(onItemClickListener: RouteFragment.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun openAutocompleteActivity(request_code: Int) {
        try {
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(activity)
            startActivityForResult(intent, request_code)
        } catch (e: GooglePlayServicesRepairableException) {
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.connectionStatusCode, 0).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)
            Snackbar.make(parent_view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ORIGIN) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(context, data)
                (view!!.findViewById<TextView>(R.id.tv_origin)).text = place.name

                fromCoordinate = Coordinate(place.latLng.longitude, place.latLng.latitude)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(context, data)
                Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQUEST_CODE_DEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(context, data)
                (view!!.findViewById<TextView>(R.id.tv_destination)).text = place.name

                toCoordinate = Coordinate(place.latLng.longitude, place.latLng.latitude)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(context, data)
                Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }

        showItinerary()
    }
}