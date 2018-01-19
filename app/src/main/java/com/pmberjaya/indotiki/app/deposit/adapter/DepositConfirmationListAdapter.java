package com.pmberjaya.indotiki.app.deposit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.deposit.DepositConfirmationDetail;
import com.pmberjaya.indotiki.app.deposit.DepositConfirmationListActivity;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by edwin on 4/7/2016.
 */
public class DepositConfirmationListAdapter extends ArrayAdapter<DepositConfirmationData> {
    List<DepositConfirmationData> data;
    Context activity;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public DepositConfirmationListAdapter(Context context, int resource,
                                          List<DepositConfirmationData> depositConfirmationDatas) {
        super(context, resource, depositConfirmationDatas);
        data = depositConfirmationDatas;
        activity = context;
        DecimalFormat df = new DecimalFormat("#.0000");
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public List<DepositConfirmationData> getItemdata() {
        return data;
    }

    static class ViewHolder {
        TextView tv_tanggal;
        TextView tv_id_deposit;
        TextView tv_jumlah;
        TextView tv_status;
        LinearLayout btn_lihat;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.deposit_confirmation_list_adapter, null);
            holder.tv_id_deposit = (TextView) convertView.findViewById(R.id.tv_id_deposit);
            holder.tv_jumlah = (TextView) convertView.findViewById(R.id.tv_jumlah);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_tanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
            holder.btn_lihat = (LinearLayout) convertView.findViewById(R.id.btn_lihat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_tanggal.setText(data.get(position).getConfirm_time());
        holder.tv_id_deposit.setText(data.get(position).getId());
        int kode_unik = Integer.parseInt(data.get(position).getDeposit_kode_unik());
        int ammount = Integer.parseInt(data.get(position).getAmount());
        int jumlah_deposit = kode_unik + ammount;
        holder.tv_jumlah.setText("Rp. " + Utility.getInstance().convertPrice(jumlah_deposit));
        Log.d("wowowo", "" + data.get(position).getStatus());
        if (data.get(position).getStatus().equals("0")) {
            holder.tv_status.setText(activity.getResources().getString(R.string.status_deposit_confirmation0));
        } else if (data.get(position).getStatus().equals("1")) {
            holder.tv_status.setText(activity.getResources().getString(R.string.status_deposit_confirmation1));
        } else if (data.get(position).getStatus().equals("2")) {
            holder.tv_status.setText(activity.getResources().getString(R.string.status_deposit_confirmation2));
        } else if (data.get(position).getStatus().equals("3")) {
            holder.tv_status.setText(activity.getResources().getString(R.string.status_deposit_confirmation3));
        }

        holder.btn_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = data.get(position).getId();
                Intent intent = new Intent(getContext(), DepositConfirmationDetail.class);
                intent.putExtra("transaction_id", id);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
