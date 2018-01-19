package com.pmberjaya.indotiki.app.promo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.promo.adapter.MyPromoAdapter;
import com.pmberjaya.indotiki.models.parcelables.PromoCodeDataParcelable;
import com.pmberjaya.indotiki.models.promo.PromoListData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 27/07/2017.
 */

public class MyPromoFragment extends Fragment {

    private ListView list_myPromo;
    private MyPromoAdapter adapter;
    private LinearLayout no_promo_code_layout;
    private LinearLayout layout_listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.promo_my_fragment, container, false);
        renderView(rootView);
        getIntentExtra();
        list_myPromo.setOnItemClickListener(onItemClickListener);
        return rootView;
    }

    private void getIntentExtra() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isUsePromo = bundle.getString("isUsePromo");
            ArrayList<PromoListData> generalPromoList = bundle.getParcelableArrayList(Constants.GENERAL_PROMO);
            ArrayList<PromoListData> myPromoList = bundle.getParcelableArrayList(Constants.MY_PROMO);
            if (generalPromoList != null) {
                myPromoList.addAll(generalPromoList);
            }
            if(myPromoList.size()!=0) {
                SetListViewAdapter(myPromoList);
            }else {
                no_promo_code_layout.setVisibility(View.VISIBLE);
                layout_listView.setVisibility(View.GONE);
            }
        }
    }

    private void renderView(View rootView) {
        list_myPromo = (ListView) rootView.findViewById(R.id.list_myPromo);
        no_promo_code_layout = (LinearLayout) rootView.findViewById(R.id.no_promo_code_layout);
        layout_listView = (LinearLayout) rootView.findViewById(R.id.layout_listView);
        list_myPromo.setOnItemClickListener(onItemClickListener);
    }

    private void SetListViewAdapter(List<PromoListData> data) {
        adapter = new MyPromoAdapter(getActivity(),R.layout.promo_list_adapter,data);
        list_myPromo.setAdapter(adapter);
        list_myPromo.setVisibility(View.VISIBLE);
    }
    private int SELECT_PROMO = 4;
    public String isUsePromo;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PromoCodeDataParcelable promoCodeDataParcelable = new PromoCodeDataParcelable();
            promoCodeDataParcelable.title = adapter.getItem(position).code_promo;
            promoCodeDataParcelable.description = adapter.getItem(position).description;
            promoCodeDataParcelable.cover = adapter.getItem(position).cover;
            Intent i ;
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(isUsePromo)) {
                i = new Intent("use_promo");
            }else{
                i = new Intent();
            }
            i.setClass(getContext(), PromoDetail.class);
            i.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
            getActivity().startActivityForResult(i, SELECT_PROMO);
        }
    };

}
