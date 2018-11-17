package idk.metropolia.fi.myapplication.view.activity

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.example.ahao9.socialevent.httpsService.Service
import com.example.ahao9.socialevent.utils.LogUtils
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MySearchResultAdapter
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_card_basic.*
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

        val text = intent.extras.get("text") as String
        loadSearchResultByKeyword("1",text)

        initComponent()
        initData()
        initListAdapterV()
    }

    private fun initData() {
        mListAdapter = MySearchResultAdapter(this, mDatas)
        mListAdapter?.setOnItemClickListener { view, postion ->
            startActivity<DetailsActivity>( "obj" to (mDatas[postion] as Serializable))
        }
    }

    private fun loadSearchResultByKeyword(page: String, keyword: String) {
        mListSubscriber = object : Subscriber<SearchEventsResultObject>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                LogUtils.e(e.stackTrace.toString())
            }

            override fun onNext(httpsResponse: SearchEventsResultObject) {
                mDatas.clear()
                mDatas.addAll(httpsResponse.data)
                mListAdapter?.notifyDataSetChanged()
            }
        }
        Service.loadCommingSoonEvents(mListSubscriber, page, "20",
                "event", keyword, Tools.getFormattedToday())
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
