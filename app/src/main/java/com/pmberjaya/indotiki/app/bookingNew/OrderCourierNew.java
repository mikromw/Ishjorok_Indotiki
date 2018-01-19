package com.pmberjaya.indotiki.app.bookingNew;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pmberjaya.indotiki.app.deposit.DepositTopUpList;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.account.login.LoginActivity;
import com.pmberjaya.indotiki.app.bookingNew.place.PlaceSelectionTab;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.app.promo.PromoDetail;
import com.pmberjaya.indotiki.app.promo.PromoOrderActivity;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.TimeFeeCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.UploadPhotoCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.interfaces.bookingData.DriverPositionInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.TimeFeeInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.UploadImageInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface;
import com.pmberjaya.indotiki.interfaces.misc.GPSTrackerInterface;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.models.bookingNew.DistanceData;
import com.pmberjaya.indotiki.models.bookingNew.DriverPositionListData.DriverPositionListData;
import com.pmberjaya.indotiki.models.bookingNew.DriverPositionListData.DriverPositionListModel;
import com.pmberjaya.indotiki.models.bookingNew.PriceCashData;
import com.pmberjaya.indotiki.models.bookingNew.PriceDepositData;
import com.pmberjaya.indotiki.models.bookingNew.PriceEstimateData;
import com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData.Result;
import com.pmberjaya.indotiki.models.bookingNew.PaymentData;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.PromoCodeDataParcelable;
import com.pmberjaya.indotiki.services.TimeService;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.utilities.GPSTracker;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.app.others.StandardImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


public class OrderCourierNew extends BaseActivity implements OnMapReadyCallback, ProgressRequestBody.UploadCallbacks, UploadImageInterface , GPSTrackerInterface {
    private static final int SELECT_FROM_PLACE = 1;
    private static final int SELECT_TO_PLACE = 2;
    private BookingDataParcelable bookingDataParcelable;
    private LinearLayout layoutFrom;
    private LinearLayout layoutTo;
    private TextView tvFrom;
    private TextView tvTo;
    private String state;
    protected Toolbar toolbar;
    private ProgressDialog pDialog;
    GoogleMap googleMap;
    private LinearLayout layoutLoading;
    DBController dbController;
    Marker markerfrom;
    Marker markerto;
    private boolean runGetPositionDriverList = false;

    float zoom = 14;
    private TextView tvPrice;
    private SessionManager session;
    private LocationSessionManager locationSession;
    private String requestType = Constants.COURIER;
    private ViewSwitcher viewSwitcherFormOrMap;
    private static final int RESULT_LOAD_IMAGE_REQUEST_CODE = 3;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 4;
    private static final int BOOK_COURIER = 5;
    private static final int REQUEST_CODE_CONTACT_FROM= 6;
    private static final int REQUEST_CODE_CONTACT_TO = 7;
    private static final int TIP_FOR_RIDER = 8;
    private CharSequence[] itemoption;
    private ImageView ivItemPhoto;
    ProgressDialog progressBarNya;
    public static final int progress_bar_type = 0;
    private ArrayList<String> phoneNumberReceiverArray;
    private String receiverNameData;
    private EditText tvFullnameReceiver;
    private EditText edPhoneReceiver;
    private RelativeLayout layoutContactPhoneSender;
    private RelativeLayout layoutContactPhoneReceiver;
    private Spinner spPhoneReceiver;

    private EditText edItemDelivered;

    private EditText edLocationDetailReceiver;
    private String userName;
    private String userPhone;
    private LinearLayout btnUploadItemPhoto;
    private LinearLayout btnAddFromContactsReceiver;
    private ImageView ivCloseSpPhoneSender;
    private ImageView ivCloseSpPhoneReceiver;
    private String text_sp_phone_to;
    private TextView tvFullnameSender;
    private TextView edPhoneSender;
    private ArrayList<String> phoneNumberSenderArray;
    private Spinner spPhoneSender;
    private String text_sp_phone_from;

    private String senderNameData;
    private LinearLayout btnAddMyAccountSender;
    private LinearLayout btnAddMyAccountReceiver;
    private EditText edLocationDetailSender;
    private LinearLayout btnAddFromContactsSender;
    private File file;
    private CoordinatorLayout layoutCoordinator;
    private Menu orderCourierNewMenu;
    private BookingController bookingController;
    private String term_km;
    private UtilityController utilityController;
    private String depositData;

    private ImageView icSuccessPromo;
    private TextView tvPayWithDeposit;
    private RelativeLayout layoutPayWithDeposit;
    private LinearLayout layoutPromo;
    private LinearLayout layoutTip;
    private TextView tvPromo;
    private PaymentAdapter paymentAdapter;
    private LinearLayout btnOrder;
    private LinearLayout footer;
    boolean isDefaultPromo = false;
    private TextView tvPaymentOutput;
    private TextView tvTotalPrice;
    private TextView tvPriceAfterPromo;
    private int SELECT_PROMO = 10;
    private PromoCodeDataParcelable promoCodeDataParcelable;
    private ImageView icPromo;
    private TextView tvDistance;
    private ProgressBar progressBarPrice;
    private TextView tvTip;
    private TextView tvEstimateCashPrice;
    private TextView tvEstimateInkiPayPrice;
    private LinearLayout layoutCashMethod;
    private LinearLayout layoutInkiPayMethod;
    private ImageView ivCashMethod;
    private ImageView ivInkiPayMethod;
    private ObjectAnimator blinkAnimObject;

    private TextView tvFullnameSenderValid;
    private TextView tvPhoneReceiverValid;
    private TextView tvFullnameReceiverValid;
    private TextView tvPhoneSenderValid;
    private TextView tvItemDeliveredValid;
    private TextView tvOrder;

    /************************************************************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_courier_new);
        initObject();
        initDB();
        renderView();
        initSession();
        init();
        GPSTracker.getInstance().init(OrderCourierNew.this,this);
    }

    private void initObject() {
        bookingDataParcelable = new BookingDataParcelable();
        bookingDataParcelable.requestType = Constants.COURIER;
        bookingController = BookingController.getInstance(this);
        utilityController = UtilityController.getInstance(OrderCourierNew.this);
        bookingController.setUploadImageInterface(this);
        itemoption = new CharSequence[]{getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
    }

    @Override
    public void onLocationReceived(Location location) {
        bookingDataParcelable.latUser = location.getLatitude();
        bookingDataParcelable.lngUser = location.getLongitude();
        initializeMap();
    }

    @Override
    public void onLocationError(String str) { HashMap<String, String> LatLng = locationSession.getLatLng();
        String lat = LatLng.get(locationSession.KEY_LATITUDE);
        String lng = LatLng.get(locationSession.KEY_LONGITUDE);
        bookingDataParcelable.latUser = Double.parseDouble(lat);
        bookingDataParcelable.lngUser = Double.parseDouble(lng);
        initializeMap();
    }


    private void initDB() {
        dbController = DBController.getInstance(OrderCourierNew.this);
        dbController.openDataBase();
    }

    private void initSession(){
        session = new SessionManager(this);
        locationSession = new LocationSessionManager(this);
        state = locationSession.getUserState();
        HashMap<String,String> mapData = session.getUserDetails();
        userName = mapData.get(session.KEY_NAMA);
        depositData = mapData.get(session.KEY_DEPOSIT);
        userPhone = mapData.get(session.KEY_PHONE);
        if(session.isLogin()) {
            tvOrder.setText(getResources().getString(R.string.order));
        }else{
            tvOrder.setText(getResources().getString(R.string.not_login)+", "+getResources().getString(R.string.click_here));
        }
    }

    public void initBookButton(){
        progressBarPrice.setVisibility(View.GONE);
        btnOrder.setBackgroundColor(Utility.getColor(getResources(), R.color.colorPrimaryDark, null));
        btnOrder.setOnClickListener(bookHandle);
    }

    public void disabledBookButton(){
        btnOrder.setBackgroundColor(Utility.getColor(getResources(), R.color.grey_500, null));
        btnOrder.setOnClickListener(null);
    }


    public List<PaymentData> getPaymentArray(){
        List<PaymentData> paymentDatas = new ArrayList<>();
        PaymentData paymentData = new PaymentData("1", getResources().getString(R.string.cash));
        paymentDatas.add(paymentData);
        paymentData = new PaymentData("3", getResources().getString(R.string.deposit));
        paymentDatas.add(paymentData);
        return paymentDatas;
    }


    private void init(){
        layoutFrom.setOnClickListener(selectFromPlace);
        layoutTo.setOnClickListener(selectToPlace);
        btnUploadItemPhoto.setOnClickListener(uploadPhotoNew);
        btnAddFromContactsReceiver.setOnClickListener(selectContactTo);
        btnAddFromContactsSender.setOnClickListener(selectContactFrom);
        ivCloseSpPhoneReceiver.setOnClickListener(deleteContactTo);
        ivCloseSpPhoneSender.setOnClickListener(deleteContactFrom);
        btnAddMyAccountSender.setOnClickListener(addMyNumberSender);
        btnAddMyAccountReceiver.setOnClickListener(addMyNumberReceiver);
        ivItemPhoto.setOnClickListener(intentPhotoDetail);
        layoutPromo.setOnClickListener(promoClickListener);
        layoutTip.setOnClickListener(intentTipRiderActivity);
        layoutCashMethod.setOnClickListener(cashMethod);
        layoutInkiPayMethod.setOnClickListener(inkiPayMethod);
        blinkAnimObject = Utility.getInstance().manageBlinkEffect(layoutPromo, Utility.getColor(getResources(), R.color.amber_100, null));
    }

    private OnClickListener intentTipRiderActivity = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(OrderCourierNew.this, TipRiderActivity.class);
            i.putExtra(Constants.INTENT_TIP_RIDER, bookingDataParcelable.tip);
            startActivityForResult(i, TIP_FOR_RIDER);
        }
    };


    private void renderView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolbar(toolbar, getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.indo_courier), null);
        ivCloseSpPhoneSender = (ImageView) findViewById(R.id.ivCloseSpPhoneSender);
        ivCloseSpPhoneReceiver = (ImageView) findViewById(R.id.ivCloseSpPhoneReceiver);
        btnAddFromContactsReceiver = (LinearLayout) findViewById(R.id.btnAddFromContactsReceiver);
        btnAddFromContactsSender = (LinearLayout) findViewById(R.id.btnAddFromContactsSender);
        btnUploadItemPhoto = (LinearLayout) findViewById(R.id.btnUploadItemPhoto);
        viewSwitcherFormOrMap = (ViewSwitcher) findViewById(R.id.viewSwitcherFormOrMap);
        layoutLoading = (LinearLayout) findViewById(R.id.layoutLoading);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvFrom.setSelected(true);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvTo.setSelected(true);
        progressBarPrice = (ProgressBar) findViewById(R.id.progressBarPrice);
        layoutFrom = (LinearLayout) findViewById(R.id.layoutFrom);
        layoutTo = (LinearLayout) findViewById(R.id.layoutTo);
        footer = (LinearLayout) findViewById(R.id.footer);
        ivItemPhoto = (ImageView) findViewById(R.id.ivItemPhoto);
        tvFullnameSender = (EditText) findViewById(R.id.tvFullnameSender);
        tvFullnameReceiver = (EditText) findViewById(R.id.tvFullnameReceiver);
        layoutContactPhoneSender = (RelativeLayout) findViewById(R.id.layoutContactPhoneSender);
        layoutContactPhoneReceiver = (RelativeLayout) findViewById(R.id.layoutContactPhoneReceiver);
        spPhoneReceiver = (Spinner) findViewById(R.id.spPhoneReceiver);
        spPhoneSender = (Spinner) findViewById(R.id.spPhoneSender);
        tvFullnameReceiverValid = (TextView) findViewById(R.id.tvFullnameReceiverValid);
        tvPhoneReceiverValid = (TextView) findViewById(R.id.tvPhoneReceiverValid);
        edItemDelivered = (EditText) findViewById(R.id.edItemDelivered);
        tvItemDeliveredValid = (TextView) findViewById(R.id.tvItemDeliveredValid);
        edLocationDetailSender = (EditText) findViewById(R.id.edLocationDetailSender);
        edLocationDetailReceiver = (EditText) findViewById(R.id.edLocationDetailReceiver);
        edPhoneSender = (EditText) findViewById(R.id.edPhoneSender);
        edPhoneReceiver = (EditText) findViewById(R.id.edPhoneReceiver);

        tvFullnameSenderValid = (TextView) findViewById(R.id.tvFullnameSenderValid);
        tvPhoneSenderValid = (TextView) findViewById(R.id.tvPhoneSenderValid);
        btnAddMyAccountSender = (LinearLayout) findViewById(R.id.btnAddMyAccountSender);
        btnAddMyAccountReceiver = (LinearLayout) findViewById(R.id.btnAddMyAccountReceiver);

        layoutTip = (LinearLayout) findViewById(R.id.layoutTip);
        tvTip = (TextView)findViewById(R.id.tvTip);
//        oldColors = tvTip.getTextColors(); //save original colors

        tvPrice = (TextView)findViewById(R.id.tvPrice);
        layoutPayWithDeposit = (RelativeLayout)findViewById(R.id.layoutPayWithDeposit);
        tvPayWithDeposit = (TextView)findViewById(R.id.tvPayWithDeposit);
        btnOrder =(LinearLayout)findViewById(R.id.btnOrder);
        tvPaymentOutput = (TextView) findViewById(R.id.tvPaymentOutput);
        tvTotalPrice = (TextView)findViewById(R.id.tvTotalPrice);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
        icPromo = (ImageView)findViewById(R.id.icPromo);
        tvPriceAfterPromo= (TextView) findViewById(R.id.tvPriceAfterPromo);

        layoutCoordinator = (CoordinatorLayout) findViewById(R.id.layoutCoordinator);
        viewSwitcherFormOrMap.setVisibility(View.VISIBLE);
        layoutPromo = (LinearLayout) findViewById(R.id.layoutPromo);
        tvPromo = (TextView) findViewById(R.id.tvPromo);
        icSuccessPromo = (ImageView) findViewById(R.id.icSuccessPromo);
        tvEstimateCashPrice = (TextView) findViewById(R.id.tvEstimateCashPrice);
        tvEstimateInkiPayPrice = (TextView) findViewById(R.id.tvEstimateInkiPayPrice);
        tvOrder = (TextView) findViewById(R.id.tvOrder);

        layoutCashMethod = (LinearLayout) findViewById(R.id.layoutCashMethod);
        layoutInkiPayMethod = (LinearLayout) findViewById(R.id.layoutInkiPayMethod);
        ivCashMethod = (ImageView) findViewById(R.id.ivCashMethod);
        ivInkiPayMethod = (ImageView) findViewById(R.id.ivInkiPayMethod);
    }

    private String error_type;
    public OnClickListener addMyNumberSender = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(session.isLogin()) {
                tvFullnameSender.setText(userName);
                edPhoneSender.setText(userPhone);
                layoutContactPhoneSender.setVisibility(View.GONE);
                edPhoneSender.setVisibility(View.VISIBLE);
                tvFullnameSenderValid.setVisibility(View.GONE);
                tvPhoneSenderValid.setVisibility(View.GONE);
            }else {
                Intent i = new Intent(OrderCourierNew.this, LoginActivity.class);
                startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                showToast(getResources().getString(R.string.sorry_must_login));
            }
        }
    };

    public OnClickListener addMyNumberReceiver= new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(session.isLogin()) {
                tvFullnameReceiver.setText(userName);
                edPhoneReceiver.setText(userPhone);
                layoutContactPhoneReceiver.setVisibility(View.GONE);
                edPhoneReceiver.setVisibility(View.VISIBLE);
                tvFullnameReceiverValid.setVisibility(View.GONE);
                tvPhoneReceiverValid.setVisibility(View.GONE);
            }else{
                Intent i = new Intent(OrderCourierNew.this, LoginActivity.class);
                startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                showToast(getResources().getString(R.string.sorry_must_login));
            }
        }
    };

    private void tryAgainSnackbarListener(){
        renderPriceLoadingView();
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(error_type) && error_type.equals("fee")) {
            getTimeFee();
        }else if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(error_type) && error_type.equals("getcurrentaddress")) {
            getCurrentAddress(bookingDataParcelable.latUser, bookingDataParcelable.lngUser);
        }
    }

    Snackbar indotikiOjekSnackbar;


    public OnClickListener tryAgainSnackbar = new OnClickListener() {
        @Override
        public void onClick(View view) {
            tryAgainSnackbarListener();
            indotikiOjekSnackbar.dismiss();
        }
    };

    private OnClickListener promoClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(bookingDataParcelable.distanceValue>0&&bookingDataParcelable.distanceValue<= 25&&bookingDataParcelable.price!=null){
                if(promoCodeDataParcelable!=null&&!isDefaultPromo) {
                    Intent i = new Intent(Constants.USE_PROMO_LATER);
                    i.setClass(OrderCourierNew.this, PromoDetail.class);
                    i.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
                    startActivityForResult(i, SELECT_PROMO);
                }else{
                    Intent i = new Intent(OrderCourierNew.this, PromoOrderActivity.class);
                    i.putExtra(Constants.REQUEST_TYPE, Constants.TRANSPORT);
                    startActivityForResult(i, SELECT_PROMO);
                }
            }else{
                Toast.makeText(OrderCourierNew.this, getResources().getString(R.string.complete_place), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void viewSwitchNext(){
        viewSwitcherFormOrMap.setInAnimation(AnimationUtils.loadAnimation(OrderCourierNew.this, R.anim.enter_from_right));
        viewSwitcherFormOrMap.setOutAnimation(AnimationUtils.loadAnimation(OrderCourierNew.this, R.anim.exit_to_left));
        viewSwitcherFormOrMap.showNext();
    }
    private void viewSwitchPrevious(){
        viewSwitcherFormOrMap.setInAnimation(AnimationUtils.loadAnimation(OrderCourierNew.this, R.anim.enter_from_left));
        viewSwitcherFormOrMap.setOutAnimation(AnimationUtils.loadAnimation(OrderCourierNew.this, R.anim.exit_to_right));
        viewSwitcherFormOrMap.showPrevious();
    }
    private OnClickListener bookHandle = new OnClickListener(){
        @Override
        public void onClick(View v) {
            if(session.isLogin()) {
                if (!tvFrom.getText().equals(getResources().getString(R.string.FROM)) && !tvTo.getText().equals(getResources().getString(R.string.TO))) {
                    checkValidation();
                }
            }else {
                Intent i = new Intent(OrderCourierNew.this, LoginActivity.class);
                startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                showToast(getResources().getString(R.string.please_login));
            }
        }
    };

    private OnClickListener selectToPlace = new OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(OrderCourierNew.this, PlaceSelectionTab.class);
            i.putExtra(Constants.LATITUDE, bookingDataParcelable.latUser);
            i.putExtra(Constants.LONGITUDE, bookingDataParcelable.lngUser);
            i.putExtra(Constants.TRANSPORTATION_TYPE, bookingDataParcelable.transportation);
            i.putExtra(Constants.STATE, state);
            i.putExtra(Constants.PLACE_TYPE, "to");
            i.putExtra(Constants.AUTOLOAD, "false");
            i.putExtra(Constants.REQUEST_TYPE, Constants.COURIER);
            startActivityForResult(i, SELECT_TO_PLACE);
        }
    };
    private OnClickListener selectFromPlace = new OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(OrderCourierNew.this, PlaceSelectionTab.class);
            i.putExtra(Constants.LATITUDE, bookingDataParcelable.latUser);
            i.putExtra(Constants.LONGITUDE, bookingDataParcelable.lngUser);
            i.putExtra(Constants.TRANSPORTATION_TYPE, bookingDataParcelable.transportation);
            i.putExtra(Constants.STATE, state);
            i.putExtra(Constants.PLACE_TYPE, "from");
            i.putExtra(Constants.AUTOLOAD, "true");
            i.putExtra(Constants.REQUEST_TYPE, Constants.COURIER);
            startActivityForResult(i, SELECT_FROM_PLACE);
        }
    };
    private String imagepath;
    private String imageName;

    private OnClickListener intentPhotoDetail = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(bookingDataParcelable.bookingCourierDatas.item_photo_small!=null){
                Intent intent = new Intent(OrderCourierNew.this,StandardImageItem.class);
                intent.putExtra("item", bookingDataParcelable.bookingCourierDatas.item_photo_small);
                intent.putExtra("activity","courier_confirm");
                startActivity(intent);
            }
        }
    };
    int checkitemoption=-1;
    private AlertDialog adialog;
    private Uri selectedImageUri;
    private OnClickListener uploadPhotoNew = new OnClickListener() {
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderCourierNew.this);
            builder.setTitle(R.string.option_get_picture);
            builder.setSingleChoiceItems(itemoption,checkitemoption, new DialogInterface.OnClickListener() {
                // indexSelected contains the index of item (of which checkbox checked)

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(getResources().getString(R.string.gallery).equals(itemoption[which]))
                    {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE_REQUEST_CODE);
                        Log.d("intent", ""+i);
                    }
                    else if(getResources().getString(R.string.camera).equals(itemoption[which]))
                    {
                        String fileName = "new-photo-name.jpg";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
                        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                    dialog.dismiss();
                }
            });
            adialog = builder.create();//AlertDialog dialog; create like this outside onClick
            adialog.show();
        }
    };
    public void getIntentExtra() {
        Intent intent = getIntent();
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(intent.getAction())) {
            bookingDataParcelable = intent.getParcelableExtra(Constants.BOOKING_DATA_PARCELABLE);
            bookingDataParcelable.district = locationSession.getUserDistrictIdCentral();
            if (bookingDataParcelable.transportation == null) {
                bookingDataParcelable.transportation = Constants.TRANSPORT_MOTORCYCLE;
            }
            if (bookingDataParcelable.from != null && bookingDataParcelable.fromPlace != null) {
                tvFrom.setText(bookingDataParcelable.fromPlace+", "+bookingDataParcelable.from);

            } else if (bookingDataParcelable.from != null && bookingDataParcelable.fromPlace == null) {
                tvFrom.setText(bookingDataParcelable.from);
            }
            if (bookingDataParcelable.to != null && bookingDataParcelable.toPlace != null) {
                tvTo.setText(bookingDataParcelable.toPlace+", "+bookingDataParcelable.to);

            } else if (bookingDataParcelable.to != null && bookingDataParcelable.toPlace == null) {
                tvTo.setText(bookingDataParcelable.to);
            }

            if (bookingDataParcelable.bookingCourierDatas.location_detail_sender != null) {
                edLocationDetailSender.setText(bookingDataParcelable.bookingCourierDatas.location_detail_sender);
            }
            if (bookingDataParcelable.bookingCourierDatas.location_detail_receiver != null) {
                edLocationDetailReceiver.setText(bookingDataParcelable.bookingCourierDatas.location_detail_receiver);
            }
            if (bookingDataParcelable.bookingCourierDatas.name_sender != null) {
                tvFullnameSender.setText(bookingDataParcelable.bookingCourierDatas.name_sender);
            }
            if (bookingDataParcelable.bookingCourierDatas.phone_sender != null) {
                edPhoneSender.setText(bookingDataParcelable.bookingCourierDatas.phone_sender);
            }
            if (bookingDataParcelable.bookingCourierDatas.name_receiver != null) {
                tvFullnameReceiver.setText(bookingDataParcelable.bookingCourierDatas.name_receiver);
            }
            if (bookingDataParcelable.bookingCourierDatas.phone_receiver != null) {
                edPhoneReceiver.setText(bookingDataParcelable.bookingCourierDatas.phone_receiver);
            }
            if (bookingDataParcelable.bookingCourierDatas.item != null) {
                edItemDelivered.setText(bookingDataParcelable.bookingCourierDatas.item);
            }
            footer.setVisibility(View.VISIBLE);
            bookingDataParcelable.distanceValue = Double.parseDouble(bookingDataParcelable.distance.substring(0, bookingDataParcelable.distance.length() - 2)) * 1000;
            setMarkerFrom(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom);
            setMarkerTo(bookingDataParcelable.latTo, bookingDataParcelable.lngTo);
            centerIncidentRouteOnMap(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom, bookingDataParcelable.latTo, bookingDataParcelable.lngTo);
            if(canUseDeposit(depositData)){
                bookingDataParcelable.payment = "3";
                getTimeFee();
                ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
                ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
            }else{
                indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.not_enough_inki_pay, Utility.getInstance().convertPrice(depositData)),
                        getResources().getString(R.string.top_up), topUpInkiPaySnackbar, Snackbar.LENGTH_LONG);
                bookingDataParcelable.payment = "1";
                ivInkiPayMethod.setImageResource(R.drawable.xml_border_circle_grey);
                ivCashMethod.setImageResource(R.mipmap.ic_success);
            }
            checkFeeParamsCompleteness(bookingDataParcelable);
            initBookButton();
            layoutLoading.setVisibility(View.GONE);
        }
        else{
            footer.setVisibility(View.GONE);
            bookingDataParcelable.district = locationSession.getUserDistrictIdCentral();
            bookingDataParcelable.transportation = Constants.TRANSPORT_MOTORCYCLE;
            getCurrentAddress(bookingDataParcelable.latUser, bookingDataParcelable.lngUser);
        }
    }
    public void centerIncidentRouteOnMap(double latitude_from, double longitude_from, double latitude_to, double longitude_to) {
        double minLat = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLon = Integer.MIN_VALUE;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        maxLat = Math.max(latitude_to, maxLat);
        minLat = Math.min(latitude_from, minLat);
        maxLon = Math.max(longitude_to, maxLon);
        minLon = Math.min(longitude_from, minLon);
        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 120));
    }


    public void initializeMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getIntentExtra();
        layoutLoading.setVisibility(View.GONE);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bookingDataParcelable.latFrom,
                bookingDataParcelable.lngFrom), 13.0f));
    }

    public void getCurrentAddress(double latitude_from, double longitude_from)
    {
        String origin = latitude_from+","+longitude_from;
        utilityController.getcurrentaddress(utilityController.geocoderParams(origin),currentAddressInterface);
        return;
    }
    private GeocoderLocationInterface currentAddressInterface = new GeocoderLocationInterface() {
        @Override
        public void onSuccessGetGeocoderLocation(GeocoderLocationGMapsCallback geocoderLocationGMapsCallback) {
            String status= geocoderLocationGMapsCallback.getStatus();
            if (status.equals("OK")) {
                List<Result> result =  geocoderLocationGMapsCallback.getResults();
                if(result.size()!=0) {
                    bookingDataParcelable.from = result.get(0).getFormattedAddress();
                    bookingDataParcelable.latFrom = bookingDataParcelable.latUser;
                    bookingDataParcelable.lngFrom = bookingDataParcelable.lngUser;
                    setOrigin(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom, null, bookingDataParcelable.from);
                    checkFeeParamsCompleteness(bookingDataParcelable);
                    layoutLoading.setVisibility(View.GONE);
                }else{
                    error_type = "getcurrentaddress";
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.cannot_determine),
                            getResources().getString(R.string.try_refresh), tryAgainSnackbar, Snackbar.LENGTH_INDEFINITE);
                }
            } else {
                error_type = "getcurrentaddress";
                indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.cannot_determine),
                        getResources().getString(R.string.try_refresh), tryAgainSnackbar, Snackbar.LENGTH_INDEFINITE);
            }
        }

        @Override
        public void onErrorGetGeocoderLocation(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(OrderCourierNew.this, getResources().getString(R.string.relogin));
                } else {
                    error_type = "getcurrentaddress";
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.cannot_determine),
                            getResources().getString(R.string.try_refresh), tryAgainSnackbar, Snackbar.LENGTH_INDEFINITE);
                }
            }
        }
    };
    private void renderPriceLoadingView() {
        tvPrice.setText(getResources().getString(R.string.calculate));
        tvPrice.setPaintFlags(tvPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        tvPriceAfterPromo.setVisibility(View.GONE);
        progressBarPrice.setVisibility(View.VISIBLE);
    }

    private OnClickListener selectContactFrom = new OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_CONTACT_FROM);
        }

    };
    private OnClickListener deleteContactFrom = new OnClickListener(){

        @Override
        public void onClick(View v) {
            edPhoneSender.setVisibility(View.VISIBLE);
            layoutContactPhoneSender.setVisibility(View.GONE);
        }

    };
    private OnClickListener deleteContactTo = new OnClickListener(){

        @Override
        public void onClick(View v) {
            edPhoneReceiver.setVisibility(View.VISIBLE);
            layoutContactPhoneReceiver.setVisibility(View.GONE);
        }

    };
    private OnClickListener selectContactTo = new OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_CONTACT_TO);
        }

    };
    Call<TimeFeeCallback> call;
    public void getTimeFee()
    {
        if(call!=null){
            call.cancel();
        }
        disabledBookButton();
        String api = Utility.getInstance().getTokenApi(OrderCourierNew.this);
        Map<String,String> timeFeeParams = bookingController.timeFeeParams(bookingDataParcelable);
        bookingController.getTimeFee(timeFeeParams, api,timeFeeInterface);
        return;
    }

    public void setBookingDataParcelableData(TimeFeeCallback timeFeeCallback){
        depositData = timeFeeCallback.getBalance();
        term_km = timeFeeCallback.getTerm_km();
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(term_km)) {
            term_km = "(" + timeFeeCallback.getTerm_km() + " km )";
        }else{
            term_km ="";
        }
        isDefaultPromo = timeFeeCallback.isDefault_promo();
        bookingDataParcelable.depositPaid = timeFeeCallback.getDeposit_paid();
        bookingDataParcelable.cashPaid= timeFeeCallback.getCash_paid();
        bookingDataParcelable.price = timeFeeCallback.getPrice();
        bookingDataParcelable.originalPrice = timeFeeCallback.getOriginal_price();
        bookingDataParcelable.promoPrice = timeFeeCallback.getPromo_price();
        bookingDataParcelable.categoryVoucher = timeFeeCallback.getPromo_category_voucher();
        DistanceData distanceData = timeFeeCallback.getDistance();
        bookingDataParcelable.distanceValue = Utility.getInstance().parseDecimal(distanceData.getValue());
        bookingDataParcelable.distance = distanceData.getText();
    }

    TimeFeeInterface timeFeeInterface = new TimeFeeInterface() {
        @Override
        public void onSuccessGetTimeFee(TimeFeeCallback timeFeeCallback) {
            int sukses = timeFeeCallback.getSukses();
            if(sukses==2){
                setBookingDataParcelableData(timeFeeCallback);
                if (bookingDataParcelable.distanceValue <= 0.10) {
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.location_valid),
                            getResources().getString(R.string.ok), null, Snackbar.LENGTH_INDEFINITE);
                    disabledBookButton();
                    return;
                }
                tvDistance.setText("( " + bookingDataParcelable.distance + " )");
                setPromoCodeAndPrice(timeFeeCallback);
                setEstimatePrice(timeFeeCallback);
                setDirectionRoute(timeFeeCallback);
                setPaymentInfo();
                initBookButton();
            }
            else if (sukses == 4) {
                Utility.getInstance().showSimpleAlertDialog(OrderCourierNew.this,null, timeFeeCallback.getPesan(),
                        getResources().getString(R.string.ok),dismissTipRiderListener,
                        null,null,
                        false);
            }else {
                indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, timeFeeCallback.getPesan(),
                        getResources().getString(R.string.close), null, Snackbar.LENGTH_INDEFINITE);
                disabledBookButton();
            }
        }

        @Override
        public void onErrorGetTimefee(APIErrorCallback apiErrorCallback) {
            error_type = "getTimeFee";
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(OrderCourierNew.this, getResources().getString(R.string.relogin));
                } else {
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.cannot_determine),
                            getResources().getString(R.string.try_refresh), tryAgainSnackbar, Snackbar.LENGTH_INDEFINITE);
                }
            }
        }
        @Override
        public void callCancel(Call<TimeFeeCallback> timeFeeCallbackCall) {
            call = timeFeeCallbackCall;
        }
    };
    private void setEstimatePrice(TimeFeeCallback timeFeeCallback) {
        PriceEstimateData priceEstimateData = timeFeeCallback.getPrice_est();
        PriceCashData priceCashData = priceEstimateData.getCash();
        PriceDepositData priceDepositData = priceEstimateData.getDeposit();

        String cashEstimate = "Rp. " +Utility.getInstance().convertPrice(priceCashData.getPrice());
        String inkiPayEstimate = "Rp. " +Utility.getInstance().convertPrice(priceDepositData.getPrice());
        tvEstimateCashPrice.setText(cashEstimate);
        tvEstimateInkiPayPrice.setText(inkiPayEstimate);
    }

   
    private void deleteCodePromo(){
        promoCodeDataParcelable = null;
        tvPrice.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
        icSuccessPromo.setVisibility(View.GONE);
        icPromo.setVisibility(View.VISIBLE);
        bookingDataParcelable.promoCode = "";
        tvPromo.setText(getResources().getString(R.string.promo));
        tvPromo.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
    }
    private DialogInterface.OnClickListener dismissTipRiderListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            tvTip.setText(getResources().getString(R.string.tip_for_rider));
            tvTip.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
            bookingDataParcelable.tip = null;
            getTimeFee();
        }
    };
    private void setPromoCodeAndPrice(TimeFeeCallback timeFeeCallback) {
        if (timeFeeCallback.getKode_promo()!=null) {
            String priceText = "";
            if (bookingDataParcelable.price == null || bookingDataParcelable.price.equals("0")) {
                priceText = getResources().getString(R.string.free);
            } else {
                priceText = "Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price);
            }
            if(!Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.promoCode)||!timeFeeCallback.getKode_promo().equals(bookingDataParcelable.promoCode)) {
                Utility.getInstance().showSimpleAlertDialog(OrderCourierNew.this, getResources().getString(R.string.promo_code_right2), timeFeeCallback.getPesan() +
                                "\nHarga awal: Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.originalPrice) + "\nHarga akhir: " + priceText + " " + term_km,
                        "OK", positivePromoDialogListener,
                        null, null, false);
            }

            bookingDataParcelable.promoCode = timeFeeCallback.getKode_promo();
            tvPromo.setVisibility(View.VISIBLE);
            icSuccessPromo.setVisibility(View.VISIBLE);
            icPromo.setVisibility(View.GONE);
            tvPromo.setText(bookingDataParcelable.promoCode);
            tvPromo.setTextColor(Utility.getColor(getResources(),R.color.black,null));

            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrice.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.originalPrice));
            tvPriceAfterPromo.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
            tvPriceAfterPromo.setVisibility(View.VISIBLE);
        }else {
            if(timeFeeCallback.getPesan()==null){
                deleteCodePromo();
            }else {
                showPromoErrorAlertDialog(timeFeeCallback.getPesan());
            }
        }
    }

    private void setPaymentInfo() {
        if (bookingDataParcelable.payment.equals("1")) {
            layoutPayWithDeposit.setVisibility(View.GONE);
            tvPaymentOutput.setText(getResources().getString(R.string.total_paid_in_cash));
            if (bookingDataParcelable.price == null || bookingDataParcelable.price.equals("0")) {
                tvTotalPrice.setText(getResources().getString(R.string.free));
            }else{
                tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.cashPaid));
            }
        }  else if (bookingDataParcelable.payment.equals("3")) {
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.cashPaid)&&bookingDataParcelable.cashPaid.equals("0")){
                layoutPayWithDeposit.setVisibility(View.GONE);
                tvPaymentOutput.setText(getResources().getString(R.string.total_paid_in_deposit));
                if (bookingDataParcelable.price == null || bookingDataParcelable.price.equals("0")) {
                    tvTotalPrice.setText(getResources().getString(R.string.free));
                }else{
                    tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.depositPaid));
                }
            } else{
                layoutPayWithDeposit.setVisibility(View.VISIBLE);
                tvPayWithDeposit.setText("- Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.depositPaid));
                tvPaymentOutput.setText(getResources().getString(R.string.pay_with_cash));
                if (bookingDataParcelable.price == null || bookingDataParcelable.price.equals("0")) {
                    tvTotalPrice.setText(getResources().getString(R.string.free));
                }else{
                    tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.cashPaid));
                };
            }
        }
    }
    public DialogInterface.OnClickListener positivePromoDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }

    };
    public DialogInterface.OnClickListener positivePromoDialogErrorListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            getTimeFee();
        }
    };

    private void showPromoErrorAlertDialog(String pesan){
        promoCodeDataParcelable = null;
        bookingDataParcelable.promoCode="";
        tvPromo.setText(getResources().getString(R.string.promo));
        tvPromo.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
        icPromo.setVisibility(View.VISIBLE);
        icSuccessPromo.setVisibility(View.GONE);
        Utility.getInstance().showSimpleAlertDialog(OrderCourierNew.this, getResources().getString(R.string.promo_code_wrong), pesan,
                "OK", positivePromoDialogErrorListener,
                null, null, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FROM_PLACE) {
            if(resultCode == RESULT_OK){
                String latitudefrom = data.getStringExtra(Constants.LATITUDE);
                String longitudefrom = data.getStringExtra(Constants.LONGITUDE);
                bookingDataParcelable.fromPlace = data.getStringExtra(Constants.PLACE);
                bookingDataParcelable.from = data.getStringExtra(Constants.PLACE_DETAILS);
                bookingDataParcelable.latFrom = Utility.getInstance().parseDecimal(latitudefrom);
                bookingDataParcelable.lngFrom = Utility.getInstance().parseDecimal(longitudefrom);
                setOrigin(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom,  bookingDataParcelable.fromPlace, bookingDataParcelable.from);
                checkFeeParamsCompleteness(bookingDataParcelable);
            }
            else if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else if(requestCode == SELECT_TO_PLACE) {
            if(resultCode == RESULT_OK){
                String latitudefrom = data.getStringExtra(Constants.LATITUDE);
                String longitudefrom = data.getStringExtra(Constants.LONGITUDE);
                bookingDataParcelable.toPlace = data.getStringExtra(Constants.PLACE);
                bookingDataParcelable.to = data.getStringExtra(Constants.PLACE_DETAILS);
                bookingDataParcelable.latTo= Utility.getInstance().parseDecimal(latitudefrom);
                bookingDataParcelable.lngTo= Utility.getInstance().parseDecimal(longitudefrom);
                setDestination(bookingDataParcelable.latTo, bookingDataParcelable.lngFrom,  bookingDataParcelable.toPlace, bookingDataParcelable.to);
                checkFeeParamsCompleteness(bookingDataParcelable);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else if (requestCode == RESULT_LOAD_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImageUri = data.getData();
                imagepath = Utility.getInstance().getPath(OrderCourierNew.this, selectedImageUri);
                imageName = imagepath.substring(imagepath.lastIndexOf("/"));
                file = new File(imagepath);

                if(file.exists()) {
                    long lengthbmp = file.length();
                    if (lengthbmp < 5000000) {
                        doUploadPhoto(file);
                        tvItemDeliveredValid.setVisibility(View.GONE);
                    } else {
                        tvItemDeliveredValid.setText(getResources().getString(R.string.large_file_size));
                        tvItemDeliveredValid.setVisibility(View.VISIBLE);
                    }
                }else{
                    tvItemDeliveredValid.setText(getResources().getString(R.string.file_not_exists));
                    tvItemDeliveredValid.setVisibility(View.VISIBLE);
                }
            }
        }
        else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ) {
            if (resultCode == Activity.RESULT_OK) {
                imagepath = Utility.getInstance().getPath(OrderCourierNew.this, selectedImageUri);
                imageName = imagepath.substring(imagepath.lastIndexOf("/"));
                file = new File(imagepath);
                if(file.exists()) {
                    long lengthbmp = file.length();
                    if (lengthbmp < 5000000) {
                        doUploadPhoto(file);
                        tvItemDeliveredValid.setVisibility(View.GONE);
                    } else {
                        tvItemDeliveredValid.setText(getResources().getString(R.string.large_file_size));
                        tvItemDeliveredValid.setVisibility(View.VISIBLE);
                    }
                }else{
                    tvItemDeliveredValid.setText(getResources().getString(R.string.file_not_exists));
                    tvItemDeliveredValid.setVisibility(View.VISIBLE);
                }
            }
        }else if(requestCode == BOOK_COURIER ) {
            if (resultCode == Activity.RESULT_OK) {
                if(file.exists()){
                    doUploadPhoto(file);
                }else{
                    imagepath = null;
                    imageName = null;
                    bookingDataParcelable.bookingCourierDatas.item_photo =  null;
                    bookingDataParcelable.bookingCourierDatas.item_photo_small=null;
                    tvItemDeliveredValid.setText(getResources().getString(R.string.please_upload_one_more));
                    tvItemDeliveredValid.setVisibility(View.VISIBLE);
                    ivItemPhoto.setImageResource(R.mipmap.img_no_image);
                }
            }
        }
        else if(requestCode == REQUEST_CODE_CONTACT_FROM){
            if (resultCode == Activity.RESULT_OK) {
                phoneNumberSenderArray= new ArrayList<String>();
                Uri contactData = data.getData();
                Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);
                while (cursor.moveToNext())
                {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    senderNameData = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if ( hasPhone.equalsIgnoreCase("1"))
                        hasPhone = "true";
                    else
                        hasPhone = "false" ;

                    if (Boolean.parseBoolean(hasPhone))
                    {
                        edPhoneSender.setVisibility(View.GONE);
                        layoutContactPhoneSender.setVisibility(View.VISIBLE);
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                        while (phones.moveToNext())
                        {
                            String PhoneData = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumberSenderArray.add(PhoneData);
                        }
                        phones.close();
                    }
                    tvFullnameSender.setText(senderNameData);
                    if(phoneNumberSenderArray.size()!=0){
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderCourierNew.this,
                                android.R.layout.simple_spinner_item, phoneNumberSenderArray);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter tvTo spinner
                        spPhoneSender.setAdapter(dataAdapter);
                    }
                    tvFullnameSenderValid.setVisibility(View.GONE);
                    tvPhoneSenderValid.setVisibility(View.GONE);

                }
                cursor.close();

            }
            else if (resultCode == RESULT_CANCELED) {
            }

        }
        else if(requestCode == REQUEST_CODE_CONTACT_TO){
            if (resultCode == Activity.RESULT_OK) {
                phoneNumberReceiverArray= new ArrayList<String>();

                Uri contactData = data.getData();
                Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);
                while (cursor.moveToNext())
                {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    receiverNameData = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1"))
                        hasPhone = "true";
                    else
                        hasPhone = "false" ;

                    if (Boolean.parseBoolean(hasPhone))
                    {
                        edPhoneReceiver.setVisibility(View.GONE);
                        layoutContactPhoneReceiver.setVisibility(View.VISIBLE);
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                        while (phones.moveToNext())
                        {
                            String PhoneData = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumberReceiverArray.add(PhoneData);
                        }
                        phones.close();
                    }
                    tvFullnameReceiver.setText(receiverNameData);
                    if(phoneNumberReceiverArray.size()!=0){
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderCourierNew.this,
                                android.R.layout.simple_spinner_item, phoneNumberReceiverArray);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter tvTo spinner
                        spPhoneReceiver.setAdapter(dataAdapter);
                    }
                    tvFullnameReceiverValid.setVisibility(View.GONE);
                    tvPhoneReceiverValid.setVisibility(View.GONE);
                }  //while (cursor.moveToNext())
                cursor.close();

            }
            else if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else  if (requestCode == TIP_FOR_RIDER) {
            if(resultCode == RESULT_OK){
                bookingDataParcelable.tip = data.getStringExtra("tip_rider");
                if(bookingDataParcelable.tip!=null&&bookingDataParcelable.tip.equals("0")){
                    tvTip.setText(getResources().getString(R.string.tip_for_rider));
                    tvTip.setTextColor(Utility.getColor(getResources(),R.color.grey_600, null));
                }
                else{
                    tvTip.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.tip));
                    tvTip.setTextColor(Utility.getColor(getResources(),R.color.black,null));
                }
            }
            else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == SELECT_PROMO) {
            if (resultCode == RESULT_OK) {
                promoCodeDataParcelable = data.getParcelableExtra(Constants.PROMO_CODE_PARCELABLE);
                if(promoCodeDataParcelable!=null) {
                    bookingDataParcelable.promoCode = promoCodeDataParcelable.title;
                    tvPromo.setText(bookingDataParcelable.promoCode);
                    tvPromo.setTextColor(Utility.getColor(getResources(),R.color.black,null));
                    getTimeFee();
                    if(blinkAnimObject!=null){
                        blinkAnimObject.cancel();
                        layoutPromo.setBackgroundColor(Utility.getColor(getResources(), R.color.white, null));
                    }
                }else{
                    icSuccessPromo.setVisibility(View.GONE);
                    icPromo.setVisibility(View.VISIBLE);
                    bookingDataParcelable.promoCode = "";
                    tvPromo.setText(getResources().getString(R.string.promo));
                    tvPromo.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
                    getTimeFee();
                    blinkAnimObject = Utility.getInstance().manageBlinkEffect(layoutPromo, Utility.getColor(getResources(), R.color.amber_100, null));
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        else  if (requestCode == Constants.STATE_LOGIN_CODE) {
            if(resultCode == RESULT_OK){
                initSession();
                if(canUseDeposit(depositData)){
                    bookingDataParcelable.payment = "3";
                    getTimeFee();
                    ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
                    ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
                }else{
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.not_enough_inki_pay, Utility.getInstance().convertPrice(depositData)),
                            getResources().getString(R.string.top_up), topUpInkiPaySnackbar, Snackbar.LENGTH_LONG);
                    bookingDataParcelable.payment = "1";
                    ivInkiPayMethod.setImageResource(R.drawable.xml_border_circle_grey);
                    ivCashMethod.setImageResource(R.mipmap.ic_success);
                }
            }
            else if (resultCode == RESULT_CANCELED) {
            }
        }
        switch (requestCode) {
            case GPSTracker.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        GPSTracker.getInstance().startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    public OnClickListener topUpInkiPaySnackbar = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(OrderCourierNew.this, DepositTopUpList.class);
            startActivity(intent);
        }
    };
    private OnClickListener cashMethod = new OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.payment)&&!bookingDataParcelable.payment.equals("1")) {
                bookingDataParcelable.payment = "1";
                getTimeFee();
                ivCashMethod.setImageResource(R.mipmap.ic_success);
                ivInkiPayMethod.setImageResource(R.drawable.xml_border_circle_grey);
            }
        }
    };

    private OnClickListener inkiPayMethod = new OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.payment)&&!bookingDataParcelable.payment.equals("3")) {
                if (session.isLogin()) {
                    if (canUseDeposit(depositData)) {
                        bookingDataParcelable.payment = "3";
                        getTimeFee();
                        ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
                        ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
                    } else {
                        indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.not_enough_inki_pay, Utility.getInstance().convertPrice(depositData)),
                                getResources().getString(R.string.top_up), topUpInkiPaySnackbar, Snackbar.LENGTH_LONG);
                    }
                } else {
                    Intent i = new Intent(OrderCourierNew.this, LoginActivity.class);
                    startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                    showToast(getResources().getString(R.string.please_login));
                }
            }
        }
    };
    public boolean canUseDeposit(String depositData){
        if (!Utility.getInstance().checkIfStringIsNotNullOrEmpty(depositData)||depositData.equals("0"))
            return false;
        return true;
    }
    private void setOrigin(double latitudefrom, double longitudefrom, String from_place, String from){
        if(from_place==null){
            tvFrom.setText(from);
        }
        else{
            tvFrom.setText(from_place+", "+ from);
        }
        setMarkerFrom(latitudefrom, longitudefrom);
    }

    private void setDestination(double latitudeto, double longitudeto, String to_place, String to){
        if(to_place==null){
            tvTo.setText(to);
        }
        else{
            tvTo.setText(to_place+", "+to);
        }
        setMarkerTo(latitudeto, longitudeto);
    }
    private void checkFeeParamsCompleteness(BookingDataParcelable bookingDataParcelable){
        if((Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.from)||Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.fromPlace))&&
                (Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.to)||Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.toPlace))){
            if(canUseDeposit(depositData)){
                bookingDataParcelable.payment = "3";
                ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
                ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
            }
            getTimeFee();
            footer.setVisibility(View.VISIBLE);
        }
    }

    private void setMarkerFrom(double lat_from_map , double lng_from_map ){
        if(markerfrom!=null){
            markerfrom.remove();
        }
        Log.d("lat lng", lat_from_map+" , "+lng_from_map);
        final MarkerOptions markerfromOption = new MarkerOptions().position(
                new LatLng(lat_from_map, lng_from_map)).draggable(false);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat_from_map, lng_from_map)).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        // changing marker color
        markerfromOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_from_place));//icon(BitmapDescriptorFactory.fromResource(R.drawable.schools_maps));

        runOnUiThread(new Runnable(){
            public void run() {
                markerfrom = googleMap.addMarker(markerfromOption);
            }
        });
    }
    private void setMarkerTo(double lat_to_map , double lng_to_map ){
        if(markerto!=null){
            markerto.remove();
        }
        final MarkerOptions markertoOption = new MarkerOptions().position(
                new LatLng(lat_to_map, lng_to_map)).draggable(false);

        // changing marker color
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat_to_map, lng_to_map)).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        markertoOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_to_place));//icon(BitmapDescriptorFactory.fromResource(R.drawable.schools_maps));
        runOnUiThread(new Runnable(){
            public void run() {
                markerto = googleMap.addMarker(markertoOption);
            }
        });
    }
    private Polyline line;
    private void setDirectionRoute(TimeFeeCallback timeFeeCallback) {
        if(line!=null){
            line.remove();
        }
        String polyLine = timeFeeCallback.getPolyline();
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        PolylineOptions lineOptions = new PolylineOptions();
        list.addAll(decodePoly(polyLine));
        lineOptions.addAll(list);
        lineOptions.width(6);
        lineOptions.color(Color.RED);
        line = googleMap.addPolyline(lineOptions);
        centerIncidentRouteOnMap(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom, bookingDataParcelable.latTo, bookingDataParcelable.lngTo);
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
    public void RequestCourier(){
        pDialog = new ProgressDialog(OrderCourierNew.this);
        pDialog.setMessage("Mohon Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        btnOrder.setEnabled(false);
        String api = Utility.getInstance().getTokenApi(OrderCourierNew.this);
        bookingController.requestCourier(bookingController.requestCourierParams(bookingDataParcelable), api, requestCourierInterface);
        return;
    }

    public void doUploadPhoto(File file){
        String api = Utility.getInstance().getTokenApi(OrderCourierNew.this);
        showDialog(progress_bar_type);
        String MEDIA_TYPE_PNG = "image/" + imageName.substring(imageName.lastIndexOf(".") + 1);
        ProgressRequestBody test = new ProgressRequestBody(file, MEDIA_TYPE_PNG, this);
        String filename ="file\"; filename=\""+imageName.substring(1,imageName.length())+"\" ";
        bookingController.doUploadPhotoCourier(parametersSendRequest(filename,test),api);
        return;
    }
    public Map<String, ProgressRequestBody> parametersSendRequest(String filename, ProgressRequestBody test) {
        Map<String, ProgressRequestBody> params = new HashMap<String, ProgressRequestBody>();
        params.put(filename, test);
        return  params;
    }

    BaseGenericInterface requestCourierInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> requestCourierCallback) {
            if (pDialog != null) {
                pDialog.dismiss();
            }
            btnOrder.setEnabled(true);
            String requestId = (String) requestCourierCallback.getData();
            int sukses = requestCourierCallback.getSukses();
            String pesan = requestCourierCallback.getPesan();
            progressBarPrice.setVisibility(View.GONE);
            if (sukses == 1) {
//				 layoutLoading.setVisibility(View.GONE);
                Toast.makeText(OrderCourierNew.this, pesan, Toast.LENGTH_LONG).show();
            } else if (sukses == 2) {
//				 layoutLoading.setVisibility(View.VISIBLE);
                Intent intent = new Intent(OrderCourierNew.this, SearchDriverNew.class);
                intent.putExtra("request_id", requestId);
                intent.putExtra("request_type", requestType);
                intent.putExtra("transportation", bookingDataParcelable.transportation);
                startActivityForResult(intent, BOOK_COURIER);
                SessionManager sessionManager = new SessionManager(OrderCourierNew.this);
                sessionManager.setTimerRepeatData(requestId, requestType, System.currentTimeMillis());
                Intent service = new Intent(OrderCourierNew.this, TimeService.class);
                service.putExtra("request_id", requestId);
                service.putExtra("request_type", requestType);
                startService(service);
            } else {
                progressBarPrice.setVisibility(View.GONE);
                Toast.makeText(OrderCourierNew.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            progressBarPrice.setVisibility(View.GONE);
            btnOrder.setEnabled(true);
            if (pDialog != null) {
                pDialog.dismiss();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        orderCourierNewMenu = menu;
        getMenuInflater().inflate(R.menu.order_courier_new_menu, orderCourierNewMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            orderCourierNewMenu.clear();
            getMenuInflater().inflate(R.menu.order_courier_new_menu2, orderCourierNewMenu);
            viewSwitchNext();
        }else if (id == R.id.action_list) {
            orderCourierNewMenu.clear();
            getMenuInflater().inflate(R.menu.order_courier_new_menu, orderCourierNewMenu);
            viewSwitchPrevious();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected Dialog onCreateDialog (int id){
        switch (id){
            case progress_bar_type: //we set this tvTo 0
                progressBarNya = new ProgressDialog(this);
                progressBarNya.setMessage("Uploading image...");
                progressBarNya.setIndeterminate(false);
                progressBarNya.setMax(100);
                progressBarNya.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBarNya.setCancelable(false);
                progressBarNya.show();
                return progressBarNya;
            default:
                return null;

        }
    }
    @Override
    public void onProgressUpdate(int percentage, String progressNumber) {
        // set current progress
        Log.d(" percentage", ""+percentage);
        progressBarNya.setProgress(percentage);
        progressBarNya.setProgressNumberFormat(progressNumber);

    }

    @Override
    public void onError() {
        Log.d(" onError", "onError");
        // do something on error
    }

    @Override
    public void onFinish() {
        Log.d(" onFinish", "onFinish");
        // do something on upload finished
        // for example start next uploading at queue
        progressBarNya.setProgress(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                try {
                    dismissDialog(progress_bar_type);
                } catch (Exception e2) {
                }
            }
        }, 1000);
        //
    }

    @Override
    public void onBackPressed()
    {
        if(viewSwitcherFormOrMap!=null&&viewSwitcherFormOrMap.getDisplayedChild()==1){
            orderCourierNewMenu.clear();
            getMenuInflater().inflate(R.menu.order_courier_new_menu, orderCourierNewMenu);
            viewSwitchPrevious();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccessUploadImage(UploadPhotoCallback data) {
        int sukses = data.getSukses();
        if(sukses==2){
            progressBarNya.dismiss();
            bookingDataParcelable.bookingCourierDatas.item_photo =  data.getPath();
            bookingDataParcelable.bookingCourierDatas.item_photo_small= data.getPhoto();
            if(bookingDataParcelable.bookingCourierDatas.item_photo_small!=null && !bookingDataParcelable.bookingCourierDatas.item_photo_small.equals("")) {
                PicassoLoader.loadImageFile(this,file,ivItemPhoto);
            }
            else{
                PicassoLoader.loadImageFail(this,ivItemPhoto);
            }
        }else if(sukses==3){
            bookingDataParcelable.bookingCourierDatas.item_photo =  data.getPath();
            Toast.makeText(OrderCourierNew.this, bookingDataParcelable.bookingCourierDatas.item_photo,Toast.LENGTH_SHORT).show();
        }
        onFinish();
    }

    @Override
    public void onErrorUploadImage(APIErrorCallback apiErrorCallback) {
        progressBarNya.dismiss();
        if(apiErrorCallback.getError()!=null) {
            if (apiErrorCallback.getError().equals("Invalid API key ")) {
                Log.d("Unauthorized", "Jalannn");
                SessionManager session = new SessionManager(this);
                session.logoutUser();
                Utility.getInstance().showInvalidApiKeyAlert(this, getResources().getString(R.string.relogin));
            } else {
                onFinish();
                Toast.makeText(this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void checkValidation(){
        bookingDataParcelable.bookingCourierDatas.name_sender = tvFullnameSender.getText().toString();
        bookingDataParcelable.bookingCourierDatas.name_receiver = tvFullnameReceiver.getText().toString();
        bookingDataParcelable.bookingCourierDatas.phone_sender = edPhoneSender.getText().toString();
        bookingDataParcelable.bookingCourierDatas.phone_receiver = edPhoneReceiver.getText().toString();

        if(bookingDataParcelable.bookingCourierDatas.phone_sender.length()>=2){
            if(bookingDataParcelable.bookingCourierDatas.phone_sender.substring(0,2).equals("08")){
                bookingDataParcelable.bookingCourierDatas.phone_sender =bookingDataParcelable.bookingCourierDatas.phone_sender.substring(0,2)
                        .replace("08", "+628")+bookingDataParcelable.bookingCourierDatas.phone_sender.substring(2,bookingDataParcelable.bookingCourierDatas.phone_sender.length());
                bookingDataParcelable.bookingCourierDatas.phone_sender = bookingDataParcelable.bookingCourierDatas.phone_sender.replaceAll("-|\\(|\\)|\\.|\\/|\\;|\\,", "");
            }
        }
        if(bookingDataParcelable.bookingCourierDatas.phone_receiver.length()>=2){
            if(bookingDataParcelable.bookingCourierDatas.phone_receiver.substring(0,2).equals("08")){
                bookingDataParcelable.bookingCourierDatas.phone_receiver = bookingDataParcelable.bookingCourierDatas.phone_receiver.substring(0,2)
                        .replace("08", "+628")+bookingDataParcelable.bookingCourierDatas.phone_receiver.substring(2,bookingDataParcelable.bookingCourierDatas.phone_receiver.length());
                bookingDataParcelable.bookingCourierDatas.phone_receiver = bookingDataParcelable.bookingCourierDatas.phone_receiver.replaceAll("-|\\(|\\)|\\.|\\/|\\;|\\,", "");
            }
        }
        if(phoneNumberSenderArray!=null&&phoneNumberSenderArray.size()!=0){
            text_sp_phone_from = spPhoneSender.getSelectedItem().toString();
            bookingDataParcelable.bookingCourierDatas.phone_sender = text_sp_phone_from.replaceAll("-|\\(|\\)|\\.|\\/|\\;|\\,", "");
        }
        if(phoneNumberReceiverArray!=null&&phoneNumberReceiverArray.size()!=0){
            text_sp_phone_to = spPhoneReceiver.getSelectedItem().toString();
            bookingDataParcelable.bookingCourierDatas.phone_receiver = text_sp_phone_to.replaceAll("-|\\(|\\)|\\.|\\/|\\;|\\,", "");
        }
        bookingDataParcelable.bookingCourierDatas.item= edItemDelivered.getText().toString();
        boolean text_fullname_from_isFilled = bookingDataParcelable.bookingCourierDatas.name_sender.length()!=0;
        boolean text_phone_from_min10 = bookingDataParcelable.bookingCourierDatas.phone_sender.length()>=10;
        boolean phone_from_isVisible = edPhoneSender.isShown();
        boolean text_sp_phone_from_isSelected = text_sp_phone_from!=null;
        boolean sp_phone_from_isVisible = spPhoneSender.isShown();

        boolean text_fullname_to_isFilled = bookingDataParcelable.bookingCourierDatas.name_receiver.length()!=0;
        boolean text_phone_to_min10 = bookingDataParcelable.bookingCourierDatas.phone_receiver.length()>=10;
        boolean phone_to_isVisible = edPhoneReceiver.isShown();
        boolean text_sp_phone_to_isSelected = text_sp_phone_to!=null;
        boolean sp_phone_to_isVisible = spPhoneReceiver.isShown();

        //-----------------------------------------boolean from_phone != session_phone_user-------------------------------------
        boolean text_not_same1 = phone_from_isVisible&&!bookingDataParcelable.bookingCourierDatas.phone_sender.equals(userPhone);
        boolean text_sp_not_same1 = false;
        if(text_sp_phone_from!=null){
            if(text_sp_phone_from.substring(0,2).equals("08")){
                text_sp_phone_from =text_sp_phone_from.substring(0,2).replace("08", "+628")+text_sp_phone_from.substring(2,text_sp_phone_from.length());
            }
            text_sp_not_same1 = sp_phone_from_isVisible&&!text_sp_phone_from.equals(userPhone);
        }
        boolean not_same1 = text_not_same1 || text_sp_not_same1;
        //----------------------------------------------------------------------------------------------------------------------

        //-----------------------------------------boolean to_phone != session_phone_user---------------------------------------

        boolean text_not_same2 = phone_to_isVisible&&!bookingDataParcelable.bookingCourierDatas.phone_receiver.equals(userPhone);
        boolean text_sp_not_same2= false;
        if(text_sp_phone_to!=null){
            if(text_sp_phone_to.substring(0,2).equals("08")){
                text_sp_phone_to =text_sp_phone_to.substring(0,2).replace("08", "+628")+text_sp_phone_to.substring(2,text_sp_phone_to.length());
            }
            text_sp_not_same2 = sp_phone_to_isVisible&&!text_sp_phone_to.equals(userPhone);

        }
        boolean not_same2 = text_not_same2 || text_sp_not_same2;

        //----------------------------------------------------------------------------------------------------------------------
        //-----------------------------------------boolean from_phone != to_phone-----------------------------------------------
        boolean text_sp_not_same3_spwsp=false;
        boolean text_sp_not_same3_edwsp=false;
        boolean text_sp_not_same3_spwed=false;

        boolean text_not_same3_edwed = phone_from_isVisible&&phone_to_isVisible&&!bookingDataParcelable.bookingCourierDatas.phone_sender.equals(bookingDataParcelable.bookingCourierDatas.phone_receiver);

        if(text_sp_phone_from!=null){
            if(text_sp_phone_from.substring(0,2).equals("08")){
                text_sp_phone_from =text_sp_phone_from.substring(0,2).replace("08", "+628")+text_sp_phone_from.substring(2,text_sp_phone_from.length());
            }
            text_sp_not_same1 = sp_phone_from_isVisible&&!text_sp_phone_from.equals(userPhone);
            text_sp_not_same3_spwed = sp_phone_from_isVisible&&phone_to_isVisible&&!text_sp_phone_from.equals(bookingDataParcelable.bookingCourierDatas.phone_receiver);
        }
        if(text_sp_phone_to!=null){
            if(text_sp_phone_to.substring(0,2).equals("08")){
                text_sp_phone_to =text_sp_phone_to.substring(0,2).replace("08", "+628")+text_sp_phone_to.substring(2,text_sp_phone_to.length());
            }
            text_sp_not_same2 = sp_phone_to_isVisible&&!text_sp_phone_to.equals(userPhone);
            text_sp_not_same3_edwsp = phone_from_isVisible&&sp_phone_to_isVisible&&!bookingDataParcelable.bookingCourierDatas.phone_sender.equals(text_sp_phone_to);
        }
        if(text_sp_phone_from!=null&&text_sp_phone_to!=null){
            if(text_sp_phone_from.substring(0,2).equals("08")){
                text_sp_phone_from =text_sp_phone_from.substring(0,2).replace("08", "+628")+text_sp_phone_from.substring(2,text_sp_phone_from.length());
            }
            if(text_sp_phone_to.substring(0,2).equals("08")){
                text_sp_phone_to =text_sp_phone_to.substring(0,2).replace("08", "+628")+text_sp_phone_to.substring(2,text_sp_phone_to.length());
            }
            text_sp_not_same1 = sp_phone_from_isVisible&&!text_sp_phone_from.equals(userPhone);
            text_sp_not_same3_spwsp = sp_phone_from_isVisible&&sp_phone_to_isVisible&&!text_sp_phone_from.equals(text_sp_phone_to);
        }

        boolean not_same3 = text_not_same3_edwed || text_sp_not_same3_spwsp || text_sp_not_same3_edwsp|| text_sp_not_same3_spwed;
        //---------------------------------------CHECK VALIDATION----------------------------------------------
        if(bookingDataParcelable.bookingCourierDatas.item.length()==0){
            tvItemDeliveredValid.setVisibility(View.VISIBLE);
            tvItemDeliveredValid.setText(getResources().getString(R.string.items_deliver_empty));
            edItemDelivered.clearFocus();
            edItemDelivered.requestFocus();
        }
        else{
            tvItemDeliveredValid.setVisibility(View.GONE);
        }
        if(imagepath==null || imageName == null)
        {
            tvItemDeliveredValid.setText(getResources().getString(R.string.Masukkan_Foto));
            tvItemDeliveredValid.setVisibility(View.VISIBLE);
            edItemDelivered.clearFocus();
            edItemDelivered.requestFocus();
        }
        bookingDataParcelable.bookingCourierDatas.location_detail_receiver = edLocationDetailReceiver.getText().toString();
        if(bookingDataParcelable.bookingCourierDatas.location_detail_receiver==null){
            bookingDataParcelable.bookingCourierDatas.location_detail_receiver="";
        }

        if(!((text_phone_to_min10&&phone_to_isVisible)||(text_sp_phone_to_isSelected&&sp_phone_to_isVisible))){
            tvPhoneReceiverValid.setVisibility(View.VISIBLE);
            tvPhoneReceiverValid.setText(getResources().getString(R.string.phone_to_empty));
            edPhoneReceiver.clearFocus();
            edPhoneReceiver.requestFocus();
        }
        else if(!not_same3){
            tvPhoneReceiverValid.setVisibility(View.VISIBLE);
            tvPhoneReceiverValid.setText(getResources().getString(R.string.sender_receiver_same3));
            tvPhoneReceiverValid.setVisibility(View.VISIBLE);
            tvPhoneReceiverValid.setText(getResources().getString(R.string.sender_receiver_same3));
            edPhoneReceiver.clearFocus();
            edPhoneReceiver.requestFocus();
        }
        else{
            tvPhoneReceiverValid.setVisibility(View.GONE);
        }
        if(!text_fullname_to_isFilled){
            tvFullnameReceiverValid.setVisibility(View.VISIBLE);
            tvFullnameReceiverValid.setText(getResources().getString(R.string.fullname_to_empty));
            tvFullnameReceiver.clearFocus();
            tvFullnameReceiver.requestFocus();
        }
        else{
            tvFullnameReceiverValid.setVisibility(View.GONE);
        }
        bookingDataParcelable.bookingCourierDatas.location_detail_sender= edLocationDetailSender.getText().toString();
        if(bookingDataParcelable.bookingCourierDatas.location_detail_sender==null){
            bookingDataParcelable.bookingCourierDatas.location_detail_sender="";
        }
        if(!((text_phone_from_min10&&phone_from_isVisible)||(text_sp_phone_from_isSelected&&sp_phone_from_isVisible))){
            tvPhoneSenderValid.setVisibility(View.VISIBLE);
            tvPhoneSenderValid.setText(getResources().getString(R.string.phone_from_empty));
            edPhoneSender.clearFocus();
            edPhoneSender.requestFocus();
        }
        else if(!not_same3){
            tvPhoneSenderValid.setVisibility(View.VISIBLE);
            tvPhoneSenderValid.setText(getResources().getString(R.string.sender_receiver_same3));
            tvPhoneSenderValid.setVisibility(View.VISIBLE);
            tvPhoneSenderValid.setText(getResources().getString(R.string.sender_receiver_same3));
            edPhoneSender.clearFocus();
            edPhoneSender.requestFocus();
        }
        else{
            tvPhoneSenderValid.setVisibility(View.GONE);
        }
        if(!text_fullname_from_isFilled){
            tvFullnameSenderValid.setVisibility(View.VISIBLE);
            tvFullnameSenderValid.setText(getResources().getString(R.string.fullname_from_empty));
            tvFullnameSender.clearFocus();
            tvFullnameSender.requestFocus();
        }
        else{
            tvFullnameSenderValid.setVisibility(View.GONE);
        }

        if(((text_fullname_from_isFilled&&
                ((text_phone_from_min10&&phone_from_isVisible)||(text_sp_phone_from_isSelected&&sp_phone_from_isVisible)))&&
                (text_fullname_to_isFilled&&((text_phone_to_min10&&phone_to_isVisible)||(text_sp_phone_to_isSelected&&sp_phone_to_isVisible))))&&
                bookingDataParcelable.bookingCourierDatas.item.length()!=0&&not_same3&&bookingDataParcelable.distanceValue>0&&bookingDataParcelable.distanceValue<=25&&imagepath!=null && imageName!= null){
            RequestCourier();
        }
    }

//    private void getPositionDriverList()
//    {
//        runGetPositionDriverList=true;
//        String api = Utility.getInstance().getTokenApi(OrderCourierNew.this);
//        bookingController.getPositionDriverList(positionDriverParameters(), api, driverPositionInterface);
//    }
//    public Map<String, String> positionDriverParameters() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("transportation", bookingDataParcelable.transportation);
//        params.put("lat", String.valueOf(bookingDataParcelable.latFrom));
//        params.put("lng", String.valueOf(bookingDataParcelable.latTo));
//        return  params;
//    }
//    private DriverPositionInterface driverPositionInterface = new DriverPositionInterface() {
//        @Override
//        public void onSuccessGetDriverPosition(BaseGenericCallback<DriverPositionListModel> driverPositionBookingCallback) {
//            int sukses = driverPositionBookingCallback.getSukses();
//            if(sukses==2){
//                DriverPositionListModel data = driverPositionBookingCallback.getData();
//                List<DriverPositionListData> driverPositionListDatas = data.getResultArray();
//                for (int i = 0; i < driverPositionListDatas.size(); i++) {
//                    String lat = driverPositionListDatas.get(i).getLat();
//                    String lng = driverPositionListDatas.get(i).getLng();
//                    final double latitude = Utility.getInstance().parseDecimal(lat);
//                    final double longitude = Utility.getInstance().parseDecimal(lng);
//                    if(bookingDataParcelable.transportation.equals("motorcycle_taxi")) {
//                        final MarkerOptions markerDriver = new MarkerOptions().position(
//                                new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle));
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                googleMap.addMarker(markerDriver);
//                            }
//                        });
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onErrorGetDriverPosition(APIErrorCallback apiErrorCallback) {
//            if (apiErrorCallback.getError() != null) {
//                if (apiErrorCallback.getError().equals("Invalid API key ")) {
//                    Log.d("Unauthorized", "Jalannn");
//                    SessionManager session = new SessionManager(OrderCourierNew.this);
//                    session.logoutUser();
//                    Utility.getInstance().showInvalidApiKeyAlert(OrderCourierNew.this, getResources().getString(R.string.relogin));
//                } else {
//                }
//            }
//        }
//    };

}

