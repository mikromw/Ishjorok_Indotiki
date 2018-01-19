package com.pmberjaya.indotiki.app.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.pmberjaya.indotiki.app.account.profile.EditProfileActivity;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.models.event.EventNewData;
import com.pmberjaya.indotiki.models.event.EventNewModel;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.List;

/**
 * Created by edwin on 11/10/2017.
 */

public class EventListNewFragment extends Fragment {
    private ListView listEventNew;
    private LinearLayout loadinglayout;
    private EventListNewAdapter adapter;
    private LinearLayout layoutNoData;
    private boolean runGetEvent = true;
    private LocationSessionManager locationSessionManager;
    private LinearLayout layoutEventNewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_list_new_fragment, container, false);
        renderView(rootView);
        initLocationSession();
        listEventNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EventNewData eventNewData = adapter.getItem(position);
                Intent in = new Intent(getActivity(), EventDetail.class);
                in.putExtra("parcelable", eventNewData);
                startActivity(in);
            }
        });
        getEventListNew();
        return rootView;
    }

    private void initLocationSession() {
        locationSessionManager = new LocationSessionManager(getActivity());
    }

    private void renderView(View rootView) {
        listEventNew = rootView.findViewById(R.id.listEventNew);
        loadinglayout = rootView.findViewById(R.id.layout_loading);
        layoutNoData = rootView.findViewById(R.id.layoutNoData);
        layoutEventNewList = rootView.findViewById(R.id.layoutEventNewList);
    }

    public void SetListViewAdapter(List<EventNewData> newsArray) {
        adapter = new EventListNewAdapter(getActivity(), R.layout.event_list_promo_adapter, newsArray);
        listEventNew.setAdapter(adapter);
    }

    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getEventListNew();
        }
    };

    private void getEventListNew() {
        renderloadingView();
        Log.d("ImageEvent", "JALAN");
        String api = Utility.getInstance().getTokenApi(getActivity());
        String district = locationSessionManager.getUserDistrictIdCentral();
        UserController.getInstance(getActivity()).getEventNewList(api, UserController.getInstance(getActivity()).eventNewParams(district), getNewsInterface);
        return;
    }


    BaseGenericInterface getNewsInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> eventCallback) {
            if (getActivity()!=null) {
                Log.d("CALLBACK", "JALAN");
                EventNewModel eventNewModel = (EventNewModel) eventCallback.getData();
                int sukses = eventCallback.getSukses();
                if (sukses == 2) {
                    List<EventNewData> eventOtherListData = eventNewModel.getResultArray();
                    if (eventOtherListData.size() != 0) {

                        SetListViewAdapter(eventOtherListData);
                        renderSuccessView();
                    } else {
                        renderErrorView();
                    }
                }
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                } else if (apiErrorCallback.getError().equals("Error: Internal Server Error")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    View errorLayout;
    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEventNewList.removeView(errorLayout);
        }
        listEventNew.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
    }
    public void renderSuccessView(){
        loadinglayout.setVisibility(View.GONE);
        layoutEventNewList.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEventNewList.removeView(errorLayout);
        }
        listEventNew.setVisibility(View.VISIBLE);
        layoutNoData.setVisibility(View.GONE);
        layoutEventNewList.setVisibility(View.VISIBLE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        layoutEventNewList.setVisibility(View.VISIBLE);
        listEventNew.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
        errorLayout = Utility.getInstance().showErrorLayout(getActivity(), layoutEventNewList, getResources().getString(R.string.oops_something_went_wrong), try_again_listener);
    }



    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}