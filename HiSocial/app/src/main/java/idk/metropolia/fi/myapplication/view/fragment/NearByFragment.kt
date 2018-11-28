package idk.metropolia.fi.myapplication.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.example.ahao9.socialevent.utils.LogUtils
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MyNearByRVAdapter
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.EventsResponse
import idk.metropolia.fi.myapplication.model.SingleBeanData
import idk.metropolia.fi.myapplication.view.activity.DetailsActivity
import kotlinx.android.synthetic.main.fragment_nearby.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class NearByFragment : Fragment() {

    private var nearByAdapter: MyNearByRVAdapter? = null
    companion object {
        var NEARBY_DATA_LIST: MutableList<SingleBeanData> = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initGridAdapterV()

        initComponent()
        initListeners()
    }

    private fun initData() {
        loadEventsByPageNumber()
        nearByAdapter = MyNearByRVAdapter(context, NEARBY_DATA_LIST)
    }

    private fun loadEventsByPageNumber(page: String = "1", page_size: String = "20", start: String = "today") {

        Networking.service.loadEventsByPageNumber(page = page, page_size = page_size,
                start = start).enqueue(object : Callback<EventsResponse> {

            override fun onFailure(call: Call<EventsResponse>?, t: Throwable?) {
                LogUtils.e("loadEventsByKeywordType --> onFailure: ${t?.localizedMessage}")
            }

            override fun onResponse(call: Call<EventsResponse>?, response: Response<EventsResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        NEARBY_DATA_LIST.addAll(it.body().dataList)
                        nearByAdapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private var myOnScrollChangeListener: MyOnScrollChangeListener? = null
    interface MyOnScrollChangeListener {
        fun onScrollChangeListener(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
    }

    fun setOnScrollChangeListener(myOnScrollChangeListener: MyOnScrollChangeListener) {
        this.myOnScrollChangeListener = myOnScrollChangeListener
    }

    private fun initListeners() {

        nearByAdapter?.setOnItemClickListener { _, position ->
            // data class中的每一个属性必须实现Serializable,否则整个obj编译不通过
            startActivity<DetailsActivity>("obj" to (NEARBY_DATA_LIST[position] as Serializable))
        }

        nested_content_nearby.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            myOnScrollChangeListener?.onScrollChangeListener(v,scrollX,scrollY,oldScrollX,oldScrollY)
        }
    }

    //纵向的网格布局
    private fun initGridAdapterV() {
        val layoutManager = GridLayoutManager(context, 1, GridLayout.VERTICAL, false)
        rvNearBy.layoutManager = layoutManager
        rvNearBy.adapter = nearByAdapter
        rvNearBy.setHasFixedSize(true)     // 解决下滑加载时候的卡顿
    }

    private fun initComponent() {
    }
}