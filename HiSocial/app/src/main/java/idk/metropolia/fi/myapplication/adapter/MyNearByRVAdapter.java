package idk.metropolia.fi.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import idk.metropolia.fi.myapplication.R;
import idk.metropolia.fi.myapplication.model.SingleBeanData;
import idk.metropolia.fi.myapplication.utils.Tools;

public class MyNearByRVAdapter extends RecyclerView.Adapter<MyNearByRVAdapter.MyHolder> {

    private List<SingleBeanData> list;
    private Context context;

    public MyNearByRVAdapter(Context context, List<SingleBeanData> list) {
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

        public void setDataAndRefreshUI(SingleBeanData dataBean){
            if (dataBean.getImages().size() > 0) {
                Tools.displayImageOriginal(context, iv_single_event, dataBean.getImages().get(0).getUrl());
            } else {
                Tools.displayImageOriginal(context, iv_single_event, R.drawable.not_found);
            }


            tv_single_title.setText(dataBean.getName().getFi());

            tv_single_date.setText(Tools.getFormattedDateEvent(Tools.convertDateToLong(dataBean.getStartTime())));

            if (dataBean.getLocation().getAddressLocality() != null) {
                tv_single_location.setText(dataBean.getLocation().getAddressLocality().getFi());
            } else {
                tv_single_location.setText("Unknown Address");
            }

        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    private MyItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
}
