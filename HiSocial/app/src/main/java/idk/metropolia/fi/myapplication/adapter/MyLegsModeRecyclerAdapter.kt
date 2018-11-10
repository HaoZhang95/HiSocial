package idk.metropolia.fi.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import idk.metropolia.fi.myapplication.extensions.inflate
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.view.widget.LegModeViewHolder

class MyLegsModeRecyclerAdapter(val legs: List<ItineraryPlanQuery.Leg>): RecyclerView.Adapter<LegModeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): LegModeViewHolder {
        val view = parent.inflate(R.layout.recycler_item_leg_mode)
        return LegModeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return legs.size
    }

    override fun onBindViewHolder(viewHolder: LegModeViewHolder, position: Int) {
        val leg = legs[position]
        viewHolder.leg = leg
    }
}