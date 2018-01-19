package com.pmberjaya.indotiki.app.event;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
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

public class EventListOtherFragment extends Fragment {
    private RecyclerView listEventOther;
    private EventListOtherAdapter eventListOtherAdapter;
    private LocationSessionManager locationSessionManager;
    private LinearLayout layoutNoData;
    private LinearLayout loadinglayout;
    private LinearLayout layoutEventOtherList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_list_other_fragment, container, false);
        renderView(rootView);
        initLocationSession();
        getEventOtherList();
        return rootView;
    }

    private void initLocationSession() {
        locationSessionManager = new LocationSessionManager(getActivity());
    }

    private void renderView(View rootView) {
        layoutEventOtherList = rootView.findViewById(R.id.layoutEventOtherList);
        listEventOther = rootView.findViewById(R.id.listEventOther);
        layoutNoData = rootView.findViewById(R.id.layoutNoData);
        loadinglayout = rootView.findViewById(R.id.layout_loading);
        GridLayoutManager gLayoutManager = new GridLayoutManager(getActivity(), 2);

        listEventOther.setHasFixedSize(true);
        listEventOther.setLayoutManager(gLayoutManager);
        listEventOther.setNestedScrollingEnabled(false);
    }
    public void SetListViewAdapter(List<EventNewData> eventOtherListData) {
        renderSuccessView();
        eventListOtherAdapter = new EventListOtherAdapter(getActivity(), eventOtherListData);
        listEventOther.setAdapter(eventListOtherAdapter);
    }
    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getEventOtherList();
        }
    };
    public void getEventOtherList(){
        renderloadingView();
        Log.d("ImageEvent","JALAN");
        String api = Utility.getInstance().getTokenApi(getActivity());
        String district = locationSessionManager.getUserDistrictIdCentral();
        UserController.getInstance(getActivity()).getEventOtherList(api, UserController.getInstance(getActivity()).eventOtherParams(district), getNewsInterface);
        return;
    }

    private BaseGenericInterface getNewsInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            if (getActivity()!=null){
                int sukses = baseGenericCallback.getSukses();
                EventNewModel eventNewModel = (EventNewModel) baseGenericCallback.getData();
                if (sukses == 2) {
                    List<EventNewData> eventOtherListData = eventNewModel.getResultArray();
                    if (eventOtherListData.size() != 0) {
                        SetListViewAdapter(eventOtherListData);
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
            layoutEventOtherList.removeView(errorLayout);
        }
        listEventOther.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
    }
    public void renderSuccessView(){
        loadinglayout.setVisibility(View.GONE);
        layoutEventOtherList.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEventOtherList.removeView(errorLayout);
        }
        listEventOther.setVisibility(View.VISIBLE);
        layoutNoData.setVisibility(View.GONE);
        layoutEventOtherList.setVisibility(View.VISIBLE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        layoutEventOtherList.setVisibility(View.VISIBLE);
        listEventOther.setVisibility(View.GONE);
        layoutNoData.setVisibility(View.GONE);
        errorLayout = Utility.getInstance().showErrorLayout(getActivity(), layoutEventOtherList, getResources().getString(R.string.oops_something_went_wrong), try_again_listener);
    }
}
