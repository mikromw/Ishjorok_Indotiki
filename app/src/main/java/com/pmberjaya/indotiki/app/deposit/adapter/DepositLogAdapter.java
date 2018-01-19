package com.pmberjaya.indotiki.app.deposit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.List;

public class DepositLogAdapter extends ArrayAdapter<DepositData> {
    List<DepositData> data;
    Context activity;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    public DepositLogAdapter(Context context, int resource,
                             List<DepositData> depositList) {
        super(context, resource, depositList);
        data = depositList;
        activity = context;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public List<DepositData> getItemdata() {
        return data;
    }
    static class ViewHolder {
        TextView waktu;
        TextView judul;
        TextView jumlah;
        TextView balance;
        ImageView deposit_media;
        LinearLayout action;
        public TextView action_text;
        public TextView information_text;
        public TextView request_id;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.deposit_log_adapter, null);
            holder.waktu = (TextView) convertView.findViewById(R.id.waktu);
            holder.information_text= (TextView) convertView.findViewById(R.id.information_text);
            holder.jumlah= (TextView) convertView.findViewById(R.id.jumlah);
            holder.balance= (TextView) convertView.findViewById(R.id.balance);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.waktu.setText(data.get(position).getTime());
        if(data.get(position).getBalance()==null){
            holder.balance.setText("Balance = Rp. 0");
        }
        else{
            holder.balance.setText("Balance = Rp. " + Utility.getInstance().convertPrice(data.get(position).getBalance()));
        }

        if (data.get(position).getAction().equals("1")) {
            holder.jumlah.setTextColor(Utility.getColor(activity.getResources(),R.color.red_700,null));
            if(data.get(position).getAmount()==null){
                holder.jumlah.setText("Rp. 0");
            }
            else{
                holder.jumlah.setText("- Rp. " + Utility.getInstance().convertPrice(data.get(position).getAmount()));
            }
        } else{
            holder.jumlah.setTextColor(Utility.getColor(activity.getResources(),R.color.green_900,null));
            if(data.get(position).getAmount()==null){
                holder.jumlah.setText("Rp. 0");
            }
            else{
                holder.jumlah.setText("+ Rp. " + Utility.getInstance().convertPrice(data.get(position).getAmount()));
            }
        }
       // int countBalance = Integer.parseInt(data.get(position).getBalance())-Integer.parseInt(data.get(position).getAmount());
        if(data.get(position).getCategoryRequest()!= null){
            holder.information_text.setText(data.get(position).getCategoryRequest());
        }else{
            holder.information_text.setText("Lainnya");
        }
        return convertView;
    }

}
