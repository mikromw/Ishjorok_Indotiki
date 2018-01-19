package com.pmberjaya.indotiki.app.bookingData.bookingProgress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingNew.SearchDriverNew;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressMemberData;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressModel;
import com.pmberjaya.indotiki.utilities.FilterManager;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BookingInProgressFragment extends Fragment {
	
	FilterManager filterManager;
	private LinearLayout loadinglayout;
	private LinearLayout nobookinglayout;
	private ListView list;
	private ImageView filter;
	private ArrayList<HashMap<String,String>> BookingTransportArray;
	private ArrayList<HashMap<String,String>> BookingTransportArrayNew;

	private String profesiDriver;
	String filterData;
	private String[] filterArray;
	private int banyakdata;
	boolean[] itemsChecked;
	boolean[] itemsFilterChecked;
	protected Toolbar toolbar;
	private BookingInProgressAdapter adapter;
	private SwipeRefreshLayout swiperefresh;
	private DBController dbController;
	BookingController bookingController;
	private LinearLayout layoutBookingProgress;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		setFilterManager();
		View rootView = inflater.inflate(R.layout.booking_in_progress_list_activity, container, false);
		bookingController = BookingController.getInstance(getActivity());
		renderView(rootView);
		initEventHandlerWidget();
		initDB();
		getBookingInProgressFromDatabaseInternal();
		getBookingInProgressMember();
		return rootView;
    }
	public void initDB(){
		dbController = DBController.getInstance(getActivity());
	}
	private void initEventHandlerWidget(){
		list.setOnItemClickListener(onItemListClicked);
		swiperefresh.setOnRefreshListener(refreshListener);
	}
	private void setFilterManager(){
		filterManager = new FilterManager(getActivity());
		final CharSequence[] itemfilterdata = getResources().getStringArray(R.array.filter_item_data);
		itemsChecked = new boolean[itemfilterdata.length];
		itemsFilterChecked = new boolean[itemfilterdata.length];
		filterArray = new String[6];
		for(int i =0; i<itemfilterdata.length; i++){
			filterArray[i]= itemfilterdata[i].toString();
			itemsFilterChecked[i]=true;
		}
	}
	private void renderView(View rootView){
		nobookinglayout = (LinearLayout) rootView.findViewById(R.id.nobookinglayout);
		swiperefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
		list = (ListView) rootView.findViewById(R.id.booking_transport_list_list);
		loadinglayout = (LinearLayout) rootView.findViewById(R.id.layout_loading);
		layoutBookingProgress = (LinearLayout) rootView.findViewById(R.id.layoutBookingProgress);
	}
	private OnRefreshListener refreshListener = new OnRefreshListener(){

		@Override
		public void onRefresh() {
			getBookingInProgressMember();
		}

	};
	private OnItemClickListener onItemListClicked = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String requestId = adapter.getItemdata().get(position).getId();
			String jenis_request= adapter.getItemdata().get(position).getSourceTable();
			String transportation = adapter.getItemdata().get(position).getTransportation();
			String statusBooking = adapter.getItemdata().get(position).getStatus();
			if(statusBooking!=null&&(statusBooking.equals("accept")||statusBooking.equals("pick_up"))) {
				Intent detail = new Intent(getActivity(), BookingInProgressDetail.class);
				detail.putExtra("requestId", requestId);
				detail.putExtra("requestType", jenis_request);
				getActivity().startActivity(detail);

			}else{
				Intent detail = new Intent(getActivity(), SearchDriverNew.class);
				detail.putExtra("request_id", requestId);
				detail.putExtra("request_type", jenis_request);
				detail.putExtra("transportation", transportation);
				detail.putExtra("searching", "true");
				getActivity().startActivity(detail);
			}
		}
	};
	public void SetListViewAdapter(List<BookingInProgressMemberData> bookingArray) {
		adapter = new BookingInProgressAdapter(getActivity(), R.layout.booking_in_progress_list_activity,bookingArray);
		list.setAdapter(adapter);
	}

	public View.OnClickListener try_again_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			getBookingInProgressMember();
		}
	};
	private void getBookingInProgressMember()
	{
		renderloadingView();
		String api = Utility.getInstance().getTokenApi(getActivity());
		bookingController.getBookingInProgressMemberRefresh(filterArray, api, bookingInProgressInterface);
		return;
	}
	BaseGenericInterface bookingInProgressInterface = new BaseGenericInterface() {
		@Override
		public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
			if(getActivity()!=null) {
				swiperefresh.setRefreshing(false);
				BookingInProgressModel data = (BookingInProgressModel) baseGenericCallback.getData();
				List<BookingInProgressMemberData> bookingInProgressMemberDatas = data.getResultArray();
				banyakdata = bookingInProgressMemberDatas.size();
				dbController.deleteBookingProgress(null);
				if (banyakdata == 0) {
					renderNoDataView();
				} else {
					for (int i = 0; i < banyakdata; i++) {
						dbController.insertProgressBooking(bookingInProgressMemberDatas.get(i));
					}
					SetListViewAdapter(bookingInProgressMemberDatas);
					renderSuccessView();
				}
			}
		}

		@Override
		public void onError(APIErrorCallback apiErrorCallback) {
			if(apiErrorCallback.getError()!=null) {
				if (apiErrorCallback.getError().equals("Invalid API key ")) {
					Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
				} else {
					renderErrorView();
					swiperefresh.setRefreshing(false);
				}
			}
		}
	};
	View errorLayout;
	public void renderloadingView(){
		loadinglayout.setVisibility(View.VISIBLE);
		if(errorLayout!=null) {
			layoutBookingProgress.removeView(errorLayout);
		}
		nobookinglayout.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
	}
	public void renderSuccessView(){
		loadinglayout.setVisibility(View.GONE);
		if(errorLayout!=null) {
			layoutBookingProgress.removeView(errorLayout);
		}
		list.setVisibility(View.VISIBLE);
		nobookinglayout.setVisibility(View.GONE);
	}
	public void renderNoDataView(){
		loadinglayout.setVisibility(View.GONE);
		if(errorLayout!=null) {
			layoutBookingProgress.removeView(errorLayout);
		}
		list.setVisibility(View.GONE);
		nobookinglayout.setVisibility(View.VISIBLE);
	}
	public void renderErrorView(){
		loadinglayout.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		nobookinglayout.setVisibility(View.GONE);
		errorLayout = Utility.getInstance().showErrorLayout(getActivity(), layoutBookingProgress, getResources().getString(R.string.oops_something_went_wrong), try_again_listener);
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onStop() {
		super.onStop();
	}
	@Override
	 public void onResume() {
		getBookingInProgressFromDatabaseInternal();
		getBookingInProgressMember();
		super.onResume();
	 }
	 @Override
	    public void onPause() {
	        super.onPause();

	    }

	public void getBookingInProgressFromDatabaseInternal(){
        if(getActivity()!=null) {
            swiperefresh.setRefreshing(false);
            loadinglayout.setVisibility(View.GONE);
            List<BookingInProgressMemberData> bookingListDriverData = dbController.getBookingInProgress();
            banyakdata = bookingListDriverData.size();
            if (banyakdata != 0) {
                SetListViewAdapter(bookingListDriverData);
                nobookinglayout.setVisibility(View.GONE);
            } else {
                nobookinglayout.setVisibility(View.VISIBLE);
            }
        }
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
