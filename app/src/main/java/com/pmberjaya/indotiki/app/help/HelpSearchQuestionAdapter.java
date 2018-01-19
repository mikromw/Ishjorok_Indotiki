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

public class HelpSearchQuestionAdapter extends ArrayAdapter<HelpSubCategoryData> {
    List<HelpSubCategoryData> data;
    Context activity;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    private HelpFragment fragment;

    public HelpSearchQuestionAdapter(Context context, int resource,
                                   List<HelpSubCategoryData> searchlist, HelpFragment fragment) {
        super(context, resource, searchlist);
        data = searchlist;
        activity = context;
        this.fragment = fragment;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        LinearLayout itemlayout;
        TextView tv_question;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.help_search_question_adapter, null);
            holder.itemlayout = (LinearLayout)convertView.findViewById(R.id.item_layout);
            holder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_question.setText(data.get(position).getSubject());

        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getHelpAnswer(data.get(position).getMessage(),"search",data.get(position).getSubject());
            }
        });
        return convertView;
    }
}