package idk.metropolia.fi.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList

import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.utils.Tools

class MyPostAdapter(private val ctx: Context,
                    private val items: List<SearchEventsResultObject.SingleBeanData>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null
    private var lastPosition = -1
    private var on_attach = true

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: SearchEventsResultObject.SingleBeanData, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView
        var desc: TextView
        var step: TextView
        var lyt_parent: View

        init {
            name = v.findViewById<View>(R.id.name) as TextView
            desc = v.findViewById<View>(R.id.description) as TextView
            step = v.findViewById<View>(R.id.tv_step) as TextView
            lyt_parent = v.findViewById(R.id.lyt_parent) as View
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_post, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("onBindViewHolder", "onBindViewHolder : $position")
        if (holder is OriginalViewHolder) {

            val p = items[position]
            holder.name.text = p.name?.fi.toString()
            holder.step.text = (position + 1) .toString()
            holder.desc.text = p.description?.fi.toString()
            holder.lyt_parent.setOnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(view, items[position], position)
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}