package com.pmberjaya.indotiki.app.deposit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositTopUpListData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.List;

/**
 * Created by Gilbert on 13/07/2017.
 */

public class DepositTopUpAdapter extends RecyclerView.Adapter<DepositTopUpAdapter.ItemRowHolder> {

    private List<DepositTopUpListData> data;
    private Context context;
    private String nominal;

    public DepositTopUpAdapter(Context context, List<DepositTopUpListData> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deposit_top_up_list_adapter, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {

        nominal = data.get(position).getNominal();
        nominal = nominal.replace("000", "");
        if (nominal.length() == 1) {
            nominal = nominal + " Jt";
        } else {
            nominal = nominal + " Rb";
        }
        holder.tv_nominal_deposit.setText(nominal);

        if (!data.get(position).getBonus().equals("0")) {
            String cashback = data.get(position).getBonus().replace("000", "");
            if (cashback.length()==1) {
                cashback = cashback + " Jt";
            } else {
                cashback = cashback + " Rb";
            }
            holder.tv_cashback_deposit.setText(cashback);
        } else {
            holder.layout_cashback.setVisibility(View.GONE); nominal = data.get(position).getNominal();
        }
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        private TextView tv_nominal_deposit;
        private ImageView iv_cashback;
        private TextView tv_cashback_deposit;
        private TextView tv_cashback_detail;
        private LinearLayout layout_cashback;


        private ItemRowHolder(View view) {
            super(view);
            tv_nominal_deposit = (TextView) view.findViewById(R.id.tv_nominal_deposit);
            iv_cashback = (ImageView) view.findViewById(R.id.iv_cashback);
            tv_cashback_deposit = (TextView) view.findViewById(R.id.tv_cashback_deposit);
            tv_cashback_detail = (TextView) view.findViewById(R.id.tv_cashback_detail);
            layout_cashback = (LinearLayout) view.findViewById(R.id.layout_cashback);
        }
    }
}
