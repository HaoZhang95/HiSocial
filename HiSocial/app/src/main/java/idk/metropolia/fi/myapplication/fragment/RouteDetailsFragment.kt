package idk.metropolia.fi.myapplication.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.ItineraryHolder
import idk.metropolia.fi.myapplication.adapter.LegsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_route_details.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class RouteDetailsFragment : Fragment() {

    private val REQUEST_CODE_ORIGIN = 1
    private val REQUEST_CODE_DEST = 2
    private lateinit var parent_view: View
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_route_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        val itinerary = ItineraryHolder.get()
        val adapter = LegsRecyclerAdapter(itinerary!!.legs())

        layoutManager = LinearLayoutManager(context)
        itineraryDetailView.layoutManager = layoutManager
        itineraryDetailView.adapter = adapter
    }

    private var onItemClickListener: RouteDetailsFragment.OnItemClickListener? = null

    interface OnItemClickListener {
        fun onSwapItemClick(view: View, obj: String)
    }

    fun setOnItemClickListener(onItemClickListener: RouteDetailsFragment.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun initListeners() {
        // 添加搜索框的自动完成

        ib_back.setOnClickListener { onItemClickListener!!.onSwapItemClick(it, "go back") }
    }


}