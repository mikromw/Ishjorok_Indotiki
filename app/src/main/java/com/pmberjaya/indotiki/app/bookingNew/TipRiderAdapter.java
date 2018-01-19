package com.pmberjaya.indotiki.app.bookingNew;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.bookingNew.TipRiderData;

import java.util.Arrays;
import java.util.List;

/**
 * Created by edwin on 22/03/2017.
 */

public class TipRiderAdapter extends RecyclerView.Adapter<TipRiderAdapter.ItemRowHolder> {
    private TipRiderData tipRiderData;
    private Context mContext;
    private AlertDialog levelDialog;
    private String[] tipRiderTxt = {"NO", "2", "5", "10", "20", "30", "50", "60", "70", "80", "100", "Input"};
    private List<String> tipRiderTxtArrays;
    private String[] tipRiderValue = {"0","2000","5000", "10000", "20000", "30000", "50000", "60000", "70000", "80000", "100000", ""};
    private List<String> tipRiderValueArrays ;
    private int positionTipSelected=-1;
    private AlertDialog aDialog;
    private String temp_tip_rider;
    TipRiderActivity activity;
    public TipRiderAdapter(Context context, String tip_value) {
        this.mContext = context;
        tipRiderTxtArrays = Arrays.asList(tipRiderTxt);
        tipRiderValueArrays = Arrays.asList(tipRiderValue);
        for(int i = 0; i<tipRiderValueArrays.size(); i++){
            if(tip_value.equals(tipRiderValueArrays.get(i))){
                positionTipSelected = i;
            }
        }
        if(positionTipSelected==-1){
            positionTipSelected = tipRiderValueArrays.size()-1;
             activity = (TipRiderActivity)mContext;
            activity.setTipAmount(tip_value);
            activity.showInputManualTipAmount();
        }
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tip_for_rider_adapter, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }


    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.tv_tip_rider_text.setText(tipRiderTxtArrays.get(itemRowHolder.getAdapterPosition()));
        TypedValue outValue = new TypedValue();
        if(itemRowHolder.getAdapterPosition()==0){
            itemRowHolder.tv_rp.setVisibility(View.GONE);
            itemRowHolder.tv_unit.setText(mContext.getResources().getString(R.string.tip));
        }else if (itemRowHolder.getAdapterPosition()==11) {
            itemRowHolder.tv_rp.setVisibility(View.GONE);
            itemRowHolder.tv_unit.setText("Manual");
        }
        else {
            itemRowHolder.tv_rp.setVisibility(View.VISIBLE);
            itemRowHolder.tv_unit.setText(mContext.getResources().getString(R.string.ribu));
        }
        if(positionTipSelected==itemRowHolder.getAdapterPosition()){
            itemRowHolder.tip_item_layout.setBackgroundColor(Color.parseColor("#c1eda1"));
        }else{
            mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            itemRowHolder.tip_item_layout.setBackgroundResource(outValue.resourceId);
        }
        itemRowHolder.tip_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemRowHolder.getAdapterPosition()==11){
                    positionTipSelected = itemRowHolder.getAdapterPosition();
                    notifyDataSetChanged();
                    TipRiderActivity activity = (TipRiderActivity)mContext;
                    activity.showInputManualTipAmount();
                    activity.showPrice();
//                    temp_tip_rider = activity.tv_tip_ammount.getText().toString();
//                    tipRiderValue[10] = temp_tip_rider;

//                    final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
//                    alert.setTitle("Input Tip Rider");
//                    LinearLayout layout = new LinearLayout(mContext);
//                    layout.setOrientation(LinearLayout.VERTICAL);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    params.setMargins(mContext.getResources().getDimensionPixelOffset(R.dimen.bigger_padding_margin), 0,
//                            mContext.getResources().getDimensionPixelOffset(R.dimen.bigger_padding_margin), 0);
//                    final EditText inputTipRider = new EditText(mContext);
//                    inputTipRider.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    layout.addView(inputTipRider, params);
//                    alert.setView(layout);
//                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//
//                                    temp_tip_rider = inputTipRider.getText().toString();
//                                    Log.d("promo code = ", temp_tip_rider);
//                                    if (temp_tip_rider.length() != 0) {
//                                        temp_tip_rider = inputTipRider.getText().toString();
//                                        tipRiderValue[10] = temp_tip_rider;
//                                        dialog.dismiss();
//                                    }
//                                }
//                            });
//                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    // do nothing
//                                }
//                            });
//                            alert.show();
                }else{
                    positionTipSelected = itemRowHolder.getAdapterPosition();
                    notifyDataSetChanged();
                    TipRiderActivity activity = (TipRiderActivity)mContext;
                    activity.hideInputManualTipAmount();
                }

            }
        });




    }

    public String getTipValue(){
        if(positionTipSelected==-1){
            return null;
        }else {
            return tipRiderValueArrays.get(positionTipSelected);
        }
    }
    @Override
    public int getItemCount() {
        return (null != tipRiderTxtArrays ? tipRiderTxtArrays.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tv_rp;
        private TextView tv_unit;
        private LinearLayout tip_item_layout;
        private TextView tv_tip_rider_text;

        private ItemRowHolder(View view) {
            super(view);
            tip_item_layout = (LinearLayout) view.findViewById(R.id.tip_item_layout);
            tv_tip_rider_text = (TextView) view.findViewById(R.id.tv_tip_rider_text);
            tv_rp = (TextView) view.findViewById(R.id.rp);
            tv_unit = (TextView) view.findViewById(R.id.unit);
        }
    }
}