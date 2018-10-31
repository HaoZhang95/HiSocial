package idk.metropolia.fi.myapplication.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

import com.balysv.materialripple.MaterialRippleLayout

import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.*
import idk.metropolia.fi.myapplication.utils.Tools

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 23:04 2018/10/31
 * @ Description：Build for Metropolia project
 */
class MyImageSliderAdapter (act: Activity, items: List<SingleBeanInSearch>) : PagerAdapter() {

    private var act: Activity = act
    private var items: List<SingleBeanInSearch> = items

    override fun getCount(): Int {
        return this.items.size
    }

    private var onItemClickListener: MyImageSliderAdapter.OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: SingleBeanInSearch)
    }

    fun setOnItemClickListener(onItemClickListener: MyImageSliderAdapter.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun getItem(pos: Int): SingleBeanInSearch {
        return items[pos]
    }

    fun setItems(items: List<SingleBeanInSearch>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = items[position]
        val inflater = act.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_slider_image, container, false)

        val image = v.findViewById<View>(R.id.image) as ImageView
        val lyt_parent = v.findViewById<View>(R.id.lyt_parent) as MaterialRippleLayout

        if (o.images.size > 0) {
            Tools.displayImageOriginal(act, image, o.images[0].url)
        } else {
            Tools.displayImageOriginal(act, image, R.drawable.not_found)
        }

        lyt_parent.setOnClickListener { v ->
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(v, o)
            }
        }

        (container as ViewPager).addView(v)

        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as RelativeLayout)
    }
}
