package idk.metropolia.fi.myapplication.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import com.example.ahao9.socialevent.utils.LogUtils
import com.example.ahao9.socialevent.utils.MyToast
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.adapter.MySearchResultAdapter
import idk.metropolia.fi.myapplication.httpsService.Networking
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.activity_card_basic.*
import org.jetbrains.anko.startActivity
import java.io.Serializable

class ListOfEventActivity : AppCompatActivity() {

    private lateinit var search_bar: View
    private lateinit var backButotn: ImageButton
    private lateinit var searchButton: ImageButton
    private lateinit var searchET: EditText

    private var mListAdapter: MySearchResultAdapter? = null
    private val mDatas = ArrayList<SearchEventsResultObject.SingleBeanData>()

    private var nextPageUrl: String? = null
    private val item_per_display = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_basic)

        val bundle = intent.extras.get("obj") as Bundle
        val text = bundle["text"] as String
        val startDate = bundle["start"] as String?
        val endDate = bundle["end"] as String?
        val location = bundle["location"] as String?

        loadSearchResultByKeyword(text, startDate?: "today", endDate, location)

        initComponent(text)
        initData()
        initListAdapterV()
        initListeners()
    }

    private fun initListeners() {
        backButotn.setOnClickListener { finish() }
        searchButton.setOnClickListener { searchAction() }

        searchET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                searchAction()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


    }

    private fun searchAction() {
        val text = searchET.text.toString().trim()
        if (text.isNotEmpty()) {
            loadSearchResultByKeyword(text,"today", null, null)
        }
    }

    // 设置搜索
    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initData() {
        mListAdapter = MySearchResultAdapter(this, mDatas)

        mListAdapter?.setOnItemClickListener(object : MySearchResultAdapter.MyItemClickListener {

            override fun onItemClick(view: View, postion: Int) {
                startActivity<DetailsActivity>("obj" to (mDatas[postion] as Serializable))
            }

        })
    }

    // page: String, page_size: String, type: String, input: String, start: String
    private lateinit var call: retrofit2.Call<SearchEventsResultObject>
    private fun loadSearchResultByKeyword(text: String, start: String, end: String?, location: String?) {

        val mDialog = ProgressDialog(this)
        mDialog.setProgressStyle(0)
        mDialog.setCancelable(false)
        mDialog.setMessage(getString(R.string.loading))
        mDialog.show()

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

                    nextPageUrl = response.body().meta.next

                    mDatas.clear()
                    mDatas.addAll(dataList)
                    mListAdapter?.notifyDataSetChanged()

                    mDialog.dismiss()
                    LogUtils.e("size: --> ${dataList.size}")

                } else {
                    MyToast.show(this@ListOfEventActivity, getString(R.string.check_network))
                    mDialog.dismiss()
                }
            }

            // this method gets called if the http call fails (no internet etc)
            override fun onFailure(call: retrofit2.Call<SearchEventsResultObject>, t: Throwable) {
                LogUtils.e("onFailure: " + t.toString())
                MyToast.show(this@ListOfEventActivity, getString(R.string.check_network))
                mDialog.dismiss()
            }
        }
        call.enqueue(value)
    }

    //纵向列表布局
    private fun initListAdapterV() {
//        val layoutManger = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//        rvListOfEvents.layoutManager = layoutManger
//        rvListOfEvents.adapter = mListAdapter


        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        rvListOfEvents.layoutManager = layoutManager
        rvListOfEvents.adapter = mListAdapter
        rvListOfEvents.setHasFixedSize(true)
    }

    private fun initComponent(text: String) {
        search_bar = findViewById(R.id.search_bar)
        backButotn = search_bar.findViewById(R.id.bt_back)
        searchButton = search_bar.findViewById(R.id.clearBtn)
        searchET = search_bar.findViewById(R.id.et_search)
        searchET.setText(text)

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
