package idk.metropolia.fi.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahao9.socialevent.httpsService.Service;
import com.example.ahao9.socialevent.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import idk.metropolia.fi.myapplication.R;
import idk.metropolia.fi.myapplication.model.SearchEventsResultObject.SingleBeanInSearch;
import idk.metropolia.fi.myapplication.model.SingleEventLocationObject;
import idk.metropolia.fi.myapplication.utils.Tools;
import rx.Subscriber;

public class MyNearByRVAdapter extends RecyclerView.Adapter<MyNearByRVAdapter.MyHolder> {

    private List<SingleBeanInSearch> list;
    private Context context;
    public static List<SingleEventLocationObject> LOCATION_DATA_LIST = new ArrayList<>();
    private Subscriber<SingleEventLocationObject> mListLocationSubscriber;

    public MyNearByRVAdapter(Context context, List<SingleBeanInSearch> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.event_card_item, null);
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
        private ImageView iv_single_event;
        private TextView tv_single_date;
        private TextView tv_single_location;
        private TextView tv_single_title;

        public MyHolder(View itemView) {
            super(itemView);
            iv_single_event = itemView.findViewById(R.id.iv_single_event);
            tv_single_date = itemView.findViewById(R.id.tv_single_date);
            tv_single_location = itemView.findViewById(R.id.tv_single_location);
            tv_single_title = itemView.findViewById(R.id.tv_single_title);

            iv_single_event.setOnClickListener(new View.OnClickListener() {
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
                Tools.displayImageOriginal(context, iv_single_event, dataBean.getImages().get(0).getUrl());
            } else {
                Tools.displayImageOriginal(context, iv_single_event, R.drawable.not_found);
            }
            tv_single_title.setText(dataBean.getName().getFi());

            tv_single_date.setText(Tools.getFormattedDateEvent(Tools.convertDateToLong(dataBean.getStartTime())));

            // tv_single_location.setText("Helsinki");

            loadPlaceById(dataBean.getLocation().getId(), tv_single_location);
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
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                LogUtils.INSTANCE.e(e.getMessage());
            }

            @Override
            public void onNext(SingleEventLocationObject singleEventLocationObject) {

                LOCATION_DATA_LIST.add(singleEventLocationObject);

                if (! singleEventLocationObject.getDivisions().isEmpty()) {
                    textView.setText(singleEventLocationObject.getDivisions().get(0).getName().getFi());
                } else {
                    textView.setText(singleEventLocationObject.getName().getFi());
                }
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
