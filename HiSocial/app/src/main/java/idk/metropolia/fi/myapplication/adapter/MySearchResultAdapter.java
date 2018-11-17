package idk.metropolia.fi.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahao9.socialevent.httpsService.Service;
import com.example.ahao9.socialevent.utils.LogUtils;

import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.*;

import java.util.ArrayList;
import java.util.List;

import idk.metropolia.fi.myapplication.R;
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject;
import idk.metropolia.fi.myapplication.utils.Tools;
import rx.Subscriber;

public class MySearchResultAdapter extends RecyclerView.Adapter<MySearchResultAdapter.MyHolder> {

    private List<SingleBeanInSearch> list;
    private Context context;
    private List mLocationDatas = new ArrayList<SingleEventLocationObject>();
    private Subscriber<SingleEventLocationObject> mListLocationSubscriber;

    public MySearchResultAdapter(Context context, List<SingleBeanInSearch> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.event_card_item_half, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder myHolder, int position) {
        myHolder.setDataAndRefreshUI(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private ImageView iv_event;
        private TextView tv_place_title;
        private TextView tv_place_brief;

        public MyHolder(View itemView) {
            super(itemView);
            iv_event = itemView.findViewById(R.id.iv_event);
            tv_place_title = itemView.findViewById(R.id.tv_place_title);
            tv_place_brief = itemView.findViewById(R.id.tv_place_brief);

            iv_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(v,getPosition());
                    }
                }
            });
        }

        public void setDataAndRefreshUI(SingleBeanInSearch dataBean){
            if (dataBean.getImages().size() > 0) {
                Tools.displayImageOriginal(context, iv_event, dataBean.getImages().get(0).getUrl());
            } else {
                Tools.displayImageOriginal(context, iv_event, R.drawable.not_found);
            }
            tv_place_title.setText(dataBean.getName().getFi());

            tv_place_brief.setText(Tools.convertToYYYYMMDD(dataBean.getStartTime()));

            // loadPlaceById(dataBean.getLocation().getId(), tv_place_brief);
        }
    }

    // https://api.hel.fi/linkedevents/v1/place/tprek:26429/
    // tprek:15490 --> tprek%3A15490
    private void loadPlaceById(String id, final TextView textView) {
        String[] splits = id.split("/");
        id = splits[splits.length - 1].replace(":", "%3A");
        LogUtils.INSTANCE.e("location id: " + id);

        mListLocationSubscriber = new Subscriber<SingleEventLocationObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.INSTANCE.e(e.getMessage());
            }

            @Override
            public void onNext(SingleEventLocationObject singleEventLocationObject) {
                textView.setText(singleEventLocationObject.getName().getFi());
            }
        };

        Service.INSTANCE.loadPlaceById(mListLocationSubscriber, id);
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    private MyItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
}