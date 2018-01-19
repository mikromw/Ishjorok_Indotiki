package com.pmberjaya.indotiki.app.help;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.help.HelpCategoryData;

import java.util.List;

/**
 * Created by edwin on 15/11/2017.
 */

public class HelpServiceAdapter extends RecyclerView.Adapter<HelpServiceAdapter.ItemRowHolder>{
    private List<HelpCategoryData> data;
    private Context mContext;
    private HelpFragment fragment;


    public HelpServiceAdapter(Context context, List<HelpCategoryData> itemFoundDatas, HelpFragment fragment) {
        data = itemFoundDatas;
        this.mContext = context;
        this.fragment = fragment;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_service_adapter, null);
        HelpServiceAdapter.ItemRowHolder mh = new HelpServiceAdapter.ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(HelpServiceAdapter.ItemRowHolder holder, int i) {
        final HelpCategoryData singleItem = data.get(i);
        holder.tv_services_name.setText(singleItem.getSubcategory());


        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.categoryhelpnext(singleItem.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tv_services_name;
        protected RelativeLayout item_layout;

        public ItemRowHolder(View view) {
            super(view);
            this.tv_services_name = (TextView) view.findViewById(R.id.tv_category);
            this.item_layout = (RelativeLayout) view.findViewById(R.id.itemlayout);

        }
    }
}