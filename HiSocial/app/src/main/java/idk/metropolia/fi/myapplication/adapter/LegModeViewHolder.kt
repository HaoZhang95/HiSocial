package idk.metropolia.fi.myapplication.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import idk.metropolia.fi.myapplication.R
import kotlin.math.roundToInt

class LegModeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

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

    lateinit var legModeIcon: ImageView
    lateinit var legModeDescription: TextView

    init {
        if (view.id == R.id.recyclerItemLegMode) {
            legModeIcon = view.findViewById(R.id.legModeIcon)
            legModeDescription = view.findViewById(R.id.legModeDescription)
        }
    }

    var leg: ItineraryPlanQuery.Leg? = null
        set(leg) {
            var legMode = leg?.mode().toString()

            if (legMode != "WALK") {
                legModeDescription.text = "${leg?.trip()?.routeShortName()}"
            } else {
                if (leg?.distance() != null) {
                    val walkDistance = leg.distance()
                    if (walkDistance!! > 1000) {
                        legModeDescription.text = "${"%.1f".format(walkDistance/1000)}km"
                    } else {
                        legModeDescription.text = "${walkDistance.roundToInt()}m"
                    }
                }
            }

            when (legMode) {
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
        }

    fun changeMode(icon: Int, color: Int) {
        legModeIcon.setImageResource(icon)
        legModeDescription.setTextColor(ContextCompat.getColor(view.context, color))
    }
}