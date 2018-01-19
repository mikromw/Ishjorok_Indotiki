package com.pmberjaya.indotiki.app.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.models.event.EventPromoData;
import com.pmberjaya.indotiki.models.event.EventPromoModel;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.List;



/**
 * Created by willy on 3/22/2016.
 */
public class EventListPromoFragment extends Fragment {
    private ListView listEventPromo;
    private LinearLayout loadinglayout;
    private EventListPromoAdapter adapter;
    private Toolbar toolbar;
    private LocationSessionManager locationSessionManager;
    private LinearLayout layoutNoData;
    private LinearLayout layoutEventPromoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_list_promo_fragment, container, false);
        renderView(rootView);
        initLocationSession();
        listEventPromo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EventPromoData eventPromoData =  adapter.getItem(position);
                Intent in = new Intent(getActivity(), EventDetailPromo.class);
                in.putExtra("parcelable", eventPromoData);
                startActivity(in);
            }
        });
        getEvent();
        return rootView;
    }

    private void initLocationSession(){
        locationSessionManager = new LocationSessionManager(getActivity());
    }
    private void renderView(View rootView) {
        layoutEventPromoList = rootView.findViewById(R.id.layoutEventPromoList);
        listEventPromo = (ListView) rootView.findViewById(R.id.listEventPromo);
        loadinglayout = (LinearLayout) rootView.findViewById(R.id.layout_loading);
        layoutNoData = rootView.findViewById(R.id.layoutNoData);
    }

    public void SetListViewAdapter(List<EventPromoData> newsArray) {
        renderSuccessView();
        adapter = new EventListPromoAdapter(getActivity(), R.layout.event_list_promo_adapter,newsArray);
        listEventPromo.setAdapter(adapter);
    }

    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getEvent();
        }
    };
    private void getEvent()
    {
        renderloadingView();
        Log.d("ImageEvent","JALAN");
        String api = Utility.getInstance().getTokenApi(getActivity());
        String district = locationSessionManager.getUserDistrictIdCentral();
        UserController.getInstance(getActivity()).getEventPromoList(api, UserController.getInstance(getActivity()).eventPromoParams(district),getEventInterface);
        return;
    }
    BaseGenericInterface getEventInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> eventCallback) {
            if (getActivity()!=null){
                EventPromoModel data = (EventPromoModel) eventCallback.getData();
                if (eventCallback.getSukses() == 2) {
                    List<EventPromoData> eventPromoDatas = data.getResultArray();
                    if (eventPromoDatas.size() != 0) {
                        SetListViewAdapter(eventPromoDatas);
                    } else {
                        renderErrorView();
                    }
                }
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    View errorLayout;
    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEventPromoList.removeView(errorLayout);
        }
        listEventPromo.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
    }
    public void renderSuccessView(){
        loadinglayout.setVisibility(View.GONE);
        layoutEventPromoList.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEventPromoList.removeView(errorLayout);
        }
        listEventPromo.setVisibility(View.VISIBLE);
        layoutNoData.setVisibility(View.GONE);
        layoutEventPromoList.setVisibility(View.VISIBLE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        layoutEventPromoList.setVisibility(View.VISIBLE);
        listEventPromo.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
        errorLayout = Utility.getInstance().showErrorLayout(getActivity(), layoutEventPromoList, getResources().getString(R.string.oops_something_went_wrong), try_again_listener);
    }
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
