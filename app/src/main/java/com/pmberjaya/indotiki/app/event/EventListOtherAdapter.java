package com.pmberjaya.indotiki.app.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.event.EventNewData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by edwin on 11/10/2017.
 */

public class EventListOtherAdapter extends RecyclerView.Adapter<EventListOtherAdapter.SingleItemRowHolder> {
    private final Activity mActivity;
    private List<EventNewData> data;
    private Context mContext;
    public EventListOtherAdapter(Context mContext, List<EventNewData> foodMenuCategoryGroupDatas) {
        data = foodMenuCategoryGroupDatas;
        this.mContext = mContext;
        this.mActivity = (Activity)mContext;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_other_adapter, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        final EventNewData singleItem = data.get(i);

        PicassoLoader.loadImageEvent(this.mContext, singleItem.getAvatar(), 400, 250, holder.ivEventOther);
//        Glide.with(this.mContext).load(singleItem.getAvatar()).apply(Utility.getInstance().setGlideOptions(300,400)).into(holder.ivEventOther);
        holder.tvTitleEventOther.setText(singleItem.getTopic());
        holder.eventOtherItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mActivity, EventDetail.class);
                in.putExtra("parcelable", singleItem);
                mActivity.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        private  LinearLayout eventOtherItem;
        private ImageView ivEventOther;
        private TextView tvTitleEventOther;

        public SingleItemRowHolder(View view) {
            super(view);
            this.ivEventOther= (ImageView) view.findViewById(R.id.ivEventOther);
            this.tvTitleEventOther= (TextView) view.findViewById(R.id.tvTitleEventOther);
            this.eventOtherItem= (LinearLayout) view.findViewById(R.id.eventOtherItem);
        }
    }
}
