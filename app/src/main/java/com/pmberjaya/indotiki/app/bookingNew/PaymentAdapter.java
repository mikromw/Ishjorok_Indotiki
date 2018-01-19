package com.pmberjaya.indotiki.app.bookingNew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.bookingNew.PaymentData;

import java.util.List;

/**
 * Created by rony on 14/02/2017.
 */
public class PaymentAdapter extends ArrayAdapter<PaymentData> {
    List<PaymentData> data;
    Context activity;
    //    static String theurlimage = url.urlimage();
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    public PaymentAdapter(Context context, int resource,
                                   List<PaymentData> paymentDatas) {
        super(context, resource, paymentDatas);
        data = paymentDatas;
        activity = context;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("RULESADAPTER", "afd");
    }

    public List<PaymentData> getItemdata() {
        return data;
    }
    static class ViewHolder {
        TextView payment_item;
//        TextView date;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_new_payment_adapter, null);
            holder.payment_item = (TextView) convertView.findViewById(R.id.payment_item);
//            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.payment_item.setText(data.get(position).getPaymentName());
        return convertView;
    }

}

