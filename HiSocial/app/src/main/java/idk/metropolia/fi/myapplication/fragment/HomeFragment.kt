package idk.metropolia.fi.myapplication.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ahao9.socialevent.httpsService.Service
import com.example.ahao9.socialevent.utils.LogUtils
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.R.id.*
import idk.metropolia.fi.myapplication.activity.CategoriesActivity
import idk.metropolia.fi.myapplication.activity.DetailsActivity
import idk.metropolia.fi.myapplication.adapter.MyImageSliderAdapter
import idk.metropolia.fi.myapplication.adapter.MyCommingSoonRVAdapter
import idk.metropolia.fi.myapplication.adapter.MyRecommendRVAdapter
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import idk.metropolia.fi.myapplication.utils.Tools
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.onPageChangeListener
import org.jetbrains.anko.support.v4.startActivity
import rx.Subscriber
import java.io.Serializable
import java.util.*

/**
 * @ Author     ：Hao Zhang.
 * @ Date       ：Created in 16:20 2018/10/29
 * @ Description：Build for Metropolia project
 */
class HomeFragment : Fragment(), MyImageSliderAdapter.OnItemClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var layout_dots: LinearLayout

    private lateinit var runnable: Runnable
    private val handler = Handler()

    private var commingSoonAdapter: MyCommingSoonRVAdapter? = null
    private var recommendAdapter: MyRecommendRVAdapter? = null
    private lateinit var imageSliderAdapter: MyImageSliderAdapter

    private val commingSoonDataList = ArrayList<SingleBeanInSearch>()
    private val recommendDataList = ArrayList<SingleBeanInSearch>()
    private val sliderDataList = ArrayList<SingleBeanInSearch>()

    private lateinit var mListSubscriber: Subscriber<SearchEventsResultObject>
    private val mLocationDatas = ArrayList<SingleEventLocationObject>()
    private lateinit var mListLocationSubscriber: Subscriber<SingleEventLocationObject>


    private fun loadSearchResultByKeyword(flag: Int, page: String, page_size: String = "1", keyword: String) {
        mListSubscriber = object : Subscriber<SearchEventsResultObject>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                LogUtils.e(e.stackTrace.toString())
            }

            override fun onNext(httpsResponse: SearchEventsResultObject) {
                if (flag == 0) {
                    sliderDataList.addAll(httpsResponse.data)

                    imageSliderAdapter.setItems(sliderDataList)
                    imageSliderAdapter.setOnItemClickListener(this@HomeFragment)
                    viewPager.adapter = imageSliderAdapter

                    // displaying selected image first
                    viewPager.currentItem = 0
                    addBottomDots(layout_dots, imageSliderAdapter.count, 0)
                    (view!!.findViewById<View>(R.id.title) as TextView).setText(sliderDataList[0].name?.fi ?: Tools.UN_KNOWN)
                    (view!!.findViewById<View>(R.id.brief) as TextView).setText(sliderDataList[0].startTime)

                    viewPager.onPageChangeListener {
                        onPageSelected {
                            (view!!.findViewById<View>(R.id.title) as TextView).setText(sliderDataList[it].name?.fi ?: Tools.UN_KNOWN)
                            (view!!.findViewById<View>(R.id.brief) as TextView).setText(Tools.convertToYYYYMMDD(sliderDataList[it].startTime))
                            addBottomDots(layout_dots, imageSliderAdapter.count, it)
                        }
                    }
                    startAutoSlider(imageSliderAdapter.count)

                } else if (flag == 1) {
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

        loadSearchResultByKeyword(1, "1", "1","art")
        loadSearchResultByKeyword(1, "1", "1","dance")
        loadSearchResultByKeyword(1, "1", "1","music")
        loadSearchResultByKeyword(1, "1", "1","design")

        loadSearchResultByKeyword(2, "1", "1","play")
        loadSearchResultByKeyword(2, "1", "1","jazz")
        loadSearchResultByKeyword(2, "1", "1","ball")
        loadSearchResultByKeyword(2, "1", "1","tahto")
        loadSearchResultByKeyword(2, "2", "1","dance")
        loadSearchResultByKeyword(2, "2", "1","music")
        loadSearchResultByKeyword(2, "2", "1","design")
        loadSearchResultByKeyword(2, "1", "1","life")

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

    private fun initListeners() {
        commingSoonMoreBtn.setOnClickListener { startActivity<CategoriesActivity>() }
        recommendMoreBtn.setOnClickListener { startActivity<CategoriesActivity>() }

        commingSoonAdapter?.setOnItemClickListener { view, postion ->
            startActivity<DetailsActivity>("obj" to (commingSoonDataList[postion] as Serializable))
        }

        recommendAdapter?.setOnItemClickListener { view, postion ->
            startActivity<DetailsActivity>("obj" to (recommendDataList[postion] as Serializable))
        }

    }

    // data class中的每一个属性必须实现Serializable,否则整个obj编译不通过
    override fun onItemClick(view: View, obj: SingleBeanInSearch) {
        startActivity<DetailsActivity>("obj" to (obj as Serializable))
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
        layout_dots = view!!.findViewById<View>(R.id.layout_dots) as LinearLayout
        viewPager = view!!.findViewById<View>(R.id.pager) as ViewPager
        imageSliderAdapter = MyImageSliderAdapter(activity!!, ArrayList<SingleBeanInSearch>())

        loadSearchResultByKeyword(0, "1", "5","music")
//        loadSearchResultByKeyword(0, "3", "play")
//        loadSearchResultByKeyword(0, "3", "ball")
//        loadSearchResultByKeyword(0, "3", "tahto")
//        loadSearchResultByKeyword(0, "3", "music")
    }

    private fun addBottomDots(layout_dots: LinearLayout, size: Int, current: Int) {
        val dots = arrayOfNulls<ImageView>(size)

        layout_dots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(context)
            val width_height = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 0, 10, 0)
            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(R.drawable.shape_circle_outline)
            dots[i]?.setColorFilter(ContextCompat.getColor(context!!, R.color.grey_40), PorterDuff.Mode.SRC_ATOP)
            layout_dots.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[current]?.setImageResource(R.drawable.shape_circle)
            dots[current]?.setColorFilter(ContextCompat.getColor(context!!, R.color.grey_40), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun startAutoSlider(count: Int) {
        runnable = Runnable {
            var pos = viewPager.currentItem
            pos += 1
            if (pos >= count) pos = 0
            viewPager.currentItem = pos
            handler.postDelayed(runnable, 3000)
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}