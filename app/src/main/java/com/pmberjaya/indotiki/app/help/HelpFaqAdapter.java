package com.pmberjaya.indotiki.app.help;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.help.HelpFaqData;

import java.util.List;

/**
 * Created by edwin on 15/11/2017.
 */

public class HelpFaqAdapter extends RecyclerView.Adapter<HelpFaqAdapter.ItemRowHolder>{
    private List<HelpFaqData> data;
    private Context mContext;
    private HelpFragment fragment;

    public HelpFaqAdapter(Context context, List<HelpFaqData> itemFoundDatas, HelpFragment fragment) {
        data = itemFoundDatas;
        this.mContext = context;
        this.fragment = fragment;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_faq_adapter, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {
        final HelpFaqData singleItem = data.get(i);
        holder.tv_question.setText(singleItem.getSubject());

        holder.tv_number.setText(String.valueOf(i+1));

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getHelpAnswer(singleItem.getMessage(),"faq",singleItem.getSubject());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tv_question;
        protected TextView tv_number;
        protected RelativeLayout item_layout;

        public ItemRowHolder(View view) {
            super(view);
            this.tv_question = (TextView) view.findViewById(R.id.subject_question);
            this.tv_number = (TextView) view.findViewById(R.id.tv_number);
            this.item_layout = (RelativeLayout) view.findViewById(R.id.itemlayout);
        }
    }
}
