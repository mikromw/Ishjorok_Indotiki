package com.pmberjaya.indotiki.app.bookingData.bookingProgress;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.bookingData.BookingReasonCancelData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.List;

/**
 * Created by edwin on 24/02/2017.
 */

public class BookingReasonCancelAdapter  extends RecyclerView.Adapter<BookingReasonCancelAdapter.SingleItemRowHolder> {
    private List<BookingReasonCancelData> data;
    private Context mContext;
    private int positionReasonSelected = -1;
    public BookingReasonCancelAdapter(Context mContext, List<BookingReasonCancelData> bookingReasonCancelDatas) {
        data = bookingReasonCancelDatas;
        this.mContext = mContext;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.booking_reason_cancel_adapter, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {
        final BookingReasonCancelData singleItem = data.get(i);
        holder.tv_reason.setText(singleItem.getReason());
        TypedValue outValue = new TypedValue();
        if(positionReasonSelected==i){
            holder.reason_cancel_item_layout.setBackgroundColor(Color.parseColor("#c1eda1"));
        }else{
            mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.reason_cancel_item_layout.setBackgroundResource(outValue.resourceId);
        }
        holder.reason_cancel_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==data.size()-1){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(false);
                    builder.setTitle("Masukkan alasan lain");
                    final EditText input = new EditText(mContext);
                    builder.setView(input);
                    if(otherReason!=null) {
                        input.setText(otherReason);
                    }
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            otherReason = input.getText().toString();
                            otherReason = otherReason.replace("\n","");
                            String reason = otherReason.replace(" ","");
                            if(!Utility.getInstance().checkIfStringIsNotNullOrEmpty(reason)) {
                                Toast.makeText(mContext, "Silahkan isi alasan anda membatalkan pesanan", Toast.LENGTH_SHORT).show();
                            } else if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(reason) && reason.length() > 5) {
                                positionReasonSelected = i;
                                data.get(i).setReason(otherReason);
                                notifyDataSetChanged();

                            } else {
                                Toast.makeText(mContext, "Alasan pembatalan harus lebih dari 5 karakter", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Action for "Cancel".
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog alert = builder.create();
                    alert.show();

                }else {
                    positionReasonSelected = i;
                    notifyDataSetChanged();
                }
            }
        });
    }
    String otherReason;
    public String getReason(){
        if(positionReasonSelected==-1){
            return null;
        }else if(positionReasonSelected==-1){
            return otherReason;
        }else {
            return data.get(positionReasonSelected).getReason();
        }
    }
    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        private final LinearLayout reason_cancel_item_layout;
        protected TextView tv_reason;
        public SingleItemRowHolder(View view) {
            super(view);
            this.tv_reason = (TextView) view.findViewById(R.id.tv_reason);
            this.reason_cancel_item_layout = (LinearLayout) view.findViewById(R.id.reason_cancel_item_layout);
        }
    }
}