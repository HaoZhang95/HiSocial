package idk.metropolia.fi.myapplication.view.fragment

import ItineraryPlanQuery
import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MyItineraryResultsRecyclerAdapter
import idk.metropolia.fi.myapplication.httpsService.Apollo
import idk.metropolia.fi.myapplication.model.Coordinate
import idk.metropolia.fi.myapplication.utils.LocationUtils
import idk.metropolia.fi.myapplication.view.activity.DetailsMapActivity
import idk.metropolia.fi.myapplication.view.widget.ItineraryHolder
import kotlinx.android.synthetic.main.fragment_route.*
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */

private const val CODE_LOCATION_REQUEST = 0xa4
private const val REQUEST_CODE_ORIGIN = 0xa5
private const val REQUEST_CODE_DEST = 0xa6

class RouteFragment : BaseFragment(),
        MyItineraryResultsRecyclerAdapter.ItineraryResultsRecyclerAdapterListener
                        /*OnPermission*/ {
    companion object {
        var destLat: Double? = null
        var destLng: Double? = null
        var destStr: String? = null
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
    private var itineraries: MutableList<ItineraryPlanQuery.Itinerary>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_view = activity!!.findViewById(android.R.id.content)  // 自动完成的搜索框

        initComponents()
        initListeners()
    }

    private fun showItinerary() {
        LogUtils.e("fromCoordinate: ${fromCoordinate?.longitude} --> ${fromCoordinate?.latitude}")
        LogUtils.e("toCoordinate: ${toCoordinate?.longitude} --> ${toCoordinate?.latitude}")
        if (fromCoordinate != null && toCoordinate != null) {
            queryItineraryPlan()
        }
    }

    private fun initComponents() {
        if (destLat != null && destLng != null) {
            toCoordinate = Coordinate(destLng!!, destLat!!)
        }

        if (destStr != null) {
            tv_destination.text = destStr
        }

        tv_start_time.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_start_time.paint.isAntiAlias = true
        tv_date.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_date.paint.isAntiAlias = true

        tv_start_time.text = "${this.hourOfDay}:${this.minute}"
        tv_date.text = "${this.dayOfMonth}-${this.month}-${this.year}"
    }

    /**
     * define a list of listeners
     */
    private fun initListeners() {
        tv_origin.setOnClickListener { openAutocompleteActivity(REQUEST_CODE_ORIGIN) }
        tv_destination.setOnClickListener { openAutocompleteActivity(REQUEST_CODE_DEST) }

        ib_locate.setOnClickListener { getCurrentLocation() }
        ib_swap.setOnClickListener { swapItinerary() }
        tv_start_time.setOnClickListener { showTimePicker() }
        fake_time.setOnClickListener { showTimePicker() }
        tv_date.setOnClickListener { showDatePicker() }
        fake_date.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val cur_calender = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance({ view, year, monthOfYear, dayOfMonth ->
            this.year = year
            this.month = monthOfYear.plus(1)
            this.dayOfMonth = dayOfMonth
            tv_date.text = "${this.dayOfMonth}-${this.month}-${this.year}"
            queryItineraryPlan()
        }, cur_calender.get(Calendar.YEAR), cur_calender.get(Calendar.MONTH), cur_calender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = resources.getColor(R.color.colorPrimary)
        datePicker.minDate = cur_calender
        datePicker.show(activity?.fragmentManager, "datePicker")

    }

    private fun showTimePicker() {
        val cur_calender = Calendar.getInstance()

        val datePicker = TimePickerDialog.newInstance(
                { view, hourOfDay, minute, second ->
            LogUtils.e(hourOfDay.toString() + " : " + minute)
            this.hourOfDay = hourOfDay
            this.minute = minute
            tv_start_time.text = "${this.hourOfDay}:${this.minute}"
            queryItineraryPlan()
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true)
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = resources.getColor(R.color.colorPrimary)
        datePicker.show(activity?.fragmentManager, "timePicker")
    }

    private fun swapItinerary() {
        tv_origin.text = tv_destination.text.also { tv_destination.text = tv_origin.text }
        fromCoordinate = toCoordinate.also { toCoordinate = fromCoordinate }
        queryItineraryPlan()
    }

    private val requestPermissionsList = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun getCurrentLocation() {

        myRequestPermissions(context!!, requestPermissionsList, object : RequestPermissionCallBack {
            override fun granted() {
                val location = LocationUtils.getInstance(context).location
                if (location != null) {
                    fromCoordinate = Coordinate(location.longitude, location.latitude)
                    DetailsMapActivity.detailsMapFromLat = location.latitude
                    DetailsMapActivity.detailsMapFromLng = location.longitude
                    tv_origin.text = getString(R.string.my_location_okay)
                    showItinerary()
                } else {
                    tv_origin.text = getString(R.string.my_location_failed)
                    MyToast.show(context!!,getString(R.string.get_my_location_failed))
                }
            }

            override fun denied() {
                MyToast.show(context!!, getString(R.string.permission_missing_message))
            }
        })
    }

    private fun queryItineraryPlan() {
        if (fromCoordinate != null && toCoordinate != null) {
            val mDialog = ProgressDialog(context)
            mDialog.setProgressStyle(0)
            mDialog.setCancelable(false)
            mDialog.setMessage(getString(R.string.loading))
            mDialog.show()

            Apollo.apolloClient.query(ItineraryPlanQuery
                    .builder()
                    .fromLat(fromCoordinate!!.latitude)
                    .fromLong(fromCoordinate!!.longitude)
                    .toLat(toCoordinate!!.latitude)
                    .toLong(toCoordinate!!.longitude)
                    .date("$year-$month-$dayOfMonth")
                    .time("$hourOfDay:$minute")
//                    .arriveBy(arriveBy)
                    .build()
            ).enqueue(object : ApolloCall.Callback<ItineraryPlanQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    errorMessageTextView.visibility = View.VISIBLE
                    errorMessageTextView.text = getString(R.string.check_network)
                    mDialog.dismiss()
                }

                override fun onResponse(response: Response<ItineraryPlanQuery.Data>) {
                    itineraries = response.data()?.plan()?.itineraries()

                    if (itineraries?.size != 0) {
                        this@RouteFragment.activity?.runOnUiThread {
                            errorMessageTextView.visibility = View.GONE
                            itineraryResultView.visibility = View.VISIBLE
                            val adapter = MyItineraryResultsRecyclerAdapter(itineraries!!)
                            adapter.listener = this@RouteFragment
                            itineraryResultView.adapter = adapter
                            mDialog.dismiss()
                        }

                    } else {
                        this@RouteFragment.activity?.runOnUiThread {
                            errorMessageTextView.visibility = View.VISIBLE
                            itineraryResultView.visibility = View.GONE
                            errorMessageTextView.text = getString(R.string.check_network)
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
        onItemClickListener!!.onItemClick()
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun openAutocompleteActivity(request_code: Int) {
        try {
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(activity)
            startActivityForResult(intent, request_code)
        } catch (e: GooglePlayServicesRepairableException) {
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.connectionStatusCode, 0).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            val message = getString(R.string.google_service_error) + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)
            Snackbar.make(parent_view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE_ORIGIN -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = PlaceAutocomplete.getPlace(context, data)
                    (view!!.findViewById<TextView>(R.id.tv_origin)).text = place.name

                    fromCoordinate = Coordinate(place.latLng.longitude, place.latLng.latitude)
                    DetailsMapActivity.detailsMapFromLat = place.latLng.latitude
                    DetailsMapActivity.detailsMapFromLng = place.latLng.longitude
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    val status = PlaceAutocomplete.getStatus(context, data)
                    Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }

            REQUEST_CODE_DEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = PlaceAutocomplete.getPlace(context, data)
                    (view!!.findViewById<TextView>(R.id.tv_destination)).text = place.name

                    toCoordinate = Coordinate(place.latLng.longitude, place.latLng.latitude)
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    val status = PlaceAutocomplete.getStatus(context, data)
                    Snackbar.make(parent_view, status.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        showItinerary()
    }
}