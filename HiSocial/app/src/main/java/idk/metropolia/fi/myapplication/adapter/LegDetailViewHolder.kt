package idk.metropolia.fi.myapplication.adapter

import ItineraryPlanQuery
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import idk.metropolia.fi.myapplication.R
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class LegDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val icBike: Int = R.drawable.ic_bike
    val icBus: Int = R.drawable.ic_bus
    val icFerry: Int = R.drawable.ic_ferry
    val icMetro: Int = R.drawable.ic_subway
    val icTrain: Int = R.drawable.ic_train
    val icTram: Int = R.drawable.ic_tram
    val icWalk: Int = R.drawable.ic_walk

    val colorBike: Int = R.color.colorBike
    val colorBus: Int = R.color.colorBus
    val colorFerry: Int = R.color.colorFerry
    val colorMetro: Int = R.color.colorMetro
    val colorTrain: Int = R.color.colorTrain
    val colorTram: Int = R.color.colorTram
    val colorWalk: Int = R.color.colorWalk

    lateinit var locationIcon: ImageView
    lateinit var fromNameTextView: TextView
    lateinit var legStartTimeTextView: TextView
    lateinit var progress: View
    lateinit var mode: ImageView
    lateinit var routeShortNameTextView: TextView
    lateinit var tripHeadsignTextView: TextView
    lateinit var durationOrDistanceTextView: TextView
    lateinit var toNameTextView: TextView
    lateinit var legEndTimeTextView: TextView
    lateinit var lastLocation: RelativeLayout
    lateinit var buttonShowStops: Button
    lateinit var recyclerItemStop: RecyclerView
    lateinit var locationStopCodeTextView: TextView

    private lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerItemTripStops: RecyclerView

    init {
        if (view.id == R.id.recyclerItemLeg) {
            locationIcon = view.findViewById(R.id.locationIcon)
            fromNameTextView = view.findViewById(R.id.fromNameTextView)
            legStartTimeTextView = view.findViewById(R.id.legStartTimeTextView)
            progress = view.findViewById(R.id.progress)
            mode = view.findViewById(R.id.mode)
            tripHeadsignTextView = view.findViewById(R.id.tripHeadsignTextView)
            routeShortNameTextView = view.findViewById(R.id.routeShortNameTextView)
            durationOrDistanceTextView = view.findViewById(R.id.durationOrDistanceTextView)
            lastLocation = view.findViewById(R.id.lastLocation)
            toNameTextView = view.findViewById(R.id.toNameTextView)
            legEndTimeTextView = view.findViewById(R.id.legEndTimeTextView)
            buttonShowStops = view.findViewById((R.id.buttonShowStops))
            recyclerItemStop = view.findViewById(R.id.recyclerItemStop)
            layoutManager = LinearLayoutManager(view.context)
            recyclerItemStop.layoutManager = layoutManager
            locationStopCodeTextView = view.findViewById(R.id.locationStopCodeTextView)
        }
    }

    fun getTime(time: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm")
        val date = java.util.Date(time)
        return "${sdf.format(date)}"
    }

    var leg: ItineraryPlanQuery.Leg? = null
        set(leg) {
            val fromName = leg?.from()?.name()
            val legStartTime = getTime(leg?.from()?.departureTime()!!)
            val mode = leg.mode().toString()
            val tripHeadsign = leg.trip()?.tripHeadsign()
            val routeShortName = leg.trip()?.routeShortName()
            val distance = leg.distance()
            val duration = leg.duration()
            val toName = leg.to().name()
            val legEndTime = getTime(leg.to().arrivalTime())
            val stopPlatformCode = leg.from().stop()?.platformCode()
            val stopCode = leg.from().stop()?.code()

            fromNameTextView.text = fromName

            if (fromName != "Origin") {
                locationStopCodeTextView.visibility = View.VISIBLE
                if (mode == "RAIL") {
                    if (stopPlatformCode == null) {
                        locationStopCodeTextView.text = "$stopCode"
                    } else {
                        locationStopCodeTextView.text = "Platform $stopPlatformCode"
                    }
                } else {
                    locationStopCodeTextView.text = "$stopCode"
                }
            }

            legStartTimeTextView.text = legStartTime

            when (mode) {
                "BICYCLE" -> {
                    changeMode(icBike, colorBike)
                }
                "BUS"-> {
                    changeMode(icBus, colorBus)
                }
                "FERRY"-> {
                    changeMode(icFerry, colorFerry)
                }
                "RAIL"-> {
                    changeMode(icTrain, colorTrain)
                }
                "SUBWAY"-> {
                    changeMode(icMetro, colorMetro)
                }
                "TRAM"-> {
                    changeMode(icTram, colorTram)
                }
                "WALK" -> {
                    changeMode(icWalk, colorWalk)
                }
            }

            if (mode == "WALK") {
                routeShortNameTextView.text = "$mode"
                tripHeadsignTextView.text = null
                durationOrDistanceTextView.text = convertDistance(distance!!)
            } else {
                routeShortNameTextView.text = "$mode $routeShortName"
                tripHeadsignTextView.text = "$tripHeadsign"
                durationOrDistanceTextView.text = convertDuration(duration!!)
            }

            if (toName == "Destination") {
                lastLocation.visibility = View.VISIBLE
                toNameTextView.text = toName
                legEndTimeTextView.text = legEndTime
            }

            if (leg.intermediatePlaces()!!.size != 0) {
                buttonShowStops.visibility = View.VISIBLE
                buttonShowStops.text = "${leg.intermediatePlaces()!!.size} stops"
                buttonShowStops.setOnClickListener {
                    if (buttonShowStops.text == "Hide") {
                        recyclerItemStop.visibility = View.GONE
                        buttonShowStops.text = "${leg.intermediatePlaces()!!.size} stops"
                    } else {
                        recyclerItemStop.visibility = View.VISIBLE
                        recyclerItemStop.adapter = TripStopsRecyclerAdapter(leg.intermediatePlaces()!!, leg.mode()!!)
                        buttonShowStops.text = "Hide"
                    }
                }
            }
        }

    private fun convertDistance(distance: Double): String {
        if (distance > 1000) {
            return "${"%.1f".format(distance / 1000)}km"
        } else {
            return "${distance.roundToInt()}m"
        }
    }

    private fun convertDuration(duration: Double): String {
        if (duration > 60 && duration < 3600) {
            return "${(duration / 60).toInt()} minutes"
        } else if (duration >= 3600) {
            val hours = TimeUnit.SECONDS.toHours(duration.toLong())
            val minutes = TimeUnit.SECONDS.toMinutes(duration.toLong())
            return "${hours}h ${minutes}m"
        }
        return ""
    }

    private fun changeMode(icon: Int, color: Int) {
        progress.setBackgroundResource(color)
        mode.setImageResource(icon)
    }
}