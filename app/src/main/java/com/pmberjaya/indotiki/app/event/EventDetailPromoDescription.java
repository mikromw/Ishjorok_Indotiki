package com.pmberjaya.indotiki.app.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;

/**
 * Created by edwin on 07/11/2017.
 */

public class EventDetailPromoDescription extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detail_promo_viewpager, container, false);
        Bundle bundle = this.getArguments();
        String description=bundle.getString("description");
        TextView textValue = rootView.findViewById(R.id.textValue);
        if(description!=null){
            textValue.setText(""+ Html.fromHtml(description));
        }else{
            textValue.setText("Tidak ada deskripsi");
        }
        return rootView;
    }
}