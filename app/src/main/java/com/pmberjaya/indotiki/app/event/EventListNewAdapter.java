package com.pmberjaya.indotiki.app.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.event.EventNewData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by edwin on 11/10/2017.
 */

public class EventListNewAdapter extends ArrayAdapter<EventNewData> {
    List<EventNewData> data;
    Context activity;

    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    public EventListNewAdapter(Context context, int resource,
                                 List<EventNewData> newsList) {
        super(context, resource, newsList);
        data = newsList;
        activity = context;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        ImageView gambar;
        TextView title;
        TextView date;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_list_new_adapter, null);
            holder.gambar = convertView.findViewById(R.id.gambar);
            holder.title = convertView.findViewById(R.id.title);
            holder.date = convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PicassoLoader.loadImageEvent(activity, data.get(position).getAvatar(), 400, 250, holder.gambar);
//        Picasso.with(activity).load(data.get(position).getAvatar()).resize(300,400).into(holder.gambar);
        Glide.with(activity).load(data.get(position).getAvatar()).into(holder.gambar);
        holder.title.setText(data.get(position).getTopic());
        holder.date.setText(data.get(position).getDate());
        return convertView;
    }
}