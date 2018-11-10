package idk.metropolia.fi.myapplication.view.widget

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MyLegsModeRecyclerAdapter
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class ItineraryResultViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerItemLegModeView: RecyclerView
    lateinit var departTimeTextView: TextView
    lateinit var firstStopTimeTextView: TextView
    lateinit var arriveTimeTextView: TextView
    lateinit var walkDistanceTextView: TextView
    lateinit var itineraryDurationTextView: TextView

    init {
        if (view.id == R.id.recyclerItemItineraryResult) {
            departTimeTextView = view.findViewById(R.id.departTimeTextView)
            firstStopTimeTextView = view.findViewById(R.id.firstStopTimeTextView)
            arriveTimeTextView = view.findViewById(R.id.arriveTimeTextView)
            walkDistanceTextView = view.findViewById(R.id.walkDistanceTextView)
            itineraryDurationTextView = view.findViewById(R.id.itineraryDurationTextView)
            recyclerItemLegModeView = view.findViewById(R.id.recyclerItemLegModeView)
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerItemLegModeView.layoutManager = layoutManager
        }
    }

    var itinerary: ItineraryPlanQuery.Itinerary? = null

        set(itinerary) {
            recyclerItemLegModeView.adapter = MyLegsModeRecyclerAdapter(itinerary!!.legs())
            departTimeTextView.text = "${getTime(itinerary.startTime()!!)}"
            arriveTimeTextView.text = "${getTime(itinerary.endTime()!!)}"

            // first stop
            for (leg in itinerary.legs()) {
                if (leg.transitLeg() == false){
                    continue
                } else {
                    firstStopTimeTextView.text = "${getTime(leg.startTime()!!)}"
                    break
                }
            }

            // walk distance
            if (itinerary.walkDistance() != null) {
                var walkDistance = itinerary.walkDistance()!!
                walkDistanceTextView.text = convertDistance(walkDistance)
            }

            // duration
            var itineraryDuration = itinerary.duration()!!
            itineraryDurationTextView.text = convertDuration(itineraryDuration.toDouble())
        }

    fun getTime(time: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm")
        val date = java.util.Date(time)
        return "${sdf.format(date)}"
    }

    fun convertDistance(distance: Double): String {
        if (distance > 1000) {
            return "${"%.1f".format(distance/1000)}km"
        } else {
            return "${distance.roundToInt()}m"
        }
    }

    fun convertDuration(duration: Double): String {
        if (duration > 60 && duration < 3600) {
            return "${(duration/60).toInt()} minutes"
        } else if (duration >= 3600) {
            val hours = TimeUnit.SECONDS.toHours(duration.toLong())
            val minutes = TimeUnit.SECONDS.toMinutes(duration.toLong())
            return "${hours}h ${minutes}m"
        }
        return ""
    }
}