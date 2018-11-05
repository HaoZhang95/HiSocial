package idk.metropolia.fi.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.extensions.inflate
import type.Mode

class TripStopsRecyclerAdapter(val intermediatePlaces: MutableList<ItineraryPlanQuery.IntermediatePlace>, val mode: Mode) : RecyclerView.Adapter<TripStopViewHolder>() {

    var listener: TripStopsRecyclerAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TripStopViewHolder {
        val view = parent.inflate(R.layout.recycler_item_trip_stops)
        return TripStopViewHolder(view)
    }

    override fun getItemCount(): Int {
        return intermediatePlaces.size
    }

    override fun onBindViewHolder(viewHolder: TripStopViewHolder, position: Int) {
        val place = intermediatePlaces[position]
        viewHolder.place = place
        viewHolder.mode = mode.toString()
        viewHolder.view.setOnClickListener {
            listener?.onClickTripStopItem(position)
        }
    }

    interface TripStopsRecyclerAdapterListener {
        fun onClickTripStopItem(position: Int)
    }
}