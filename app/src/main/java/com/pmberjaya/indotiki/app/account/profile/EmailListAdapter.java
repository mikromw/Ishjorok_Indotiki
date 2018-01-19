package com.pmberjaya.indotiki.app.account.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingNew.PaymentAdapter;
import com.pmberjaya.indotiki.models.account.EmailData;

import java.util.List;

/**
 * Created by Gilbert on 01/07/2017.
 */

public class EmailListAdapter extends ArrayAdapter<EmailData> {

    List<EmailData> data;
    Context activity;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
    public EmailListAdapter(Context context, int resource,
                            List<EmailData> emailDatas) {
        super(context, resource, emailDatas);
        data = emailDatas;
        activity = context;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("RULESADAPTER", "afd");
    }

    public List<EmailData> getItemdata() {
        return data;
    }
    static class ViewHolder {
        TextView payment_item;
//        TextView date;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.account_email_list_adapter, null);
            holder.payment_item = convertView.findViewById(R.id.payment_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.payment_item.setText(data.get(position).getEmail());
        return convertView;
    }
}
