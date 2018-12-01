package idk.metropolia.fi.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.ahao9.socialevent.httpsService.Service
import com.example.ahao9.socialevent.utils.LogUtils
import idk.metropolia.fi.myapplication.R
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject
import idk.metropolia.fi.myapplication.utils.Tools
import rx.Subscriber

class MySearchResultAdapter(private val context: Context,
                            private val list: List<SearchEventsResultObject.SingleBeanData>) :
        RecyclerView.Adapter<MySearchResultAdapter.MyHolder>() {

    private var mListLocationSubscriber: Subscriber<SingleEventLocationObject>? = null
    private var mOnItemClickListener: MyItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = View.inflate(context, R.layout.event_card_item_half, null)
        return MyHolder(view)
    }

    override fun onBindViewHolder(myHolder: MyHolder, position: Int) {
        myHolder.setDataAndRefreshUI(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iv_event: ImageView
        private val tv_place_title: TextView
        private val tv_place_brief: TextView

        init {
            iv_event = itemView.findViewById(R.id.iv_event)
            tv_place_title = itemView.findViewById(R.id.tv_place_title)
            tv_place_brief = itemView.findViewById(R.id.tv_place_brief)

            iv_event.setOnClickListener { v ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(v, position)
                }
            }
        }

        fun setDataAndRefreshUI(dataBean: SearchEventsResultObject.SingleBeanData) {
            if (dataBean.images.size > 0) {
                Tools.displayImageOriginal(context, iv_event, dataBean.images[0].url)
            } else {
                Tools.displayImageOriginal(context, iv_event, R.drawable.not_found)
            }
            tv_place_title.text = dataBean.name!!.fi

            tv_place_brief.text = Tools.convertToYYYYMMDD(dataBean.startTime)

            // loadPlaceById(dataBean.getLocation().getId(), tv_place_brief);
        }
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
                textView.text = singleEventLocationObject.name.fi
            }
        }

        Service.loadPlaceById(mListLocationSubscriber!!, id)
    }

    interface MyItemClickListener {
        fun onItemClick(view: View, postion: Int)
    }

    fun setOnItemClickListener(listener: MyItemClickListener) {
        this.mOnItemClickListener = listener
    }
}