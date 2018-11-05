package idk.metropolia.fi.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import idk.metropolia.fi.myapplication.R

class TripStopViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val colorBike: Int = R.color.colorBike
    val colorBus: Int = R.color.colorBus
    val colorFerry: Int = R.color.colorFerry
    val colorMetro: Int = R.color.colorMetro
    val colorTrain: Int = R.color.colorTrain
    val colorTram: Int = R.color.colorTram
    val colorWalk: Int = R.color.colorWalk

    lateinit var stopProgressView: View
    lateinit var stopNameTextView: TextView
    lateinit var stopCodeTextView: TextView
    lateinit var departureTimeAtStopTextView: TextView

    init {
        if (view.id == R.id.recyclerItemTripStops) {
            stopProgressView = view.findViewById(R.id.stopProgressView)
            stopNameTextView = view.findViewById(R.id.stopNameTextView)
            stopCodeTextView = view.findViewById(R.id.stopCodeTextView)
            departureTimeAtStopTextView = view.findViewById(R.id.departureTimeAtStopTextView)
        }
    }

    var place: ItineraryPlanQuery.IntermediatePlace? = null
        set (place) {
            stopNameTextView.text = place?.name()
            stopCodeTextView.text = place?.stop()?.code()
            departureTimeAtStopTextView.text = getTime(place?.departureTime()!!)
        }

    var mode: String? = null
        set (mode) {
            when (mode) {
                "BICYCLE" -> { changeMode(colorBike)
                }
                "BUS" -> { changeMode(colorBus)
                }
                "FERRY" -> { changeMode(colorFerry)
                }
                "RAIL" -> { changeMode(colorTrain)
                }
                "SUBWAY" -> { changeMode(colorMetro)
                }
                "TRAM" -> { changeMode(colorTram)
                }
                "WALK" -> { changeMode(colorWalk)
                }
            }
        }

    private fun changeMode(color: Int) {
        stopProgressView.setBackgroundResource(color)
    }

    fun getTime(time: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm")
        val date = java.util.Date(time)
        return "${sdf.format(date)}"
    }
}
