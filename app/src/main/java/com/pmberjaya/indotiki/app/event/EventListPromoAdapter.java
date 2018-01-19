package com.pmberjaya.indotiki.app.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.event.EventPromoData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by willy on 3/23/2016.
 */
public class EventListPromoAdapter extends ArrayAdapter<EventPromoData>{
    List<EventPromoData> data;
    Context activity;
    private String endDate;
    private String inputDate;
    private String outputDate;

//    static String theurlimage = url.urlimage();
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    public EventListPromoAdapter(Context context, int resource,
                                 List<EventPromoData> newsList) {
        super(context, resource, newsList);
        data = newsList;
        activity = context;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        ImageView gambar;
        TextView judul;
        TextView tvPromoCode;
//        TextView tv_end_time;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_list_promo_adapter, null);
            holder.gambar = convertView.findViewById(R.id.gambar);
            holder.judul = convertView.findViewById(R.id.judul);
            holder.tvPromoCode = convertView.findViewById(R.id.tvPromoCode);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PicassoLoader.loadImageEvent(activity, data.get(position).getAvatar(), 400, 250, holder.gambar);
//        Picasso.with(activity).load(data.get(position).getAvatar()).resize(300,400).into(holder.gambar);
//        Glide.with(activity).load(data.get(position).getAvatar()).apply(Utility.getInstance().setGlideOptions(300,400)).into(holder.gambar);
        holder.judul.setText(data.get(position).getTitle());
        holder.tvPromoCode.setText(data.get(position).getCode_promo());
        return convertView;
    }

}
