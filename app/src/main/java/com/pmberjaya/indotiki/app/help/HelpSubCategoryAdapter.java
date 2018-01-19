package com.pmberjaya.indotiki.app.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.help.HelpSubCategoryData;

import java.util.List;

/**
 * Created by edwin on 15/11/2017.
 */

public class HelpSubCategoryAdapter extends ArrayAdapter<HelpSubCategoryData> {
    List<HelpSubCategoryData> data;
    Context activity;
    private HelpFragment fragment;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public HelpSubCategoryAdapter(Context context, int resource,
                                  List<HelpSubCategoryData> list,HelpFragment fragment) {
        super(context, resource, list);
        data = list;
        activity = context;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment = fragment;
    }

    static class ViewHolder {
        TextView question;
        LinearLayout subMenuItemlayout;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        final HelpSubCategoryData singleItem = data.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.help_subcategory_adapter, null);
            holder.question = (TextView) convertView.findViewById(R.id.subject_question);
            holder.subMenuItemlayout = (LinearLayout)convertView.findViewById(R.id.submenuhelp_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.question.setText(data.get(position).getSubject());

        holder.subMenuItemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getHelpAnswer(singleItem.getMessage(),"subcategory",singleItem.getSubject());
            }
        });
        return convertView;
    }
}