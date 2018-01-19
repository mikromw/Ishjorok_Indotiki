package com.pmberjaya.indotiki.app.bookingData.bookingComplete;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingCourierFragment;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingFoodFragment;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingMarketFragment;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingTransportFragment;
import com.pmberjaya.indotiki.app.bookingNew.TipRiderActivity;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberDetailData;
import com.pmberjaya.indotiki.models.bookingData.DriverData;
import com.pmberjaya.indotiki.models.parcelables.BookingCourierDatas;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.BookingTransportDatas;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.ImageViewRounded;

import java.util.HashMap;
import java.util.Map;

public class BookingCompleteDetail extends BaseActivity {
	LinearLayout btnRate;
	RelativeLayout layoutBookingCompleteMain;
	private TextView tvCompleteTime;
	private TextView tvRequestType;
	private TextView tvStatus;
	private ImageView ivRequestType;

	private String activity;
	private String commentRatingDriverData;
	String ratingDriverData = "";
	protected Toolbar toolbar;
	ProgressDialog progressBarNya;
	private LinearLayout loadinglayout;
	private TextView tvNoPlat;
	private TextView tvNamaKendaraan;
	private TextView tvCommentRating;

	private ImageViewRounded ivDriverPhoto;
	private TextView tvDriverName;
	LinearLayout layoutRating;
	private RatingBar ratingbar;
	private EditText edComment;
	private TextView tvComment;
	private String ratingDriverDataTemp;
	private boolean ratingComment;

	private LinearLayout layoutTip;
	private TextView tvTipDesc;
	private ImageView ivIconTip;
	private TextView tvTip;
	private TextView tvInkiPayBalance;
	private LinearLayout btnGiveTip;
	private TextView tvGiveTip;
	private String inkiPayBalance;
	private int TIP_FOR_RIDER = 3;
	private LinearLayout layoutBookingComplete;

	/************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_complete_detail_activity);
		renderView();
		initSession();
		getIntentExtra();
		initEventHandlerWidget();
		getBookingCompleteMemberDetail();
	}

	public void renderView(){
		ivDriverPhoto = findViewById(R.id.ivDriverPhoto);
		tvDriverName = findViewById(R.id.tvDriverName);
		btnRate = findViewById(R.id.btnRate);
		layoutRating = findViewById(R.id.layoutRating);
		layoutBookingCompleteMain = findViewById(R.id.layoutBookingCompleteDetailMain);
		layoutBookingComplete = findViewById(R.id.layoutBookingCompleteDetail);
		tvCompleteTime = findViewById(R.id.tvCompleteTime);
		tvRequestType = findViewById(R.id.tvRequestType);
		tvStatus = findViewById(R.id.tvStatus);
		ivRequestType= findViewById(R.id.ivRequestType);
		edComment = findViewById(R.id.edComment);
		tvComment = findViewById(R.id.tvComment);
		loadinglayout = findViewById(R.id.layout_loading);
		ratingbar = findViewById(R.id.ratingBar);
		Typeface custom_font = Typeface.createFromAsset(getResources().getAssets(),  "fonts/BenchNine-Bold.ttf");
		tvStatus.setTypeface(custom_font);
		tvNoPlat = findViewById(R.id.tvNoPlat);
		tvNamaKendaraan = findViewById(R.id.tvNamaKendaraan);
		tvCommentRating = findViewById(R.id.tvCommentRating);
		layoutTip = findViewById(R.id.layoutTip);
		tvTipDesc = findViewById(R.id.tvTipDesc);
		ivIconTip = findViewById(R.id.ivIconTip);
		tvTip = findViewById(R.id.tvTip);
		tvInkiPayBalance = findViewById(R.id.tvInkiPayBalance);
		btnGiveTip = findViewById(R.id.btnGiveTip);
		tvGiveTip = findViewById(R.id.tvGiveTip);
	}
	private void initSession() {
		SessionManager session = new SessionManager(this);
		HashMap<String,String> mapData = session.getUserDetails();
		inkiPayBalance = mapData.get(SessionManager.KEY_DEPOSIT);
		tvInkiPayBalance.setText(" " + "Rp." + "" +Utility.getInstance().convertPrice(inkiPayBalance));
	}
	private void initEventHandlerWidget(){
		btnRate.setOnClickListener(rateDriver);
		btnGiveTip.setOnClickListener(intentTopTip);
		ratingbar.setOnRatingBarChangeListener(onRatingBarChangeListener);
	}
	public void getIntentExtra(){
		bookingDataParcelable = new BookingDataParcelable();
		Intent intent = getIntent();
		bookingDataParcelable.id = intent.getStringExtra("requestId");
		bookingDataParcelable.requestType = intent.getStringExtra("requestType");
		activity = intent.getStringExtra("activity");
	}
	private OnRatingBarChangeListener onRatingBarChangeListener= new OnRatingBarChangeListener(){

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
			ratingDriverDataTemp = String.valueOf(Math.round(rating));
			Log.d("rating bar " , " > " +ratingDriverDataTemp);
			CommentRatingDriver();

			ratingComment = ratingDriverDataTemp.equals("4") || ratingDriverDataTemp.equals("5");
		}
	};

	private void CommentRatingDriver() {
		if (ratingDriverDataTemp.equals("0")) {
			tvCommentRating.setText(getResources().getString(R.string.rate_star));
		}
		else if (ratingDriverDataTemp.equals("1")) {
			tvCommentRating.setText(getResources().getString(R.string.very_bad));
		}
		else if (ratingDriverDataTemp.equals("2")) {
			tvCommentRating.setText(getResources().getString(R.string.bad));
		}
		else if (ratingDriverDataTemp.equals("3")) {
			tvCommentRating.setText(getResources().getString(R.string.good));
		}
		else if (ratingDriverDataTemp.equals("4")) {
			tvCommentRating.setText(getResources().getString(R.string.satisfied));
		}
		else if (ratingDriverDataTemp.equals("5")) {
			tvCommentRating.setText(getResources().getString(R.string.very_satisfied));
		}
	}

	public OnClickListener try_again_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getBookingCompleteMemberDetail();
		}
	};
	public OnClickListener rateDriver = new OnClickListener(){
		@Override
		public void onClick(View v) {
			commentRatingDriverData = edComment.getText().toString();

			if (!commentRatingDriverData.equals("")) {
				ratingComment = true;
			}

			if((Utility.getInstance().checkIfStringIsNotNullOrEmpty(ratingDriverDataTemp)&&!ratingDriverDataTemp.equals("0") && ratingComment == true)){
				getRatingDriver();
			}else{
				if(!Utility.getInstance().checkIfStringIsNotNullOrEmpty(ratingDriverDataTemp)||ratingDriverDataTemp.equals("0")){
					Toast.makeText(BookingCompleteDetail.this, getResources().getString(R.string.please_select_star), Toast.LENGTH_SHORT).show();
				}else if(!Utility.getInstance().checkIfStringIsNotNullOrEmpty(commentRatingDriverData)){
					Toast.makeText(BookingCompleteDetail.this, getResources().getString(R.string.please_insert_comment), Toast.LENGTH_SHORT).show();
				}else if(ratingComment == false) {
					Toast.makeText(BookingCompleteDetail.this, getResources().getString(R.string.please_insert_comment), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	private OnClickListener intentTopTip = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent i = new Intent(BookingCompleteDetail.this, TipRiderActivity.class);
			i.putExtra("tip_rider", bookingDataParcelable.tipBefore);
			startActivityForResult(i, TIP_FOR_RIDER);
		}
	};
	public void RatingDriver(String score){
		ratingbar.setRating(Float.parseFloat(score));
	}

	private void getBookingCompleteMemberDetail() {
		renderloadingView();
		String api = Utility.getInstance().getTokenApi(BookingCompleteDetail.this);
		BookingController.getInstance(BookingCompleteDetail.this).getBookingCompleteDetail(bookingDataParcelable.id , bookingDataParcelable.requestType, api, getBookingCompleteInterface);
		return;
	}

	BaseGenericInterface getBookingCompleteInterface = new BaseGenericInterface() {
		@Override
		public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
			int sukses = baseGenericCallback.getSukses();
			if(sukses==2){
				BookingCompleteMemberDetailData bookingCompleteMemberDetailData = (BookingCompleteMemberDetailData) baseGenericCallback.getData();
				ratingDriverData = bookingCompleteMemberDetailData.getPoint_plus();
				commentRatingDriverData = bookingCompleteMemberDetailData.getComment();
				bookingDataParcelable = buildBookingDataParcelable(bookingCompleteMemberDetailData);
				setCompletedOrCancelledView(bookingDataParcelable);
				setDriverDataView(bookingDataParcelable);
				setTransportationTypeView(bookingDataParcelable);
				setBookingFragmentView(bookingDataParcelable);
				tvCompleteTime.setText(bookingDataParcelable.endTime);
			}
			renderSuccessView();
		}

		@Override
		public void onError(APIErrorCallback apiErrorCallback) {
			if(apiErrorCallback.getError()!=null) {
				if (apiErrorCallback.getError().equals("Invalid API key ")) {
					Utility.getInstance().showInvalidApiKeyAlert(BookingCompleteDetail.this, getResources().getString(R.string.relogin));
				}else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
					renderErrorView();
					Toast.makeText(BookingCompleteDetail.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
				}
				else{
					renderErrorView();
					Toast.makeText(BookingCompleteDetail.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	View errorLayout;
	public void renderloadingView(){
		loadinglayout.setVisibility(View.VISIBLE);
		layoutBookingCompleteMain.setVisibility(View.GONE);
		if(errorLayout!=null) {
			layoutBookingCompleteMain.removeView(errorLayout);
		}
	}
	public void renderSuccessView(){
		loadinglayout.setVisibility(View.GONE);
		layoutBookingComplete.setVisibility(View.VISIBLE);
		if(errorLayout!=null) {
			layoutBookingCompleteMain.removeView(errorLayout);
		}
		layoutBookingCompleteMain.setVisibility(View.VISIBLE);
	}
	public void renderErrorView(){
		loadinglayout.setVisibility(View.GONE);
		layoutBookingComplete.setVisibility(View.GONE);
		layoutBookingCompleteMain.setVisibility(View.VISIBLE);
		errorLayout = Utility.getInstance().showErrorLayout(BookingCompleteDetail.this, layoutBookingCompleteMain, getResources().getString(R.string.oops_something_went_wrong), try_again_listener, 0, 0);
	}
	BookingDataParcelable bookingDataParcelable;
	public BookingDataParcelable buildBookingDataParcelable(BookingCompleteMemberDetailData bookingCompleteMemberDetailData){
		bookingDataParcelable.requestTime = bookingCompleteMemberDetailData.getRequest_time();
		bookingDataParcelable.acceptTime = bookingCompleteMemberDetailData.getAccept_time();
		bookingDataParcelable.endTime = bookingCompleteMemberDetailData.getEnd_time();
		bookingDataParcelable.channel = bookingCompleteMemberDetailData.getChannel();
		bookingDataParcelable.distance = bookingCompleteMemberDetailData.getDistance();

		bookingDataParcelable.id = bookingCompleteMemberDetailData.getId();
		bookingDataParcelable.originalPrice = bookingCompleteMemberDetailData.getOriginal_price();
		bookingDataParcelable.payment = bookingCompleteMemberDetailData.getPayment();
		bookingDataParcelable.price = bookingCompleteMemberDetailData.getPrice();
		bookingDataParcelable.promoCode = bookingCompleteMemberDetailData.getPromo();
		bookingDataParcelable.promoPrice = bookingCompleteMemberDetailData.getPromo_price();
		bookingDataParcelable.transportation = bookingCompleteMemberDetailData.getTransportation();
		bookingDataParcelable.status = bookingCompleteMemberDetailData.getStatus();
		bookingDataParcelable.tip = bookingCompleteMemberDetailData.getTip();
		bookingDataParcelable.tipBefore = bookingCompleteMemberDetailData.getTip();
		bookingDataParcelable.depositPaid = bookingCompleteMemberDetailData.getDeposit_paid();
		bookingDataParcelable.cashPaid = bookingCompleteMemberDetailData.getCash_paid();
		bookingDataParcelable.from = bookingCompleteMemberDetailData.getFrom_place();
		bookingDataParcelable.toPlace = bookingCompleteMemberDetailData.getTo_place();
		bookingDataParcelable.from = bookingCompleteMemberDetailData.getFrom();
		bookingDataParcelable.to = bookingCompleteMemberDetailData.getTo();
		//----------------------------set driver model-----------------------------------------------------------
		bookingDataParcelable.driverData = new DriverData();
		bookingDataParcelable.driverData.setDriver_avatar(bookingCompleteMemberDetailData.getDriver_avatar());
		bookingDataParcelable.driverData.setDriver_fullname(bookingCompleteMemberDetailData.getDriver_fullname());
		bookingDataParcelable.driverData.setDriver_id(bookingCompleteMemberDetailData.getDriver_id());
		bookingDataParcelable.driverData.setDriver_phone(bookingCompleteMemberDetailData.getDriver_phone());
		bookingDataParcelable.driverData.setNumber_plate(bookingCompleteMemberDetailData.getNumber_plate());
		bookingDataParcelable.driverData.setTransportation_name(bookingCompleteMemberDetailData.getTransportation_name());
		//----------------------------set driver model-----------------------------------------------------------
		if(bookingDataParcelable.requestType.equals(Constants.TRANSPORT)||bookingDataParcelable.requestType.equals(Constants.CAR)){
			bookingDataParcelable.bookingTransportDatas = buildBookingTransportDatas(bookingCompleteMemberDetailData);
		}else if(bookingDataParcelable.requestType.equals(Constants.COURIER)){
			bookingDataParcelable.bookingCourierDatas = buildBookingCourierDatas(bookingCompleteMemberDetailData);
		}else if(bookingDataParcelable.requestType.equals(Constants.FOOD)){
//			bookingDataParcelable.bookingFoodDatas = buildBookingFoodDatas(bookingCompleteMemberDetailData);
		}else if(bookingDataParcelable.requestType.equals(Constants.MART)){
//			bookingDataParcelable.bookingMartDatas = buildBookingMartDatas(bookingCompleteMemberDetailData);
		}
		return bookingDataParcelable;
	}

	public BookingTransportDatas buildBookingTransportDatas(BookingCompleteMemberDetailData bookingCompleteMemberDetailData){
		BookingTransportDatas bookingTransportDatas = new BookingTransportDatas();
		bookingTransportDatas.locationDetail = bookingCompleteMemberDetailData.getLocation_detail();
		return bookingTransportDatas;
	}

	public BookingCourierDatas buildBookingCourierDatas(BookingCompleteMemberDetailData bookingCompleteMemberDetailData){
		BookingCourierDatas bookingCourierDatas = new BookingCourierDatas();
		bookingCourierDatas.item = bookingCompleteMemberDetailData.getItem();
		bookingCourierDatas.item_photo = bookingCompleteMemberDetailData.getItem_photo();
		bookingCourierDatas.name_sender= bookingCompleteMemberDetailData.getName_sender();
		bookingCourierDatas.phone_sender= bookingCompleteMemberDetailData.getPhone_sender();
		bookingCourierDatas.location_detail_sender= bookingCompleteMemberDetailData.getLocation_detail_sender();
		bookingCourierDatas.name_receiver= bookingCompleteMemberDetailData.getName_receiver();
		bookingCourierDatas.phone_receiver= bookingCompleteMemberDetailData.getPhone_receiver();
		bookingCourierDatas.location_detail_receiver= bookingCompleteMemberDetailData.getLocation_detail_receiver();
		return bookingCourierDatas;
	}


	private void setCompletedOrCancelledView(BookingDataParcelable bookingDataParcelable){
		if(bookingDataParcelable.status!=null&&bookingDataParcelable.status.equals("complete")) {
			tvStatus.setText(getResources().getString(R.string.booking_success));
			tvStatus.setTextColor(Utility.getColor(getResources(), R.color.green_900, null));
			if(ratingDriverData!=null){
				setDisableRate();
				RatingDriver(ratingDriverData);
				btnRate.setVisibility(View.GONE);
			}else{
				if (!Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.tipBefore)||bookingDataParcelable.tipBefore.equals("0")) {
					if (!inkiPayBalance.equals("0")) {
						layoutTip.setVisibility(View.VISIBLE);
					} else {
						layoutTip.setVisibility(View.GONE);
					}
				} else {
					layoutTip.setVisibility(View.GONE);
					bookingDataParcelable.tipBefore = "0";
				}
				btnRate.setVisibility(View.VISIBLE);
			}
		}else if(bookingDataParcelable.status!=null&&bookingDataParcelable.status.equals("passenger_cancel")){
			tvStatus.setText(getResources().getString(R.string.customer_cancel));
			tvStatus.setTextColor(Utility.getColor(getResources(), R.color.red_900, null));
			layoutRating.setVisibility(View.GONE);
			layoutTip.setVisibility(View.GONE);
			btnRate.setVisibility(View.GONE);
		}else if(bookingDataParcelable.status!=null&&bookingDataParcelable.status.equals("driver_cancel")){
			tvStatus.setText(getResources().getString(R.string.driver_cancel));
			tvStatus.setTextColor(Utility.getColor(getResources(), R.color.red_900, null));
			layoutRating.setVisibility(View.GONE);
			layoutTip.setVisibility(View.GONE);
			btnRate.setVisibility(View.GONE);
		}
	}

	private void setDriverDataView(BookingDataParcelable bookingDataParcelable){
		if(bookingDataParcelable.driverData.getDriver_id()==null){
			layoutRating.setVisibility(View.GONE);
		}
		else{
			tvDriverName.setText(bookingDataParcelable.driverData.getDriver_fullname());
			tvNoPlat.setText(bookingDataParcelable.driverData.getNumber_plate());
			tvNamaKendaraan.setText(bookingDataParcelable.driverData.getTransportation_name());
			PicassoLoader.loadProfile(BookingCompleteDetail.this, bookingDataParcelable.driverData.getDriver_avatar(), ivDriverPhoto, R.mipmap.img_no_avatar_driver);
		}
	}
	public void setBookingFragmentView(BookingDataParcelable bookingDataParcelable) {
		Fragment fragment = null;
		if(bookingDataParcelable.requestType.equals(Constants.TRANSPORT)||bookingDataParcelable.requestType.equals(Constants.CAR)){
			fragment = new BookingTransportFragment();
		}else if(bookingDataParcelable.requestType.equals(Constants.COURIER)){
			fragment = new BookingCourierFragment();
		}else if(bookingDataParcelable.requestType.equals(Constants.FOOD)){
			fragment = new BookingFoodFragment();
		}else if(bookingDataParcelable.requestType.equals(Constants.MART)){
			fragment = new BookingMarketFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putParcelable(Constants.BOOKING_DATA_PARCELABLE, bookingDataParcelable);
		fragment.setArguments(bundle);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.bookingFragment, fragment).commitAllowingStateLoss();
	}

	private void getRatingDriver() {
		progressBarNya = ProgressDialog.show(BookingCompleteDetail.this, "", "loading...");
		String api = Utility.getInstance().getTokenApi(BookingCompleteDetail.this);
		BookingController.getInstance(BookingCompleteDetail.this).postUpdateBookingRatingDriver(ratingParameters(),progressBarNya, api, getRatingDriverInterface);
		return;
	}
	public Map<String, String> ratingParameters() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", bookingDataParcelable.id);
		params.put("request_type", bookingDataParcelable.requestType);
		params.put("point_plus", ratingDriverDataTemp);
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.tipBefore)) {
			params.put("tip", bookingDataParcelable.tipBefore);
		}else{
			params.put("tip", "");
		}
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(commentRatingDriverData)) {
			params.put("comment", commentRatingDriverData);
		}else{
			params.put("comment", "");
		}
		return params;
	}

	BaseInterface getRatingDriverInterface = new BaseInterface() {
		@Override
		public void onSuccess(BaseCallback baseCallback) {
			int sukses = baseCallback.getSukses();
			String pesan = baseCallback.getPesan();
			if(sukses == 2){
				progressBarNya.dismiss();
				ratingDriverData = ratingDriverDataTemp;
				Toast.makeText(BookingCompleteDetail.this, pesan, Toast.LENGTH_SHORT).show();
				setDisableRate();
				btnGiveTip.setOnClickListener(null);
			} else {
				Toast.makeText(BookingCompleteDetail.this, pesan, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onError(APIErrorCallback apiErrorCallback) {
			progressBarNya.dismiss();
			if(apiErrorCallback.getError()!=null) {
				if (apiErrorCallback.getError().equals("Invalid API key ")) {
					Utility.getInstance().showInvalidApiKeyAlert(BookingCompleteDetail.this, getResources().getString(R.string.relogin));
				}else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
					Toast.makeText(BookingCompleteDetail.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(BookingCompleteDetail.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	public void setEnableRate(){
		btnRate.setVisibility(View.VISIBLE);
		ratingbar.setIsIndicator(false);
		edComment.setVisibility(View.VISIBLE);
		tvComment.setVisibility(View.GONE);
	}
	public void setDisableRate(){
		btnRate.setVisibility(View.GONE);
		ratingbar.setIsIndicator(true);
		edComment.setVisibility(View.GONE);
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(commentRatingDriverData)) {
			tvComment.setText(commentRatingDriverData + "");
		}else{
			tvComment.setText(getResources().getString(R.string.no_comment));
		}
		tvComment.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed()
	{
		if (bookingDataParcelable.status != null && bookingDataParcelable.status.equals("complete")) {
			if(activity.equals("broadcast")) {
				showMustRateAlertDialogBroadCast();
			}
			else if(activity.equals("rate")){
				showMustRateAlertDialog();
			}
			else if(activity.equals("listview")){
//				showMustRateAlertDialog();
				this.finish();
			}
		} else if ((bookingDataParcelable.status != null && bookingDataParcelable.status.equals("driver_cancel")) || ((bookingDataParcelable.status != null && bookingDataParcelable.status.equals("passenger_cancel")))){
			if(activity.equals("broadcast")) {
				Intent i = new Intent(this, MainActivityTab.class);
				i.putExtra("tab","1");
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}else if(activity.equals("listview")){
				this.finish();
			}

		}
		else {
			Toast.makeText(this, getResources().getString(R.string.please_rate), Toast.LENGTH_SHORT).show();
		}

	}
	private void showMustRateAlertDialogBroadCast() {
		if(ratingDriverData==null) {
			Utility.getInstance().showSimpleAlertDialog(this, null,getResources().getString(R.string.please_rate), "OK", positiveDialogListener, null,null, false);
		}else{
			Intent i = new Intent(this, MainActivityTab.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
	}
	private void showMustRateAlertDialog() {
		if(ratingDriverData==null) {
			Utility.getInstance().showSimpleAlertDialog(this, null,getResources().getString(R.string.please_rate), "OK", positiveDialogListener, null,null, false);
		}else{
			Intent intentToBooking = new Intent(this, MainActivityTab.class);
			intentToBooking.putExtra("tab", "1");
			intentToBooking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentToBooking);
			finish();
		}
	}

	public DialogInterface.OnClickListener positiveDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			dialogInterface.dismiss();
		}
	};
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
	}


	public void setTransportationTypeView(BookingDataParcelable bookingDataParcelable) {
		if(bookingDataParcelable.requestType.equals(Constants.TRANSPORT)){
			if(bookingDataParcelable.transportation.equals("motorcycle_taxi")){
				ivRequestType.setImageResource(R.mipmap.ic_logo_motorcycle);
				tvRequestType.setText(getResources().getString(R.string.app_name)+"-"+getResources().getString(R.string.indo_ojek));
			}
			else if(bookingDataParcelable.transportation.equals("pedicab")){
				ivRequestType.setImageResource(R.mipmap.ic_logo_pedicab);
				tvRequestType.setText(getResources().getString(R.string.app_name)+"-"+getResources().getString(R.string.indo_becak));
			}
		}else if(bookingDataParcelable.requestType.equals(Constants.COURIER)){
			ivRequestType.setImageResource(R.mipmap.ic_logo_courier);
			tvRequestType.setText(getResources().getString(R.string.app_name)+"-"+getResources().getString(R.string.indo_courier));
		}else if(bookingDataParcelable.requestType.equals(Constants.FOOD)){
			ivRequestType.setImageResource(R.mipmap.ic_logo_food);
			tvRequestType.setText(getResources().getString(R.string.app_name)+"-"+getResources().getString(R.string.indo_food));
		}else if(bookingDataParcelable.requestType.equals(Constants.MART)){
			ivRequestType.setImageResource(R.mipmap.ic_logo_mart);
			tvRequestType.setText(getResources().getString(R.string.app_name)+"-"+getResources().getString(R.string.market));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TIP_FOR_RIDER) {
			if (resultCode == RESULT_OK) {
				bookingDataParcelable.tipBefore = data.getStringExtra("tip_rider");
				if(bookingDataParcelable.tipBefore!=null&&bookingDataParcelable.tipBefore.equals("0")){
					tvTipDesc.setText(getResources().getString(R.string.please_give_tip));
					ivIconTip.setVisibility(View.VISIBLE);
					tvTip.setVisibility(View.GONE);
					tvGiveTip.setText(getResources().getString(R.string.give_tip));
				} else {
					tvTipDesc.setText("Tip Yang anda berikan :");
					ivIconTip.setVisibility(View.GONE);
					tvTip.setVisibility(View.VISIBLE);
					tvTip.setText("Rp." + "" +Utility.getInstance().convertPrice(bookingDataParcelable.tipBefore));
					tvGiveTip.setText(getResources().getString(R.string.change_nominal_tip));
				}
			} else if (resultCode == RESULT_CANCELED) {

			}
		}
	}
}

