package com.pmberjaya.indotiki.app.bookingData.bookingProgress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingData.bookingComplete.BookingCompleteDetail;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingCourierFragment;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingFoodFragment;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingMarketFragment;
import com.pmberjaya.indotiki.app.bookingData.bookingFragment.BookingTransportFragment;
import com.pmberjaya.indotiki.app.chat.ChatActivity;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.callbacks.bookingData.BookingCancelCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.DistanceTimeGMapsCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingCancelInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.DistanceTimeInterface;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberDetailData;
import com.pmberjaya.indotiki.models.bookingData.DriverData;
import com.pmberjaya.indotiki.models.bookingData.DriverPositionMapData;
import com.pmberjaya.indotiki.models.gmaps.DistanceTimeGMapsData.Duration;
import com.pmberjaya.indotiki.models.gmaps.DistanceTimeGMapsData.Element;
import com.pmberjaya.indotiki.models.gmaps.DistanceTimeGMapsData.Row;
import com.pmberjaya.indotiki.models.mart.MartItemTempData;
import com.pmberjaya.indotiki.models.others.CheckStatusBookingData;
import com.pmberjaya.indotiki.models.parcelables.BookingCourierDatas;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.BookingTransportDatas;
import com.pmberjaya.indotiki.models.parcelables.FoodItemTempData;
import com.pmberjaya.indotiki.models.parcelables.ReceiveBroadCastParcelable;
import com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edwin on 4/21/2016.
 */
public class BookingInProgressDetail extends BaseActivity implements BaseActivity.BroadcastReceiverInterface {
    private static final int GET_REASON_CANCEL_BOOKING = 0;
    public static Runnable runnable;
    public static Handler handler;
    SessionManager session;
    public String userId;
    public DBController dbController;
    public boolean runGetBookingInProgressMember = true;
    private LinearLayout loadinglayout;
    private LinearLayout nobookinglayout;
    private LinearLayout cancellayout;
    private LinearLayout location_layout;
    private LinearLayout driver_data_layout;
    private LinearLayout info_driver_layout;
    private LinearLayout call_layout;
    private LinearLayout chat_layout;
    private RelativeLayout menulayout;
    private LinearLayout bt_try_again;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private TextView tv_error_timeout;
    private RelativeLayout error_layout;
    private String TAG_REQUEST_ID = "requestId";
    private String TAG_REQUEST_TYPE = "requestType";
    private String channel;
    private TextView tv_arriving_time;
    private TextView tv_driver_go_to_location;
    private TextView tv_nama_supir;
    private TextView tv_nohp_supir;
    private ImageView iv_foto_supir;
    private ImageView iv_request_type;
    private TextView tv_request_type;
    private TextView tv_no_plat_driver;
    private TextView tv_nama_kendaraan_driver;
    private String lat_distance;
    private String lng_distance;
    private String deposit_paid;
    private String cash_paid;
    private String confirm_status;
    private LinearLayout bookinginprogress_layout;
    BookingDataParcelable bookingDataParcelable;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbController = DBController.getInstance(this);
        setContentView(R.layout.booking_in_progress_detail_activity);
        setBroadCastReceiverInterface(this);
        initToolbar(getResources().getString(R.string.booking_in_progress));
        getIntentExtra();
        renderView();
        init();

//      getBookingInProgressMember(bookingDataParcelable.id, bookingDataParcelable.requestType);
    }

    private void init() {
        call_layout.setOnClickListener(callDriver);
        chat_layout.setOnClickListener(chatDriver);
        cancellayout.setOnClickListener(cancelBooking);
        location_layout.setOnClickListener(getDriverLocation);
        bt_try_again.setOnClickListener(try_again_listener);
    }
    Intent intent;
    public void getIntentExtra() {
        intent = getIntent();
        bookingDataParcelable = new BookingDataParcelable();
        bookingDataParcelable.id = intent.getStringExtra(TAG_REQUEST_ID);
        bookingDataParcelable.requestType = intent.getStringExtra(TAG_REQUEST_TYPE);
        confirm_status=intent.getStringExtra("confirm");

//        tip_rider = i.getStringExtra("tip_rider");
//        minprice = i.getStringExtra("min_price");
//        maxprice = i.getStringExtra("max_price");
    }

    private void renderView() {
        call_layout = (LinearLayout) findViewById(R.id.call_layout);
        chat_layout = (LinearLayout) findViewById(R.id.chat_layout);
        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        nobookinglayout = (LinearLayout) findViewById(R.id.nobookinglayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        menulayout = (RelativeLayout) findViewById(R.id.menuLayout);
        location_layout = (LinearLayout) findViewById(R.id.location_layout);
        driver_data_layout = (LinearLayout) findViewById(R.id.driver_data_layout);
        info_driver_layout = (LinearLayout) findViewById(R.id.info_driver_layout);
        cancellayout = (LinearLayout) findViewById(R.id.cancellayout);

        tv_arriving_time = (TextView) findViewById(R.id.tv_arriving_time);
        tv_driver_go_to_location = (TextView) findViewById(R.id.tv_driver_go_to_location);
        tv_nama_supir = (TextView) findViewById(R.id.tv_driver_fullname);
        tv_nohp_supir = (TextView) findViewById(R.id.tv_driver_phone);
        iv_foto_supir = (ImageView) findViewById(R.id.iv_driver_photo);
        iv_request_type = (ImageView) findViewById(R.id.request_type);
        tv_request_type = (TextView) findViewById(R.id.requestTypeTxt);
        tv_no_plat_driver = (TextView) findViewById(R.id.tv_no_plat_driver);
        tv_nama_kendaraan_driver = (TextView) findViewById(R.id.tv_nama_kendaraan_driver);
        bookinginprogress_layout = (LinearLayout) findViewById(R.id.booking_in_progress_layout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        tv_error_timeout = (TextView) findViewById(R.id.tvErrorMessage);
        bt_try_again = (LinearLayout) findViewById(R.id.btnError);
        error_layout = (RelativeLayout) findViewById(R.id.layoutError);
    }

    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            renderloadingView();
            getBookingInProgressMember(bookingDataParcelable.id, bookingDataParcelable.requestType);
        }
    };

    public void renderloadingView() {
        loadinglayout.setVisibility(View.VISIBLE);
        bookinginprogress_layout.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }

    public void renderErrorView() {
        loadinglayout.setVisibility(View.GONE);
        bookinginprogress_layout.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
//        cancellayout.setVisibility(View.GONE);
        tv_error_timeout.setText(R.string.currently_unavailable);
    }

    private String lat_from;
    private String lng_from;
    private String lat_to;
    private String lng_to;

    private String from_place;
    private String from_detail;
    private String to_place;
    private String to_detail;
    private View.OnClickListener getDriverLocation = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BookingInProgressDetail.this, BookingInProgressMap.class);
            intent.putExtra("driver_id", bookingDataParcelable.driverData.getDriver_id());
            intent.putExtra("request_id", bookingDataParcelable.id);
            intent.putExtra("request_type", bookingDataParcelable.requestType);
            intent.putExtra("lat_from", lat_from);
            intent.putExtra("lng_from", lng_from);
            intent.putExtra("lat_to", lat_to);
            intent.putExtra("lng_to", lng_to);
            intent.putExtra("driver_name", bookingDataParcelable.driverData.getDriver_fullname());
            intent.putExtra("driver_phone", bookingDataParcelable.driverData.getDriver_phone());
            intent.putExtra("driver_avatar", bookingDataParcelable.driverData.getDriver_avatar());
            intent.putExtra("driver_plate_number", bookingDataParcelable.driverData.getNumber_plate());
            intent.putExtra("transportation", bookingDataParcelable.transportation);
            intent.putExtra("from_place", from_place);
            intent.putExtra("from_detail", from_detail);
            intent.putExtra("to_place", to_place);
            intent.putExtra("to_detail", to_detail);
            startActivity(intent);
        }

    };
    private View.OnClickListener chatDriver = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BookingInProgressDetail.this, ChatActivity.class);
            intent.putExtra("request_id", bookingDataParcelable.id);
            intent.putExtra("request_type", bookingDataParcelable.requestType);
            intent.putExtra("user_id_booking", bookingDataParcelable.driverData.getDriver_id());
            intent.putExtra("user_name", bookingDataParcelable.driverData.getDriver_fullname());
            intent.putExtra("user_phone", bookingDataParcelable.driverData.getDriver_phone());
            intent.putExtra("user_avatar", bookingDataParcelable.driverData.getDriver_avatar());
            intent.putExtra("channel", channel);
            startActivity(intent);
        }
    };
    private String nohp;
    private View.OnClickListener callDriver = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent callintent = new Intent(Intent.ACTION_DIAL);
            callintent.setData(Uri.parse("tel:" + bookingDataParcelable.driverData.getDriver_phone()));
            startActivity(callintent);
        }

    };
    private String status;
    private String forceCancel;
    private View.OnClickListener cancelBooking = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (status.equals("waiting")) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(BookingInProgressDetail.this);
                builder2.setIcon(R.mipmap.ic_launcher);
                builder2.setTitle(getResources().getString(R.string.confirm));
                builder2.setMessage(getResources().getString(R.string.cancel_confirmation)).setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                forceCancel = "1";
//                                ResponseRequestCancel(requestId, requestType, forceCancel);
                                Intent i = new Intent(BookingInProgressDetail.this, BookingReasonCancel.class);
                                startActivityForResult(i, GET_REASON_CANCEL_BOOKING);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            } else {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(BookingInProgressDetail.this);
                builder2.setIcon(R.mipmap.ic_launcher);
                builder2.setTitle(getResources().getString(R.string.confirm));
                builder2.setMessage(getResources().getString(R.string.cancel_confirmation2)).setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                forceCancel = "0";
//                                ResponseRequestCancel(requestId, requestType, forceCancel);
                                Intent i = new Intent(BookingInProgressDetail.this, BookingReasonCancel.class);
                                startActivityForResult(i, GET_REASON_CANCEL_BOOKING);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }
        }

    };

    public void getBookingInProgressMember(String requestId, String requestType) {
        Log.d("alasldfdsalf", requestId + "," + requestType);
        runGetBookingInProgressMember = true;
        String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
        BookingController.getInstance(BookingInProgressDetail.this).getBookingInProgressDetail(requestId, requestType, api, getBookingInProgressInterface);
        return;
    }

    BaseGenericInterface getBookingInProgressInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            runGetBookingInProgressMember = false;
            int suksesData = baseGenericCallback.getSukses();
            if (suksesData == 2) {
                BookingInProgressMemberDetailData bookingInProgressMemberData = (BookingInProgressMemberDetailData) baseGenericCallback.getData();
                channel = bookingInProgressMemberData.getChannel();
                bookingDataParcelable = buildBookingDataParcelable((BaseGenericCallback<BookingInProgressMemberDetailData>) baseGenericCallback);
                from_place = bookingInProgressMemberData.getFrom_place();
                to_place = bookingInProgressMemberData.getTo_place();
                from_detail = bookingInProgressMemberData.getFrom();
                to_detail = bookingInProgressMemberData.getTo();
                lat_from = bookingInProgressMemberData.getLat_from();
                lng_from = bookingInProgressMemberData.getLng_from();
                lat_to = bookingInProgressMemberData.getLat_to();
                lng_to = bookingInProgressMemberData.getLng_to();
                status = bookingInProgressMemberData.getStatus();
                deposit_paid = bookingInProgressMemberData.getDeposit_paid();
                cash_paid = bookingInProgressMemberData.getCash_paid();
                bookinginprogress_layout.setVisibility(View.VISIBLE);
                if (channel != null) {
                    setDriverDataView(bookingDataParcelable);
                    setTransportationTypeView(bookingDataParcelable);
                    setBookingFragmentView(bookingDataParcelable);
                    setStatusBooking(bookingDataParcelable);
                    loadinglayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    menulayout.setVisibility(View.VISIBLE);
                    nobookinglayout.setVisibility(View.GONE);
                } else {
                    loadinglayout.setVisibility(View.GONE);
                    nobookinglayout.setVisibility(View.VISIBLE);
                }
            } else {
                loadinglayout.setVisibility(View.GONE);
                nobookinglayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(BookingInProgressDetail.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    renderErrorView();
                    Toast.makeText(BookingInProgressDetail.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                } else{
                    renderErrorView();
                    Toast.makeText(BookingInProgressDetail.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public BookingDataParcelable buildBookingDataParcelable(BaseGenericCallback<BookingInProgressMemberDetailData> baseGenericCallback) {
        BookingInProgressMemberDetailData bookingInProgressMemberDetailData = baseGenericCallback.getData();
        bookingDataParcelable.requestTime = bookingInProgressMemberDetailData.getRequest_time();
        bookingDataParcelable.acceptTime = bookingInProgressMemberDetailData.getAccept_time();
        bookingDataParcelable.channel = bookingInProgressMemberDetailData.getChannel();
        bookingDataParcelable.distance = bookingInProgressMemberDetailData.getDistance();

        bookingDataParcelable.id = bookingInProgressMemberDetailData.getId();
        bookingDataParcelable.originalPrice = bookingInProgressMemberDetailData.getOriginal_price();
        bookingDataParcelable.payment = bookingInProgressMemberDetailData.getPayment();
        bookingDataParcelable.price = bookingInProgressMemberDetailData.getPrice();
        bookingDataParcelable.promoCode = bookingInProgressMemberDetailData.getPromo();
        bookingDataParcelable.promoPrice = bookingInProgressMemberDetailData.getPromo_price();
        bookingDataParcelable.transportation = bookingInProgressMemberDetailData.getTransportation();
        bookingDataParcelable.status = bookingInProgressMemberDetailData.getStatus();
        bookingDataParcelable.tip = bookingInProgressMemberDetailData.getTip();
        bookingDataParcelable.depositPaid = bookingInProgressMemberDetailData.getDeposit_paid();
        bookingDataParcelable.cashPaid = bookingInProgressMemberDetailData.getCash_paid();

        bookingDataParcelable.fromPlace = bookingInProgressMemberDetailData.getFrom_place();
        bookingDataParcelable.toPlace = bookingInProgressMemberDetailData.getTo_place();
        bookingDataParcelable.from = bookingInProgressMemberDetailData.getFrom();
        bookingDataParcelable.to = bookingInProgressMemberDetailData.getTo();

        bookingDataParcelable.latFrom = Utility.getInstance().parseDecimal(bookingInProgressMemberDetailData.getLat_from());
        bookingDataParcelable.latTo = Utility.getInstance().parseDecimal(bookingInProgressMemberDetailData.getLat_to());
        bookingDataParcelable.lngFrom = Utility.getInstance().parseDecimal(bookingInProgressMemberDetailData.getLng_from());
        bookingDataParcelable.lngTo = Utility.getInstance().parseDecimal(bookingInProgressMemberDetailData.getLng_to());
        //----------------------------set driver model-----------------------------------------------------------
        bookingDataParcelable.driverData = new DriverData();
        bookingDataParcelable.driverData.setDriver_avatar(bookingInProgressMemberDetailData.getDriver_avatar());
        bookingDataParcelable.driverData.setDriver_fullname(bookingInProgressMemberDetailData.getDriver_fullname());
        bookingDataParcelable.driverData.setDriver_id(bookingInProgressMemberDetailData.getDriver_id());
        bookingDataParcelable.driverData.setDriver_phone(bookingInProgressMemberDetailData.getDriver_phone());
        bookingDataParcelable.driverData.setNumber_plate(bookingInProgressMemberDetailData.getNumber_plate());
        bookingDataParcelable.driverData.setTransportation_name(bookingInProgressMemberDetailData.getTransportation_name());
        //----------------------------set driver model-----------------------------------------------------------
        if (bookingDataParcelable.requestType.equals(Constants.TRANSPORT) || bookingDataParcelable.requestType.equals(Constants.CAR)) {
            bookingDataParcelable.bookingTransportDatas = buildBookingTransportDatas(bookingInProgressMemberDetailData);
        } else if (bookingDataParcelable.requestType.equals(Constants.COURIER)) {
            bookingDataParcelable.bookingCourierDatas = buildBookingCourierDatas(bookingInProgressMemberDetailData);
        } else if (bookingDataParcelable.requestType.equals(Constants.FOOD)) {
//            bookingDataParcelable.bookingFoodDatas = buildBookingFoodDatas(bookingInProgressMemberDetailData);
        } else if(bookingDataParcelable.requestType.equals(Constants.MART)){
//            bookingDataParcelable.bookingMartDatas = buildBookingMartDatas(bookingInProgressMemberDetailData);
        }
        return bookingDataParcelable;
    }

    public BookingTransportDatas buildBookingTransportDatas(BookingInProgressMemberDetailData bookingInProgressMemberDetailData) {
        BookingTransportDatas bookingTransportDatas = new BookingTransportDatas();
        bookingTransportDatas.locationDetail = bookingInProgressMemberDetailData.getLocation_detail();
        return bookingTransportDatas;
    }

    public BookingCourierDatas buildBookingCourierDatas(BookingInProgressMemberDetailData bookingInProgressMemberDetailData) {
        BookingCourierDatas bookingCourierDatas = new BookingCourierDatas();

        bookingCourierDatas.item = bookingInProgressMemberDetailData.getItem();
        bookingCourierDatas.item_photo = bookingInProgressMemberDetailData.getItem_photo();
        bookingCourierDatas.name_sender = bookingInProgressMemberDetailData.getName_sender();
        bookingCourierDatas.phone_sender = bookingInProgressMemberDetailData.getPhone_sender();
        bookingCourierDatas.location_detail_sender = bookingInProgressMemberDetailData.getLocation_detail_sender();
        bookingCourierDatas.name_receiver = bookingInProgressMemberDetailData.getName_receiver();
        bookingCourierDatas.phone_receiver = bookingInProgressMemberDetailData.getPhone_receiver();
        bookingCourierDatas.location_detail_receiver = bookingInProgressMemberDetailData.getLocation_detail_receiver();
        return bookingCourierDatas;
    }


    public List<FoodItemTempData> buildFoodItemTempData(BookingInProgressMemberDetailData bookingInProgressMemberDetailData) {
        String itemsData = bookingInProgressMemberDetailData.getItem();
        String note = bookingInProgressMemberDetailData.getNote();
        String quantity = bookingInProgressMemberDetailData.getQuantity();
        String price_per_item = bookingInProgressMemberDetailData.getPrice_per_item();
        List<FoodItemTempData> foodItemTempDatas = new ArrayList<FoodItemTempData>();

        if (bookingInProgressMemberDetailData.getTotal_item() != null) {
            quantity = quantity.replace("\"", "");
            quantity = quantity.replace("[", "");
            quantity = quantity.replace("]", "");
            String[] quantitySplit = quantity.split(",");

            itemsData = itemsData.replace("\"", "");
            itemsData = itemsData.replace("[", "");
            itemsData = itemsData.replace("]", "");
            String[] itemsSplit = itemsData.split(",");

            price_per_item = price_per_item.replace("\"", "");
            price_per_item = price_per_item.replace("[", "");
            price_per_item = price_per_item.replace("]", "");
            String[] pricePerItemSplit = price_per_item.split(",");

            note = note.replace("[\"", "");
            note = note.replace("\"]", "");
            String[] noteSplit = note.split("\",\"");

            for (int i = 0; i < Integer.parseInt(bookingInProgressMemberDetailData.getTotal_item()); i++) {
                FoodItemTempData foodItemTempData = new FoodItemTempData();
                foodItemTempData.setPrice(pricePerItemSplit[i]);
                foodItemTempData.setLabel(itemsSplit[i]);
                if (noteSplit[i] != null && !noteSplit[i].equals("NULL")) {
                    foodItemTempData.setDescription_note(noteSplit[i]);
                }
                foodItemTempData.setQuantity(Integer.parseInt(quantitySplit[i]));
                foodItemTempDatas.add(foodItemTempData);
                bookingDataParcelable.bookingFoodDatas.foodCost = bookingDataParcelable.bookingFoodDatas.foodCost + Integer.parseInt(pricePerItemSplit[i]) * Integer.parseInt(quantitySplit[i]);
            }

        }
        return foodItemTempDatas;
    }



    public List<MartItemTempData> buildMartItemTempData(BookingInProgressMemberDetailData bookingInProgressMemberDetailData) {
        String itemsData = bookingInProgressMemberDetailData.getItem();
        String note = bookingInProgressMemberDetailData.getNote();
        String quantity = bookingInProgressMemberDetailData.getQuantity();
        String price_per_item = bookingInProgressMemberDetailData.getPrice_per_item();
        List<MartItemTempData> martItemTempDatas = new ArrayList<MartItemTempData>();

        if (bookingInProgressMemberDetailData.getTotal_item() != null) {
            quantity = quantity.replace("\"", "");
            quantity = quantity.replace("[", "");
            quantity = quantity.replace("]", "");
            String[] quantitySplit = quantity.split(",");

            itemsData = itemsData.replace("\"", "");
            itemsData = itemsData.replace("[", "");
            itemsData = itemsData.replace("]", "");
            String[] itemsSplit = itemsData.split(",");

            price_per_item = price_per_item.replace("\"", "");
            price_per_item = price_per_item.replace("[", "");
            price_per_item = price_per_item.replace("]", "");
            String[] pricePerItemSplit = price_per_item.split(",");

            note = note.replace("[\"", "");
            note = note.replace("\"]", "");
            String[] noteSplit = note.split("\",\"");

            for (int i = 0; i < Integer.parseInt(bookingInProgressMemberDetailData.getTotal_item()); i++) {
                MartItemTempData martItemTempData = new MartItemTempData();
                martItemTempData.setPrice(pricePerItemSplit[i]);
                martItemTempData.setLabel(itemsSplit[i]);
                if (noteSplit[i] != null && !noteSplit[i].equals("NULL")) {
                    martItemTempData.setDescription_note(noteSplit[i]);
                }
                martItemTempData.setQuantity(Integer.parseInt(quantitySplit[i]));
                martItemTempDatas.add(martItemTempData);
                bookingDataParcelable.bookingMartDatas.itemCost = bookingDataParcelable.bookingMartDatas.itemCost + Integer.parseInt(pricePerItemSplit[i]) * Integer.parseInt(quantitySplit[i]);
            }

        }
        return martItemTempDatas;
    }


    public int buildFoodCostData(List<FoodItemTempData> foodItemTempDatas, List<FoodItemTempData> foodItemTempManuallyDatas) {
        int foodCost = 0;
        for (int i = 0; i < foodItemTempDatas.size(); i++) {
            foodCost = foodCost + Utility.getInstance().parseInteger(foodItemTempDatas.get(i).getPrice());
        }
        for (int i = 0; i < foodItemTempManuallyDatas.size(); i++) {
            foodCost = foodCost + Utility.getInstance().parseInteger(foodItemTempManuallyDatas.get(i).getPrice());
        }
        return foodCost;
    }

    private void setDriverDataView(BookingDataParcelable bookingDataParcelable) {
        tv_nama_supir.setText(bookingDataParcelable.driverData.getDriver_fullname());
        tv_nohp_supir.setText(bookingDataParcelable.driverData.getDriver_phone());
        tv_no_plat_driver.setText(bookingDataParcelable.driverData.getNumber_plate());
        tv_nama_kendaraan_driver.setText(bookingDataParcelable.driverData.getTransportation_name());
        PicassoLoader.loadProfile(BookingInProgressDetail.this, bookingDataParcelable.driverData.getDriver_avatar(), iv_foto_supir, R.mipmap.img_no_avatar_driver);
    }

    public void setStatusBooking(BookingDataParcelable bookingDataParcelable) {
        if (bookingDataParcelable.requestType.equals(Constants.TRANSPORT) || bookingDataParcelable.requestType.equals(Constants.CAR)) {
            if (bookingDataParcelable.status.equals("accept")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                lat_distance = lat_from;
                lng_distance = lng_from;
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else if (bookingDataParcelable.status.equals("pick_up")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_detail);
                }
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else {
                driver_data_layout.setVisibility(View.GONE);
            }
        } else if (bookingDataParcelable.requestType.equals(Constants.COURIER)) {
            if (bookingDataParcelable.status.equals("accept")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                lat_distance = lat_from;
                lng_distance = lng_from;
                if (from_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier) + " " + from_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier) + " " + from_detail);
                }
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else if (bookingDataParcelable.status.equals("pick_up")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier2) + " " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier2) + " " + to_detail);
                }
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else {
                driver_data_layout.setVisibility(View.GONE);
            }
        } else if (bookingDataParcelable.requestType.equals(Constants.FOOD)) {
            if (bookingDataParcelable.status.equals("accept")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                lat_distance = lat_from;
                lng_distance = lng_from;
                if (from_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + from_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + from_detail);
                }
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else if (bookingDataParcelable.status.equals("pick_up")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + to_detail);
                }
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else {
                driver_data_layout.setVisibility(View.GONE);
            }
        } else if (bookingDataParcelable.requestType.equals(Constants.TAXI)) {
            if (bookingDataParcelable.status.equals("accept")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                lat_distance = lat_from;
                lng_distance = lng_from;
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else if (bookingDataParcelable.status.equals("pick_up")) {
                driver_data_layout.setVisibility(View.VISIBLE);
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_detail);
                }
                startHandler(bookingDataParcelable.driverData.getDriver_id());
            } else {
                driver_data_layout.setVisibility(View.GONE);
            }
        }
    }

    public void setTransportationTypeView(BookingDataParcelable bookingDataParcelable) {
        if (bookingDataParcelable.requestType.equals(Constants.TRANSPORT)) {
            if (bookingDataParcelable.transportation.equals("motorcycle_taxi")) {
                iv_request_type.setImageResource(R.mipmap.ic_logo_motorcycle);
                tv_request_type.setText(getResources().getString(R.string.app_name) + "-" + getResources().getString(R.string.indo_ojek));
            } else if (bookingDataParcelable.transportation.equals("pedicab")) {
                iv_request_type.setImageResource(R.mipmap.ic_logo_pedicab);
                tv_request_type.setText(getResources().getString(R.string.app_name) + "-" + getResources().getString(R.string.indo_becak));
            }
        } else if (bookingDataParcelable.requestType.equals(Constants.COURIER)) {
            iv_request_type.setImageResource(R.mipmap.ic_logo_courier);
            tv_request_type.setText(getResources().getString(R.string.app_name) + "-" + getResources().getString(R.string.indo_courier));
        } else if (bookingDataParcelable.requestType.equals(Constants.FOOD)) {
            iv_request_type.setImageResource(R.mipmap.ic_logo_food);
            tv_request_type.setText(getResources().getString(R.string.app_name) + "-" + getResources().getString(R.string.indo_food));
        }else if (bookingDataParcelable.requestType.equals(Constants.MART)) {
            iv_request_type.setImageResource(R.mipmap.ic_logo_mart);
            tv_request_type.setText(getResources().getString(R.string.app_name) + "-" + getResources().getString(R.string.market));
        }
    }
    Fragment fragment = null;
    public void setBookingFragmentView(BookingDataParcelable bookingDataParcelable){
        if (bookingDataParcelable.requestType.equals(Constants.TRANSPORT) || bookingDataParcelable.requestType.equals(Constants.CAR)) {
            fragment = new BookingTransportFragment();
        } else if (bookingDataParcelable.requestType.equals(Constants.COURIER)) {
            fragment = new BookingCourierFragment();
        } else if (bookingDataParcelable.requestType.equals(Constants.FOOD)) {
            fragment = new BookingFoodFragment();
        }else if(bookingDataParcelable.requestType.equals(Constants.MART)){
            fragment = new BookingMarketFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BOOKING_DATA_PARCELABLE, bookingDataParcelable);
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.booking_fragment, fragment).commitAllowingStateLoss();
    }





    public void ResponseRequestCancel(String requestId, String requestType, String isForceCancel, String reason) {
        String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
        BookingController.getInstance(BookingInProgressDetail.this).postRequestResponseCancel(responseParameters(requestId, requestType, isForceCancel, reason), api, bookingCancelInterface);
        return;
    }

    public Map<String, String> responseParameters(String requestId, String requestType, String isForceCancel, String reason) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", requestId);
        params.put("request_type", requestType);
        params.put("is_force_cancel", isForceCancel);
        params.put("comment", reason);
        return params;
    }


    public void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
//                    Intent intentToBooking = new Intent(BookingInProgressDetail.this, MainActivityTab.class);
//                    intentToBooking.putExtra("tab", "1");
//                    intentToBooking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intentToBooking);
//                    finish();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);


        }

    }

    public void getDistanceTimePlace(String latitude_from, String longitude_from, String latitude_to, String longitude_to) {
        //String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
        //String output = "json";
        String origin = latitude_from + "," + longitude_from;
        String destinations = latitude_to + "," + longitude_to;
        UtilityController.getInstance(BookingInProgressDetail.this).getDistanceTimePlace(distanceParameters(origin, destinations), distanceTimeInterface);
        return;
    }

    DistanceTimeInterface distanceTimeInterface = new DistanceTimeInterface() {
        @Override
        public void onSuccessGetDistanceTime(DistanceTimeGMapsCallback distanceTimeGMapsCallback) {
            String status = distanceTimeGMapsCallback.getStatus();
            if (status.equals("OK")) {
                List<Row> rowsArray = distanceTimeGMapsCallback.getRows();
                List<Element> elementsArray = rowsArray.get(0).getElements();
                String status2 = elementsArray.get(0).getStatus();
                if (status2.equals("ZERO_RESULTS")) {
//                    tv_arriving_time.setText(getResources().getString(R.string.info_driver_error) );
                    tv_arriving_time.setVisibility(View.GONE);
                } else {
                    Duration durationData = elementsArray.get(0).getDuration();
                    int durationValue = durationData.getValue();
                    String duration;
                    if(durationValue<500){
                        duration = "5 "+ getResources().getString(R.string.minutes);
                    }else{
                        duration = durationData.getText().replace("mins", getResources().getString(R.string.minutes));
                    }
                    Log.d("DURATIONVALUE",""+durationValue);
                    tv_arriving_time.setVisibility(View.VISIBLE);
                    tv_arriving_time.setText(getResources().getString(R.string.info_driver2) + " " + duration);
                }
            } else {
//                Toast.makeText(BookingInProgressDetail.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onErrorGetDistanceTime(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(BookingInProgressDetail.this, getResources().getString(R.string.relogin));
                } else {
                }
            }
        }
    };
    String reason;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_REASON_CANCEL_BOOKING) {
            if (resultCode == RESULT_OK) {
                reason = data.getStringExtra("reason");
                ResponseRequestCancel(bookingDataParcelable.id, bookingDataParcelable.requestType, forceCancel, reason);
            } else if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public Map<String, String> distanceParameters(String origins, String destinations) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("origins", origins);
        params.put("destinations", destinations);
        return params;
    }

    public void startHandler(final String driverId) {
        getDriverPositionMap(driverId);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                  /* do what you need to do */
                getDriverPositionMap(driverId);
	    	      /* and here comes the "trick" */
                handler.postDelayed(this, 20000);
            }
        };
        handler.postDelayed(runnable, 20000);
    }

    public void stopHandler() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void getDriverPositionMap(String driverId) {
        String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
        BookingController.getInstance(BookingInProgressDetail.this).getDriverPositionMap(driverId, api, getDriverPositionInterface);
        return;
    }
    BaseGenericInterface getDriverPositionInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> driverPositionBookingCallback) {
            int sukses = driverPositionBookingCallback.getSukses();
            if (sukses == 2) {
                DriverPositionMapData data = (DriverPositionMapData) driverPositionBookingCallback.getData();
                if (data == null) {
//                    tv_arriving_time.setText(getResources().getString(R.string.info_driver_error));
                    tv_arriving_time.setVisibility(View.GONE);
                } else {
                    String lat_driver = data.getLat();
                    String lng_driver = data.getLng();
                    getDistanceTimePlace(lat_driver, lng_driver, lat_distance, lng_distance);
                }

            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {

        }
    };
    public void getCheckStatusBooking(String requestId, String requestType) {
        String api = Utility.getInstance().getTokenApi(BookingInProgressDetail.this);
        BookingController.getInstance(BookingInProgressDetail.this).getCheckStatusBooking(requestId, requestType, api, checkStatusBookingInterface);
        return;
    }

    BaseGenericInterface checkStatusBookingInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            int sukses = baseGenericCallback.getSukses();
            if (sukses == 2) {
                CheckStatusBookingData data = (CheckStatusBookingData) baseGenericCallback.getData();
                if (data != null) {
                    String status = data.getStatus();
                    if (bookingDataParcelable.requestType.equals("transport")) {
                        if (status.equals("pick_up")) {
                            cancellayout.setVisibility(View.GONE);
                            lat_distance = lat_to;
                            lng_distance = lng_to;
                            if (to_place != null) {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_place);
                            } else {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_detail);
                            }
                        }
                    } else if (bookingDataParcelable.requestType.equals("courier")) {
                        if (status.equals("pick_up")) {
                            cancellayout.setVisibility(View.GONE);
                            lat_distance = lat_to;
                            lng_distance = lng_to;
                            if (to_place != null) {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier2) + " " + to_place);
                            } else {

                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier2) + " " + to_detail);
                            }
                        }
                    } else if (bookingDataParcelable.requestType.equals("food")) {
                        if (status.equals("pick_up")) {
                            cancellayout.setVisibility(View.GONE);
                            lat_distance = lat_to;
                            lng_distance = lng_to;
                            if (to_place != null) {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + to_place);
                            } else {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + to_detail);
                            }
                        }
                    }else if (bookingDataParcelable.requestType.equals("mart")) {
                        if (status.equals("pick_up")) {
                            cancellayout.setVisibility(View.GONE);
                            lat_distance = lat_to;
                            lng_distance = lng_to;
                            if (to_place != null) {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + to_place);
                            } else {
                                tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_food2) + " " + to_detail);
                            }
                        }
                    }
                } else {
                    dbController.deleteChatHistory(bookingDataParcelable.id, bookingDataParcelable.requestType);
                    PicassoLoader.deleteImageFromDir(BookingInProgressDetail.this, bookingDataParcelable.requestType, bookingDataParcelable.id);
                    Intent intentToBooking = new Intent(BookingInProgressDetail.this, BookingCompleteDetail.class);
                    intentToBooking.putExtra("requestType", bookingDataParcelable.requestType);
                    intentToBooking.putExtra("requestId", bookingDataParcelable.id);
                    intentToBooking.putExtra("activity", "broadcast");
                    intentToBooking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentToBooking);
                    finish();
                }
            } else {
//                    Toast.makeText(BookingInProgressDetail.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    Utility.getInstance().showInvalidApiKeyAlert(BookingInProgressDetail.this, getResources().getString(R.string.relogin));
                } else {
                    renderErrorView();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (runGetBookingInProgressMember == true) {
            getBookingInProgressMember(bookingDataParcelable.id, bookingDataParcelable.requestType);
        }
        getCheckStatusBooking(bookingDataParcelable.id, bookingDataParcelable.requestType);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopHandler();
    }

    @Override
    public void onBackPressed() {
        if(intent.getAction()!=null&&intent.getAction().equals(Constants.SEARCH_DRIVER)){
            Intent intentToBooking = new Intent(BookingInProgressDetail.this, MainActivityTab.class);
            intentToBooking.setAction(Constants.SEARCH_DRIVER);
            intentToBooking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentToBooking);
            finish();
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.booking_in_progress_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            getBookingInProgressMember(bookingDataParcelable.id, bookingDataParcelable.requestType);
            getCheckStatusBooking(bookingDataParcelable.id, bookingDataParcelable.requestType);
        }
        return super.onOptionsItemSelected(item);
    }


    BookingCancelInterface bookingCancelInterface = new BookingCancelInterface() {
        @Override
        public void onSuccessCancelBooking(BookingCancelCallback bookingCancelCallback) {
            int sukses = bookingCancelCallback.getSukses();
            String pesan = bookingCancelCallback.getPesan();
            if (sukses == 2) {
                dbController.deleteChatHistory(bookingDataParcelable.id, bookingDataParcelable.requestType);
                PicassoLoader.deleteImageFromDir(BookingInProgressDetail.this, bookingDataParcelable.requestType, bookingDataParcelable.id);
                boolean suspend = bookingCancelCallback.getSuspend();
                if (suspend) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(BookingInProgressDetail.this);
                    builder.setTitle(getResources().getString(R.string.app_name))
                            .setMessage(pesan)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    SessionManager session = new SessionManager(BookingInProgressDetail.this);
                                    session.logoutUser();
//                                Intent intent = new Intent(BookingInProgressDetail.this, MainMenuActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
                                    stopService(new Intent(BookingInProgressDetail.this, FCMRealtimeDatabaseHandler.class));
                                    BookingInProgressDetail.this.finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                } else {
                    Intent confirm = new Intent(BookingInProgressDetail.this, MainActivityTab.class);
                    confirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                confirm.putExtra("userlevel", "Member");
                    confirm.putExtra("tab", "1");
                    startActivity(confirm);
                    Toast.makeText(BookingInProgressDetail.this, pesan, Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(BookingInProgressDetail.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onErrorCancelBooking(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    Utility.getInstance().showInvalidApiKeyAlert(BookingInProgressDetail.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(BookingInProgressDetail.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onReceiveBroadCast(ReceiveBroadCastParcelable receiveBroadCastParcelable) {
        if (receiveBroadCastParcelable.itemType.equals("pick_up")) {
            if (bookingDataParcelable.requestType.equals("transport")) {
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver3) + " " + to_detail);
                }
            } else if (bookingDataParcelable.requestType.equals("courier")) {
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier2) + " " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver_courier2) + " " + to_detail);
                }
            } else if (bookingDataParcelable.requestType.equals("food")) {
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver) + " at " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver) + " at " + to_detail);
                }
            } else if (bookingDataParcelable.requestType.equals("mart")) {
                cancellayout.setVisibility(View.GONE);
                lat_distance = lat_to;
                lng_distance = lng_to;
                if (to_place != null) {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver) + " at " + to_place);
                } else {
                    tv_driver_go_to_location.setText(getResources().getString(R.string.info_driver) + " at " + to_detail);
                }
            } else if (receiveBroadCastParcelable.itemType.equals("change_food_price")) {
                ((BookingFoodFragment) fragment).setPriceChangesData(String.valueOf(receiveBroadCastParcelable.changePriceData.oldPrice), receiveBroadCastParcelable.changePriceData.newPrice,
                        receiveBroadCastParcelable.changePriceData.receiptImagePath, receiveBroadCastParcelable.changePriceData.cashPaid, receiveBroadCastParcelable.changePriceData.depositPaid);
            } else if (receiveBroadCastParcelable.itemType.equals("change_mart_price")) {
                ((BookingMarketFragment) fragment).setItemPriceChangesData(String.valueOf(receiveBroadCastParcelable.changePriceData.oldPrice), receiveBroadCastParcelable.changePriceData.newPrice,
                        receiveBroadCastParcelable.changePriceData.receiptImagePath, receiveBroadCastParcelable.changePriceData.cashPaid, receiveBroadCastParcelable.changePriceData.depositPaid);
            }
        }
    }

}