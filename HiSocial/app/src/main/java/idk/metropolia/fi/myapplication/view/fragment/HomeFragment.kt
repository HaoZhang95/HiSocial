package idk.metropolia.fi.myapplication.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.example.ahao9.socialevent.httpsService.Service
import com.example.ahao9.socialevent.utils.LogUtils
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.view.activity.CategoriesActivity
import idk.metropolia.fi.myapplication.view.activity.DetailsActivity
import idk.metropolia.fi.myapplication.adapter.MyCommingSoonRVAdapter
import idk.metropolia.fi.myapplication.adapter.MyRecommendRVAdapter
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startActivity
import rx.Subscriber
import java.io.Serializable
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class HomeFragment : Fragment() {

    private var commingSoonAdapter: MyCommingSoonRVAdapter? = null
    private val commingSoonDataList = ArrayList<SingleBeanInSearch>()
    private var recommendAdapter: MyRecommendRVAdapter? = null
    private val recommendDataList = ArrayList<SingleBeanInSearch>()

    private val mLocationDatas = ArrayList<SingleEventLocationObject>()
    private lateinit var mListSubscriber: Subscriber<SearchEventsResultObject>
    private lateinit var mListLocationSubscriber: Subscriber<SingleEventLocationObject>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initGridAdapterV()
        initStaggerAdapterV()

        initComponent()
        initListeners()
    }

    private fun initData() {

        loadSearchResultByKeyword(1, "1", "1", "art")
        loadSearchResultByKeyword(1, "1", "1", "dance")
        loadSearchResultByKeyword(1, "1", "1", "music")
        loadSearchResultByKeyword(1, "1", "1", "design")

        loadSearchResultByKeyword(2, "1", "1", "play")
        loadSearchResultByKeyword(2, "1", "1", "jazz")
        loadSearchResultByKeyword(2, "1", "1", "ball")
        loadSearchResultByKeyword(2, "1", "1", "tahto")
        loadSearchResultByKeyword(2, "2", "1", "dance")
        loadSearchResultByKeyword(2, "2", "1", "music")
        loadSearchResultByKeyword(2, "2", "1", "design")
        loadSearchResultByKeyword(2, "1", "1", "life")

        commingSoonAdapter = MyCommingSoonRVAdapter(context, commingSoonDataList)
        recommendAdapter = MyRecommendRVAdapter(context, recommendDataList)
    }

    // https://api.hel.fi/linkedevents/v1/place/tprek:26429/
    // tprek:15490 --> tprek%3A15490
    private fun loadPlaceById(id: String, textView: TextView) {
        var id = id
        val splits = id.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        id = splits[splits.size - 1].replace(":", "%3A")
        LogUtils.e("location id: $id")

        mListLocationSubscriber = object : Subscriber<SingleEventLocationObject>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                LogUtils.e(e.message.toString())
            }

            override fun onNext(singleEventLocationObject: SingleEventLocationObject) {
                mLocationDatas.add(singleEventLocationObject)
                textView.text = singleEventLocationObject.name.fi
            }
        }

        // Service.loadPlaceById(mListLocationSubscriber, id)
    }

    private fun loadSearchResultByKeyword(flag: Int, page: String, page_size: String = "1", keyword: String) {

        mListSubscriber = object : Subscriber<SearchEventsResultObject>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                LogUtils.e(e.stackTrace.toString())
            }

            override fun onNext(httpsResponse: SearchEventsResultObject) {
                if (flag == 1) {
                    commingSoonDataList.addAll(httpsResponse.data)
                    commingSoonAdapter?.notifyDataSetChanged()
                } else if (flag == 2) {
                    recommendDataList.addAll(httpsResponse.data)
                    recommendAdapter?.notifyDataSetChanged()
                }
            }
        }
        Service.loadCommingSoonEvents(mListSubscriber, page, page_size,
                "event", keyword, Tools.getFormattedToday())
    }

    private fun initListeners() {
        commingSoonMoreBtn.setOnClickListener { startActivity<CategoriesActivity>() }
        recommendMoreBtn.setOnClickListener { startActivity<CategoriesActivity>() }

        commingSoonAdapter?.setOnItemClickListener { _, position ->
            // data class中的每一个属性必须实现Serializable,否则整个obj编译不通过
            startActivity<DetailsActivity>("obj" to (commingSoonDataList[position] as Serializable))
        }

        recommendAdapter?.setOnItemClickListener { _, position ->
            startActivity<DetailsActivity>("obj" to (recommendDataList[position] as Serializable))
        }
    }

    //纵向的网格布局
    private fun initGridAdapterV() {
        val layoutManager = GridLayoutManager(context, 2, GridLayout.VERTICAL, false)
        rvCommingSoon.layoutManager = layoutManager
        rvCommingSoon.adapter = commingSoonAdapter
        rvCommingSoon.setHasFixedSize(true)     // 解决下滑加载时候的卡顿
    }

    //纵向的瀑布流布局
    private fun initStaggerAdapterV() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvRecommend.layoutManager = layoutManager
        rvRecommend.adapter = recommendAdapter
        rvRecommend.setHasFixedSize(true)
    }

    private fun initComponent() {

    }
}