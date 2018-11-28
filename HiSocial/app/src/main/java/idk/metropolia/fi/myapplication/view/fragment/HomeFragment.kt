package idk.metropolia.fi.myapplication.view.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.example.ahao9.socialevent.utils.LogUtils
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MyRecyclerViewAdapter
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.view.activity.DetailsActivity
import idk.metropolia.fi.myapplication.view.activity.ListOfEventActivity
import kotlinx.android.synthetic.main.event_card_item.*
import kotlinx.android.synthetic.main.fragment_home.*
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
class HomeFragment : Fragment() {

    private var childrenPartAdapter: MyRecyclerViewAdapter? = null
    private val childrenPartDataList = ArrayList<SearchEventsResultObject.SingleBeanData>()
    private var familyPartAdapter: MyRecyclerViewAdapter? = null
    private val familyPartDataList = ArrayList<SearchEventsResultObject.SingleBeanData>()

    private var singleBeanInSearch: SearchEventsResultObject.SingleBeanData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initGridAdapterV()
        initStaggerAdapterV()

        // 属性的初始化方法一定要在设置监听器之前
        initComponent()
        initListeners()
    }

    private fun initData() {
        loadEventsByKeywordType(keywordId = Tools.CHILDREN_ID, page_size = "5")
        loadEventsByKeywordType(keywordId = Tools.FAMILY_ID)

        childrenPartAdapter = MyRecyclerViewAdapter(context, childrenPartDataList)
        familyPartAdapter = MyRecyclerViewAdapter(context, familyPartDataList)
    }

    private fun loadEventsByKeywordType(keywordId: String, page_size: String = "10", start: String = "today", end: String = "today") {

        val mDialog = ProgressDialog(context)
        mDialog.setProgressStyle(0)
        mDialog.setCancelable(false)
        mDialog.setMessage("Loading...")
        mDialog.show()

        Networking.service.loadEventsByKeywordType(keyword = keywordId, page_size = page_size,
                start = start, end = end).enqueue(object : Callback<SearchEventsResultObject> {

            override fun onFailure(call: Call<SearchEventsResultObject>?, t: Throwable?) {
                LogUtils.e("loadEventsByKeywordType --> onFailure: ${t?.localizedMessage}")
                mDialog.dismiss()
            }

            override fun onResponse(call: Call<SearchEventsResultObject>?, response: Response<SearchEventsResultObject>?) {
                when (keywordId) {
                    Tools.CHILDREN_ID -> {
                        response?.let {
                            if (it.isSuccessful) {
                                for (i in 0 until (it.body().data.size - 1)) {
                                    childrenPartDataList.add(it.body().data[i])
                                }
                                childrenPartAdapter?.notifyDataSetChanged()

                                val dataBean = it.body().data.last()
                                singleBeanInSearch = dataBean
                                if (dataBean.images.isNotEmpty()) {
                                    Tools.displayImageOriginal(context, iv_single_event, dataBean.images.get(0).url)
                                } else {
                                    Tools.displayImageOriginal(context, iv_single_event, R.drawable.not_found)
                                }
                                tv_single_title.text = dataBean.name?.fi ?: Tools.UN_KNOWN
                                tv_single_date.text = Tools.getFormattedDateEvent(Tools.convertDateToLong(dataBean.startTime))
                            }
                            mDialog.dismiss()
                        }
                    }

                    Tools.FAMILY_ID -> {
                        response?.let {
                            if (it.isSuccessful) {
                                familyPartDataList.addAll(it.body().data)
                                familyPartAdapter?.notifyDataSetChanged()
                            }
                        }
                        mDialog.dismiss()
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
        childrenMoreBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("text", "child")
            startActivity<ListOfEventActivity>("obj" to bundle)
        }
        familyMoreBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("text", "family")
            startActivity<ListOfEventActivity>("obj" to bundle)
        }

        childrenPartAdapter?.setOnItemClickListener { _, position ->
            startActivity<DetailsActivity>("obj" to (childrenPartDataList[position] as Serializable))
        }

        iv_single_event.setOnClickListener {
            singleBeanInSearch?.let {
                startActivity<DetailsActivity>("obj" to (it as Serializable))
            }
        }

        familyPartAdapter?.setOnItemClickListener { _, position ->
            startActivity<DetailsActivity>("obj" to (familyPartDataList[position] as Serializable))
        }

        nested_scroll_view.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            myOnScrollChangeListener?.onScrollChangeListener(v, scrollX, scrollY, oldScrollX, oldScrollY)
        }
    }

    //纵向的网格布局
    private fun initGridAdapterV() {
        val layoutManager = GridLayoutManager(context, 2, GridLayout.VERTICAL, false)
        rcChildren.layoutManager = layoutManager
        rcChildren.adapter = childrenPartAdapter
        rcChildren.setHasFixedSize(true)     // 解决下滑加载时候的卡顿
    }

    //纵向的瀑布流布局
    private fun initStaggerAdapterV() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvFamily.layoutManager = layoutManager
        rvFamily.adapter = familyPartAdapter
        rvFamily.setHasFixedSize(true)
    }

    private fun initComponent() {

    }
}