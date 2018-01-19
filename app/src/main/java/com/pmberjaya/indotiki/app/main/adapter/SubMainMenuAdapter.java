package com.pmberjaya.indotiki.app.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.main.SubMenuData;

import java.util.List;

/**
 * Created by Gilbert on 12/4/2017.
 */

public class SubMainMenuAdapter extends RecyclerView.Adapter<SubMainMenuAdapter.ItemRowHolder>{

    private List<SubMenuData> data;
    private Context context;

    public SubMainMenuAdapter(Context context, List<SubMenuData> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_sub_main_menu_adapter, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {

        String displayName = data.get(position).getDisplay_submenu();
        if (displayName.equals("INDO_PLN")) {
            holder.tvMenuName.setText(context.getResources().getString(R.string.pln));
        }
        else if (displayName.equals("INDO_PDAM")) {
            holder.tvMenuName.setText("PDAM");
        }
        else if (displayName.equals("INDO_BPJS")) {
            holder.tvMenuName.setText("BPJS");
        }
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        private ImageView ivLogo;
        private TextView tvMenuName;

        private ItemRowHolder(View view) {
            super(view);
            ivLogo = (ImageView) view.findViewById(R.id.ivLogo);
            tvMenuName = (TextView) view.findViewById(R.id.tvMenuName);
        }
    }
}
