package com.pmberjaya.indotiki.app.bookingData.bookingComplete;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingNew.OrderCourierNew;
import com.pmberjaya.indotiki.app.bookingNew.OrderFoodNew;
import com.pmberjaya.indotiki.app.bookingNew.OrderTransportNew;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingCompleteInterface;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteData;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteModel;
import com.pmberjaya.indotiki.models.bookingData.BookingThisTripAgainData;
import com.pmberjaya.indotiki.models.parcelables.BookingCourierDatas;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.BookingTransportDatas;
import com.pmberjaya.indotiki.utilities.FilterManager;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.FloatingGroupExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingCompleteFragment extends Fragment implements BookingCompleteInterface {
	DBController controller;
	FilterManager filterManager;
	private LinearLayout loadinglayout;
	private LinearLayout nobookinglayout;
	FloatingGroupExpandableListView list;
	private String[] filterArray;
	private int banyakdata;
	private int nowTime;
	protected Toolbar toolbar;
	private BookingCompleteAdapter adapter;
	private SwipeRefreshLayout swiperefresh;
//	ArrayList<HashMap<String, String>> placeFavoriteId;
	boolean[] itemsChecked;
	boolean[] itemsFilterChecked;
	private List<BookingCompleteData> bookingListMemberData;
	private ArrayList<List<BookingCompleteData>> childGroupData;
	private ArrayList<String> headerGroupData = new ArrayList<String>();
	CharSequence[] itemfilterdata;
	CharSequence[] itemfilter;
	String filterCompleteData;
	SessionManager sessionManager;
	LocationSessionManager locationSessionManager;
	BookingController bookingMemberController;
	private LinearLayout layoutBookingComplete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.booking_complete_list_activity, container, false);
		sessionManager = new SessionManager(getActivity());
		locationSessionManager = new LocationSessionManager(getActivity());
		bookingMemberController = BookingController.getInstance(getActivity());
		bookingMemberController.setBookingCompleteInterface(this);
		renderView(rootView);
		setHasOptionsMenu(true);
		initEventHandlerWidget();
		initDatabase();
		initFilterSession();
		getBookingCompleteFromDatabaseInternal();
		getBookingCompleteMember();
		return rootView;
	}
	private void initDatabase(){controller = DBController.getInstance(getActivity());}
	private void renderView(View rootView){
		swiperefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
		list = (FloatingGroupExpandableListView) rootView.findViewById(R.id.listBookingComplete);
		loadinglayout = (LinearLayout) rootView.findViewById(R.id.layout_loading);
		nobookinglayout = (LinearLayout) rootView.findViewById(R.id.nobookinglayout);
		layoutBookingComplete = (LinearLayout) rootView.findViewById(R.id.layoutBookingComplete);

	}
	private void initEventHandlerWidget(){
//		list.setOnScrollFloatingGroupListener(floatScrollListener);
		list.setOnChildClickListener(onChildClickListener);
		swiperefresh.setOnRefreshListener(refreshList);
	}
	private OnRefreshListener refreshList = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			getBookingCompleteMember();
		}
	};
	public OnClickListener try_again_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getBookingCompleteMember();
		}
	};

	private OnChildClickListener onChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
									int groupPosition, int childPosition, long id) {
			BookingCompleteData map = adapter.getChild(groupPosition, childPosition);
			String requestId = map.getId();
			String requestType = map.getSource_table();
			String from = map.getFrom();
			String from_place = map.getFrom_place();
			Intent detail = new Intent(getActivity(), BookingCompleteDetail.class);
			detail.putExtra("requestId", requestId);
			detail.putExtra("requestType", requestType);
			detail.putExtra("from", from);
			detail.putExtra("from_place", from_place);
			detail.putExtra("activity", "listview");
			getActivity().startActivity(detail);
			return true;
		}

	};

	private void initFilterSession(){
		filterManager = new FilterManager(getActivity());
		String filterSession = filterManager.getFilter();
//		placeFavoriteId = controller.getFavoritePlaceId("google");
		itemfilterdata = getResources().getStringArray(R.array.filter_item_data);
		itemfilter = getResources().getStringArray(R.array.filter_item);
		itemsChecked = new boolean[itemfilterdata.length];
		itemsFilterChecked = new boolean[itemfilterdata.length];

		if(filterSession!=null){
			filterArray = new String[6];
			filterArray = filterSession.split(",");
			for (int i = 0; i < filterArray.length; i++) {
				for(int j =0; j<itemfilterdata.length; j++){
					if(filterArray[i].equals(itemfilterdata[j].toString())){
						itemsFilterChecked[j]=true;
						break;
					}
				}
			}
		}
		else{
			filterArray = new String[6];
			for(int i =0; i<itemfilterdata.length; i++){
				filterArray[i]= itemfilterdata[i].toString();
				itemsFilterChecked[i]=true;
			}
		}
		for (int i = 0; i < itemfilterdata.length; i++) {
			if (itemsFilterChecked[i]) {
				itemsChecked[i] = true;
			} else {
				itemsChecked[i] = false;
			}
		}
	}
	public void SetListViewAdapter() {
		Log.d("SUDAH SET LISTVIEW", "JALAN");
//			loadinglayout.setVisibility(View.GONE);
		list.setVisibility(View.VISIBLE);
		adapter = new BookingCompleteAdapter(getActivity(),childGroupData, headerGroupData, BookingCompleteFragment.this);
		final BookingCompleteGroupAdapter wrapperAdapter = new BookingCompleteGroupAdapter(adapter);
		list.setAdapter(wrapperAdapter);
		for(int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
			list.expandGroup(i);
		}
	}

	public void setGroupListBookingComplete(BookingCompleteData data) {
		Calendar secondsTime = Calendar.getInstance();
		int secondsTimes = Integer.parseInt(data.getTimeData());
		secondsTime.setTimeInMillis((long)secondsTimes*1000);
		int timeData = secondsTime.get(Calendar.DATE);
		Log.d("time data", timeData+"");
		Log.d("time sekarang", nowTime+"");
		if(nowTime == timeData ){
			bookingListMemberData.add(data);
			Log.d("datanya1", data.getTime()+"");
			if(Calendar.getInstance().get(Calendar.DATE)==timeData&&headerGroupData.size()==0){
				childGroupData.add(bookingListMemberData);
				headerGroupData.add(getResources().getString(R.string.today));
			}
		}
		else{
			bookingListMemberData = new ArrayList<BookingCompleteData>();
			bookingListMemberData.add(data);
			Log.d("datanya2", data.getDate() + "");
			childGroupData.add(bookingListMemberData);
			headerGroupData.add(data.getDate());
			nowTime = timeData;

		}
	}

	public void getBookingCompleteFromDatabaseInternal(){
		List<BookingCompleteData> bookingCompleteMemberData = controller.getBookingHistory();
		banyakdata = bookingCompleteMemberData.size();
		final Calendar calendar = Calendar.getInstance();
		nowTime = calendar.get(Calendar.DATE);
		headerGroupData = new ArrayList<String>();
		adapter=null;
		if (banyakdata != 0) {
			bookingListMemberData = new ArrayList<BookingCompleteData>();
			childGroupData = new ArrayList<List<BookingCompleteData>>();
			for (int i = 0; i < banyakdata; i++) {
				try {
					setGroupListBookingComplete(bookingCompleteMemberData.get(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			SetListViewAdapter();
			loadinglayout.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			nobookinglayout.setVisibility(View.GONE);
		}
	}

//	@Subscribe
//	public void onData(BaseGenericCallback<Data> bookingCompleteMemberCallback){
//		if (bookingCompleteMemberCallback.getCallback().equals("bookingCompleteMemberAdapter")) {
//
//		}
//
//	}
	private void getBookingCompleteMember()
	{
//		if (Utility.getInstance().isInternetOn(getActivity()))
//		{
			renderloadingView();
			String api = Utility.getInstance().getTokenApi(getActivity());
			bookingMemberController.getBookingCompleteMemberRefresh(filterArray, api);
			return;
//		}
//		else{
//			swiperefresh.setRefreshing(false);
//			renderErrorView();
//			Toast.makeText(getActivity(), getResources().getString(R.string.active_your_internet), Toast.LENGTH_SHORT).show();
//		}
	}

	BookingDataParcelable bookingDataParcelable;
	public BookingDataParcelable buildBookingDataParcelable(BookingThisTripAgainData bookingThisTripAgainData, String requestType){
        bookingDataParcelable = new BookingDataParcelable();
        bookingDataParcelable.requestType=requestType;
		bookingDataParcelable.distance = bookingThisTripAgainData.getDistance();
		bookingDataParcelable.distanceValue = Double.parseDouble(bookingDataParcelable.distance.substring(0, bookingDataParcelable.distance.length() - 2)) * 1000;
		bookingDataParcelable.district = bookingThisTripAgainData.getDistrict();
		bookingDataParcelable.payment = bookingThisTripAgainData.getPayment();
		bookingDataParcelable.fromPlace = bookingThisTripAgainData.getFromPlace();
		bookingDataParcelable.from = bookingThisTripAgainData.getFrom();
		bookingDataParcelable.latFrom = Double.valueOf(bookingThisTripAgainData.getLatFrom());
		bookingDataParcelable.lngFrom = Double.valueOf(bookingThisTripAgainData.getLngFrom());
		bookingDataParcelable.to = bookingThisTripAgainData.getTo();
		bookingDataParcelable.toPlace = bookingThisTripAgainData.getToPlace();
		bookingDataParcelable.latTo = Double.valueOf(bookingThisTripAgainData.getLatTo());
		bookingDataParcelable.lngTo = Double.valueOf(bookingThisTripAgainData.getLngTo());
		bookingDataParcelable.cashPaid = bookingThisTripAgainData.getCash_paid();
		bookingDataParcelable.depositPaid = bookingThisTripAgainData.getDeposit_paid();
		bookingDataParcelable.transportation = bookingThisTripAgainData.getTransportation();
		if(bookingDataParcelable.requestType.equals(Constants.TRANSPORT)||bookingDataParcelable.requestType.equals(Constants.CAR)){
			bookingDataParcelable.bookingTransportDatas = buildBookingTransportDatas(bookingThisTripAgainData);
		}else if(bookingDataParcelable.requestType.equals(Constants.COURIER)){
			bookingDataParcelable.bookingCourierDatas = buildBookingCourierDatas(bookingThisTripAgainData);
		}else if(bookingDataParcelable.requestType.equals(Constants.FOOD)){
//			bookingDataParcelable.bookingFoodDatas = buildBookingFoodDatas(bookingThisTripAgainData);
		}else if(bookingDataParcelable.requestType.equals(Constants.MART)){
//			bookingDataParcelable.bookingMartDatas = buildBookingMartDatas(bookingThisTripAgainData);
		}
		return bookingDataParcelable;
	}

	public BookingTransportDatas buildBookingTransportDatas(BookingThisTripAgainData bookingThisTripAgainData){
		BookingTransportDatas bookingTransportDatas = new BookingTransportDatas();
		bookingTransportDatas.locationDetail = bookingThisTripAgainData.getLocationDetail();
		return bookingTransportDatas;
	}

	public BookingCourierDatas buildBookingCourierDatas(BookingThisTripAgainData bookingThisTripAgainData){
		BookingCourierDatas bookingCourierDatas = new BookingCourierDatas();
		bookingCourierDatas.item = bookingThisTripAgainData.getItem();
		bookingCourierDatas.item_photo = bookingThisTripAgainData.getItem_photo();
		bookingCourierDatas.name_sender= bookingThisTripAgainData.getNameSender();
		bookingCourierDatas.phone_sender= bookingThisTripAgainData.getPhoneSender();
		bookingCourierDatas.location_detail_sender= bookingThisTripAgainData.getLocationDetailSender();
		bookingCourierDatas.name_receiver= bookingThisTripAgainData.getNameReceiver();
		bookingCourierDatas.phone_receiver= bookingThisTripAgainData.getPhoneReceiver();
		bookingCourierDatas.location_detail_receiver= bookingThisTripAgainData.getLocationDetailReceiver();
		return bookingCourierDatas;
	}

	ProgressDialog progressBarNya;
	public void bookingAgain(BookingCompleteData data)
	{
		progressBarNya = new ProgressDialog(getActivity());
		progressBarNya.setMessage("Mohon Tunggu...");
		progressBarNya.setIndeterminate(false);
		progressBarNya.setCancelable(true);
		progressBarNya.show();
		LocationSessionManager locationSessionManager = new LocationSessionManager(getActivity());
		String requestId =data.getId();
		String requestType = data.getSource_table();
		String api = Utility.getInstance().getTokenApi(getActivity());
		BookingController.getInstance(getActivity()).bookingAgain(requestId,requestType, api, progressBarNya, bookingAgainInterface);
	}

	private BaseGenericInterface bookingAgainInterface = new BaseGenericInterface() {
		@Override
		public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
			String request_type= baseGenericCallback.getRequest_type();
			int check = baseGenericCallback.getSukses();
			BookingThisTripAgainData bookingThisTripAgainData= (BookingThisTripAgainData) baseGenericCallback.getData();
			bookingDataParcelable = buildBookingDataParcelable(bookingThisTripAgainData,request_type);
			if(check==2){
				Intent confirm = null;
				if(bookingDataParcelable.requestType.equals(Constants.TRANSPORT)){
					confirm = new Intent(getActivity(),OrderTransportNew.class);
				}else if(bookingDataParcelable.requestType.equals(Constants.COURIER)){
					confirm = new Intent(getActivity(), OrderCourierNew.class);
				}else if(bookingDataParcelable.requestType.equals(Constants.FOOD)){
					if(bookingThisTripAgainData.getStatus_store().equals("Open")){
						confirm = new Intent(getActivity(), OrderFoodNew.class);
					}else{
						Utility.getInstance().showSimpleAlertDialog(getActivity(), getResources().getString(R.string.reminder), getResources().getString(R.string.restaurant_close),
								getResources().getString(R.string.ok),null,null,null, true);
						return;
					}
				}else if(bookingDataParcelable.requestType.equals(Constants.MART)) {
					if (bookingThisTripAgainData.getStatus_store().equals("Open")) {
//						confirm = new Intent(getActivity(), OrderMartNew.class);
					} else {
						Utility.getInstance().showSimpleAlertDialog(getActivity(), getResources().getString(R.string.reminder), getResources().getString(R.string.restaurant_close),
								getResources().getString(R.string.ok),null,null,null, true);
						return;
					}
				}else {return;}
				confirm.putExtra(Constants.BOOKING_DATA_PARCELABLE, bookingDataParcelable);
				confirm.setAction("book_again");
				confirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(confirm);
			}
		}

		@Override
		public void onError(APIErrorCallback apiErrorCallback) {
			if(apiErrorCallback.getError()!=null) {
				if (apiErrorCallback.getError().equals("Invalid API key ")) {
					Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
				} else {
					Toast.makeText(getActivity(),getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	public void sendReceipt(String requestId,String requestType){
		String api = Utility.getInstance().getTokenApi(getActivity());
		BookingController.getInstance(getActivity()).sendReceipt(requestId,requestType, api, sendReceiptInterface);
	}

	private BaseInterface sendReceiptInterface = new BaseInterface() {
		@Override
		public void onSuccess(BaseCallback baseCallback) {
			Toast.makeText(getActivity(), baseCallback.getPesan(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(APIErrorCallback apiErrorCallback) {
			if(apiErrorCallback.getError()!=null) {
				if (apiErrorCallback.getError().equals("Invalid API key ")) {
					Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
				} else {
					Toast.makeText(getActivity(),getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};


//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
//		menuInflater.inflate(R.menu.booking_tab_menu, menu);
//		super.onCreateOptionsMenu(menu, menuInflater);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		AlertDialog dialog;
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		builder.setTitle("Penyaringan");
//		builder.setMultiChoiceItems(itemfilter, itemsChecked,
//				new DialogInterface.OnMultiChoiceClickListener() {
//					// indexSelected contains the index of item (of which checkbox checked)
//					@Override
//					public void onClick(DialogInterface dialog, int which,
//										boolean isChecked) {
//						itemsChecked[which] = isChecked;
//					}
//				})
//				// Set the action buttons
//				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int id) {
//						filterCompleteData =null;
//						filterArray = new String[5];
//						StringBuilder sb = new StringBuilder();
//						int index = 0;
//						for (int i = 0; i < itemfilterdata.length; i++) {
//							if (itemsChecked[i]==true) {
//								filterArray[index] = itemfilterdata[i].toString();
//								itemsFilterChecked[i] = true;
//								sb.append(filterArray[index]).append(",");
//								filterCompleteData = sb.toString();
//								index++;
//							} else {
//								itemsFilterChecked[i] = false;
//							}
//						}
//						if (filterCompleteData==null) {
//							filterArray[0] = itemfilterdata[0].toString();
//							Toast.makeText(getActivity(), "Harus Ada 1 Jenis Pemesanan yang dipilih", Toast.LENGTH_SHORT).show();
//							itemsFilterChecked[0] = true;
//							itemsChecked[0] = true;
//							sb.append(filterArray[index]).append(",");
//							filterCompleteData = sb.toString();
//						}
//						filterManager.createFilterSession(filterCompleteData);
//						getBookingCompleteMember();
//					}
//				});
//		dialog = builder.create();//AlertDialog dialog; create like this outside onClick
//		dialog.show();
//		return true;
//	}



	View errorLayout;
	public void renderloadingView(){
		loadinglayout.setVisibility(View.VISIBLE);
		if(errorLayout!=null) {
			layoutBookingComplete.removeView(errorLayout);
		}
		nobookinglayout.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
	}
	public void renderSuccessView(){
		loadinglayout.setVisibility(View.GONE);
		if(errorLayout!=null) {
			layoutBookingComplete.removeView(errorLayout);
		}
		list.setVisibility(View.VISIBLE);
		nobookinglayout.setVisibility(View.GONE);
	}
	public void renderNoDataView(){
		loadinglayout.setVisibility(View.GONE);
		if(errorLayout!=null) {
			layoutBookingComplete.removeView(errorLayout);
		}
		list.setVisibility(View.GONE);
		nobookinglayout.setVisibility(View.VISIBLE);
	}
	public void renderErrorView(String message){
		loadinglayout.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		nobookinglayout.setVisibility(View.GONE);
		errorLayout = Utility.getInstance().showErrorLayout(getActivity(), layoutBookingComplete, message, try_again_listener);
	}
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
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
	public void onGetBookingCompletedSuccess(BaseGenericCallback<BookingCompleteModel> baseGenericCallback) {
		if(getActivity()!=null) {
			swiperefresh.setRefreshing(false);
			int sukses = baseGenericCallback.getSukses();
			BookingCompleteModel data = baseGenericCallback.getData();
			if (sukses == 2) {
				List<BookingCompleteData> bookingCompleteMemberData = data.getResultArray();
				banyakdata = bookingCompleteMemberData.size();
				final Calendar calendar = Calendar.getInstance();
				nowTime = calendar.get(Calendar.DATE);
				headerGroupData = new ArrayList<String>();
				adapter = null;
				controller.deleteBookingHistory();
				if (banyakdata != 0) {
					bookingListMemberData = new ArrayList<BookingCompleteData>();
					childGroupData = new ArrayList<List<BookingCompleteData>>();
					for (int i = 0; i < banyakdata; i++) {
						controller.insertHistoryBooking(bookingCompleteMemberData.get(i));
						setGroupListBookingComplete(bookingCompleteMemberData.get(i));
					}
					SetListViewAdapter();
					renderSuccessView();
				} else {
					renderNoDataView();
				}
			} else {
				renderErrorView(getResources().getString(R.string.unable_to_load_data));
			}
		}
	}

	@Override
	public void onGetBookingCompletedError(APIErrorCallback apiErrorCallback) {
		if(apiErrorCallback.getError()!=null) {
			if (apiErrorCallback.getError().equals("Invalid API key ")) {
				Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
			} else {
				renderErrorView(getResources().getString(R.string.no_internet));
				swiperefresh.setRefreshing(false);
			}
		}
	}
}
