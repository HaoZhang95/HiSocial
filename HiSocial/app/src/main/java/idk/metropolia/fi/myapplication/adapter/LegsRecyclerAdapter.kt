package idk.metropolia.fi.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import idk.metropolia.fi.myapplication.extensions.inflate
import idk.metropolia.fi.myapplication.R

class LegsRecyclerAdapter(val legs: MutableList<ItineraryPlanQuery.Leg>): RecyclerView.Adapter<LegDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): LegDetailViewHolder {
        val view = parent.inflate(R.layout.recycler_item_leg)
        return LegDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return legs.size
    }

    override fun onBindViewHolder(viewHolder: LegDetailViewHolder, position: Int) {
        val leg = legs[position]
        viewHolder.leg = leg
    }
}