package idk.metropolia.fi.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import idk.metropolia.fi.myapplication.extensions.inflate
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.view.widget.ItineraryResultViewHolder

class MyItineraryResultsRecyclerAdapter(val itineraries: List<ItineraryPlanQuery.Itinerary>): RecyclerView.Adapter<ItineraryResultViewHolder>() {

    var listener: ItineraryResultsRecyclerAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItineraryResultViewHolder {
        val view = parent.inflate(R.layout.recycler_item_itinerary_result)
        return ItineraryResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itineraries.size
    }

    override fun onBindViewHolder(viewHolder: ItineraryResultViewHolder, position: Int) {
        val itinerary = itineraries[position]
        viewHolder.itinerary = itinerary
        viewHolder.view.setOnClickListener {
            listener?.onClickItineraryItem(position)
        }
    }

    interface ItineraryResultsRecyclerAdapterListener {
        fun onClickItineraryItem(position: Int)
    }
}