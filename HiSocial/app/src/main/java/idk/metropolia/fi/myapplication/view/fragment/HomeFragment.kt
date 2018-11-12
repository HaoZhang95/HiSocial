package idk.metropolia.fi.myapplication.view.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.bumptech.glide.request.animation.ViewAnimation
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
import idk.metropolia.fi.myapplication.utils.ViewAnimationUtils
import kotlinx.android.synthetic.main.event_card_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
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

    private var singleBeanInSearch: SingleBeanInSearch? = null
    private lateinit var mListSubscriber: Subscriber<SearchEventsResultObject>
    private lateinit var mListLocationSubscriber: Subscriber<SingleEventLocationObject>

    private lateinit var fab: FloatingActionButton
    private var hide = false
    private lateinit var mapFragment: MapFragment
    private lateinit var fragmentTransaction: FragmentTransaction

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

        loadSearchResultByKeyword(0, "1", "1", "design")
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
                } else if (flag == 0) {
                    val dataBean = httpsResponse.data.get(0)
                    singleBeanInSearch = dataBean
                    if (dataBean.images.isNotEmpty()) {
                        Tools.displayImageOriginal(context, iv_single_event, dataBean.images.get(0).url)
                    } else {
                        Tools.displayImageOriginal(context, iv_single_event, R.drawable.not_found)
                    }
                    tv_single_title.text = dataBean.name!!.fi
                    tv_single_date.text = Tools.getFormattedDateEvent(Tools.convertDateToLong(dataBean.startTime))
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

        iv_single_event.setOnClickListener {
            singleBeanInSearch?.let {
                startActivity<DetailsActivity>("obj" to (it as Serializable))
            }
        }

        recommendAdapter?.setOnItemClickListener { _, position ->
            startActivity<DetailsActivity>("obj" to (recommendDataList[position] as Serializable))
        }

        fabInHome.setOnClickListener {
            fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

            if (!mapFragment.isHidden) {
//                if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[0]) == null) {
//                    fragmentTransaction.add(R.id.container, fragmentArray[0], fragmentTagsArray[0])
//                }
//                for (i in 0 until fragmentArray.size) {
//                    if (i == 0) {
//                        fragmentTransaction.show(fragmentArray[i])
//                    } else {
//                        if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[i]) != null) {
//                            fragmentTransaction.hide(fragmentArray[i])
//                        }
//                    }
//                }
//                fragmentTransaction.commit()

                fabInHome.setImageDrawable(resources.getDrawable(R.drawable.ic_map));

            } else {
//                if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[3]) == null) {
//                    fragmentTransaction.add(R.id.container, fragmentArray[3], fragmentTagsArray[3])
//                }
//                for (i in 0 until fragmentArray.size) {
//                    if (i == 3) {
//                        fragmentTransaction.show(fragmentArray[i])
//                    } else {
//                        if (supportFragmentManager.findFragmentByTag(fragmentTagsArray[i]) != null) {
//                            fragmentTransaction.hide(fragmentArray[i])
//                        }
//                    }
//                }
//                fragmentTransaction.commit()

                fabInHome.setImageDrawable(resources.getDrawable(R.drawable.ic_dialog_dialer));
            }
        }

//        nested_scroll_view.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            if (scrollY >= oldScrollY) { // down
//                if (hide) return@setOnScrollChangeListener
//                ViewAnimationUtils.hideFab(fab)
//                hide = true
//            } else {
//                if (!hide) return@setOnScrollChangeListener
//                ViewAnimationUtils.showFab(fab)
//                hide = false
//            }
//        }
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
        mapFragment = MapFragment()
        fab = view!!.findViewById<FloatingActionButton>(R.id.fabInHome)
    }
}