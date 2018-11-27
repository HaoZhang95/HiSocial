package idk.metropolia.fi.myapplication.view.activity

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.example.ahao9.socialevent.httpsService.Service
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MySearchResultAdapter
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.model.SearchPlacesResultObject
import idk.metropolia.fi.myapplication.utils.Tools
import idk.metropolia.fi.myapplication.view.activity.SearchActivity.Companion.LOCATION_NAME_LIST
import kotlinx.android.synthetic.main.activity_card_basic.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.io.Serializable

class ListOfEventActivity : AppCompatActivity() {

    private lateinit var search_bar: View

    private var mListAdapter: MySearchResultAdapter? = null
    private val mDatas = ArrayList<SingleBeanInSearch>()
    private lateinit var mListSubscriber: Subscriber<SearchEventsResultObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_basic)

        val bundle = intent.extras.get("obj") as Bundle
        val text = bundle["text"] as String
        val startDate = bundle["start"] as String?
        val endDate = bundle["end"] as String?
        val location = bundle["location"] as String?

        loadSearchResultByKeyword(text, startDate?: "today", startDate, location)

        initComponent()
        initData()
        initListAdapterV()
    }

    private fun initData() {
        mListAdapter = MySearchResultAdapter(this, mDatas)
        mListAdapter?.setOnItemClickListener { view, postion ->
            startActivity<DetailsActivity>("obj" to (mDatas[postion] as Serializable))
        }
    }

    // page: String, page_size: String, type: String, input: String, start: String
    private lateinit var call: retrofit2.Call<SearchEventsResultObject>
    private fun loadSearchResultByKeyword(text: String, start: String, end: String?, location: String?) {

        if (location != null && end != null) {
            call = Networking.service.searchEvent(
                    keyword = text,
                    start = start,
                    end = end,
                    location = location
            )
        } else if (location == null && end != null) {
            call = Networking.service.searchEvent(
                    keyword = text,
                    start = start,
                    end = end
            )
        } else if (end == null && location != null){
            call = Networking.service.searchEvent(
                    keyword = text,
                    start = start,
                    location = location
            )
        } else if (location == null && end == null) {
            call = Networking.service.searchEvent(
                    keyword = text,
                    start = start
            )
        }

        LogUtils.e("text: $text --> startDate: $start --> endDate: $end --> location: $location")

        val value = object : retrofit2.Callback<SearchEventsResultObject> {
            override fun onResponse(call: retrofit2.Call<SearchEventsResultObject>, response: retrofit2.Response<SearchEventsResultObject>) {

                if (response.isSuccessful) {
                    val dataList = response.body().data

                    mDatas.clear()
                    mDatas.addAll(dataList)
                    mListAdapter?.notifyDataSetChanged()

                    LogUtils.e("size: --> ${dataList.size}")
                } else {
                    MyToast.show(this@ListOfEventActivity, "Can not get any result")
                }
            }

            // this method gets called if the http call fails (no internet etc)
            override fun onFailure(call: retrofit2.Call<SearchEventsResultObject>, t: Throwable) {
                LogUtils.e("onFailure: " + t.toString())
            }
        }
        call.enqueue(value)
    }

    //纵向列表布局
    private fun initListAdapterV() {
        val layoutManger = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvListOfEvents.layoutManager = layoutManger
        rvListOfEvents.adapter = mListAdapter
    }

    private fun initComponent() {
        search_bar = findViewById(R.id.search_bar)
        nested_scroll_view.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY < oldScrollY) { // up
                animateSearchBar(false)
            }
            if (scrollY > oldScrollY) { // down
                animateSearchBar(true)
            }
        }
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    internal var isSearchBarHide = false
    private fun animateSearchBar(hide: Boolean) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return
        isSearchBarHide = hide
        val moveY = if (hide) -(2 * search_bar.height) else 0
        search_bar.animate().translationY(moveY.toFloat()).setStartDelay(100).setDuration(300).start()
    }
}
