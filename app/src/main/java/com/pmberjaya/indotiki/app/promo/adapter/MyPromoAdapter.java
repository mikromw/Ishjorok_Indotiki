package com.pmberjaya.indotiki.app.promo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.promo.PromoListData;
import com.pmberjaya.indotiki.utilities.Utility;


import java.util.List;

/**
 * Created by Gilbert on 18/07/2017.
 */

public class MyPromoAdapter extends ArrayAdapter<PromoListData> {
    List<PromoListData> data;
    Context context;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    String nominal_promo;

    public MyPromoAdapter(Context context, int resource, List<PromoListData> promoList) {
        super(context, resource, promoList);
        data = promoList;
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        LinearLayout layout_adapter;
        TextView tv_nominal_promo;
        TextView tv_promo_text;
        TextView tv_type_service;
        TextView tv_promo_left;
        TextView tv_promocode;
        TextView tv_valid_until;
    }

    public int getItemCount() {
        return data.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.promo_list_adapter, null);

            holder.layout_adapter = (LinearLayout) convertView.findViewById(R.id.layout_adapter);
            holder.tv_nominal_promo = (TextView) convertView.findViewById(R.id.tv_nominal_promo);
            holder.tv_promo_text = (TextView) convertView.findViewById(R.id.tv_promo_text);
            holder.tv_type_service = (TextView) convertView.findViewById(R.id.tv_type_service);
            holder.tv_promo_left = (TextView) convertView.findViewById(R.id.tv_promo_left);
            holder.tv_promocode = (TextView) convertView.findViewById(R.id.tv_promocode);
            holder.tv_valid_until = (TextView) convertView.findViewById(R.id.tv_valid_until);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        nominal_promo = data.get(position).terms_km.get(data.get(position).terms_km.size()-1).getAmount();

        if (data.get(position).category_voucher.equals("1")) {
            holder.tv_nominal_promo.setText("IDR " + Utility.getInstance().convertPrice(nominal_promo));
            holder.tv_promo_text.setText(" Only");
        } else if (data.get(position).category_voucher.equals("2")) {
            holder.tv_nominal_promo.setText("IDR " + Utility.getInstance().convertPrice(nominal_promo));
            holder.tv_promo_text.setText(" Off");
        }

        holder.tv_type_service.setText(data.get(position).caption_type);
        holder.tv_promo_left.setText(" " + data.get(position).user_overall_quota_left);
        holder.tv_promocode.setText(data.get(position).code_promo);
        holder.tv_valid_until.setText(" " + data.get(position).end_promo);

        return convertView;
    }
}
