package com.pmberjaya.indotiki.app.bookingNew;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.account.login.LoginActivity;
import com.pmberjaya.indotiki.app.bookingNew.place.PlaceSelectionTab;
import com.pmberjaya.indotiki.app.deposit.DepositTopUpList;
import com.pmberjaya.indotiki.app.promo.PromoDetail;
import com.pmberjaya.indotiki.app.promo.PromoOrderActivity;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.TimeFeeCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.TimeFeeInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface;
import com.pmberjaya.indotiki.interfaces.misc.GPSTrackerInterface;
import com.pmberjaya.indotiki.models.bookingNew.DistanceData;
import com.pmberjaya.indotiki.models.bookingNew.PriceCashData;
import com.pmberjaya.indotiki.models.bookingNew.PriceDepositData;
import com.pmberjaya.indotiki.models.bookingNew.PriceEstimateData;
import com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData.Result;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.PromoCodeDataParcelable;
import com.pmberjaya.indotiki.services.TimeService;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.GPSTracker;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class OrderTransportNew extends BaseActivity implements OnMapReadyCallback, GPSTrackerInterface {
    private CoordinatorLayout layoutCoordinator;

    private TextView tvFromPlace;
    private TextView tvToPlace;
    private LinearLayout layoutOrigin;
    private LinearLayout layoutDestination;

    private String state;
    int jikaSukses;

    protected Toolbar toolbar;
    private ProgressDialog pDialog;

    GoogleMap googleMap;

    private LinearLayout layoutLoading;
    private LinearLayout layoutBooking;
    private ProgressBar progressBarPrice;
    Marker markerfrom;
    Marker markerto;
    private TextView tvPrice;
    private TextView tvDistance;
    private RelativeLayout btnOrder;
    private SessionManager session;
    private LocationSessionManager locationSession;
    private LinearLayout layoutPromo;
    private LinearLayout layoutTip;

    private TextView tvTip;
    private TextView tvPromo;
    private BookingController bookingController;
    private int TIP_FOR_RIDER = 3;
    private int SELECT_PROMO = 4;
    private UtilityController utilityController;
    private EditText edNotes;
    private int ORIGIN_PLACE = 1;
    private int DESTINATION_PLACE = 2;
    private LinearLayout layoutFooterOutput;
    private BookingDataParcelable bookingDataParcelable;
    private String term_km;
    private TextView tvOrder;
    private LinearLayout layoutPaymentPromoTip;
    boolean isDefaultPromo = false;
    private PromoCodeDataParcelable promoCodeDataParcelable;
    private TextView tvPayWithDeposit;
    private TextView tvPaymentOutput;
    private TextView tvTotalPrice;
    private RelativeLayout layoutPayWithDeposit;
    private TextView tvPriceAfterPromo;
    private CardView layoutHeaderInput;
    private ImageView icPromo;
    private ImageView icSuccessPromo;
    private Polyline line;
    private LinearLayout layoutCashMethod;
    private LinearLayout layoutInkiPayMethod;
    private TextView tvEstimateCashPrice;
    private TextView tvEstimateInkiPayPrice;
    private ImageView ivCashMethod;
    private ImageView ivInkiPayMethod;
    private String depositData;
    private ObjectAnimator blinkAnimObject;

    /************************************************************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_transport_new);
        initObject();
        renderView();
        initSession();
        init();
        GPSTracker.getInstance().init(OrderTransportNew.this,this);
    }

    private void initObject() {
        bookingController = BookingController.getInstance(OrderTransportNew.this);
        utilityController = UtilityController.getInstance(OrderTransportNew.this);
        bookingDataParcelable = new BookingDataParcelable();
        bookingDataParcelable.requestType = Constants.TRANSPORT;
    }

    @Override
    public void onLocationReceived(Location location) {
        bookingDataParcelable.latUser= location.getLatitude();
        bookingDataParcelable.lngUser= location.getLongitude();
        initializeMap();
    }

    @Override
    public void onLocationError(String str) {
        HashMap<String, String> LatLng = locationSession.getLatLng();
        String lat = LatLng.get(locationSession.KEY_LATITUDE);
        String lng = LatLng.get(locationSession.KEY_LONGITUDE);
        bookingDataParcelable.latUser = Double.parseDouble(lat);
        bookingDataParcelable.lngUser = Double.parseDouble(lng);
        initializeMap();
    }


    private void init() {
        layoutOrigin.setOnClickListener(selectFromPlace);
        layoutDestination.setOnClickListener(selectToPlace);
        layoutPromo.setOnClickListener(promoClickListener);
        layoutTip.setOnClickListener(tipRiderListener);
        layoutCashMethod.setOnClickListener(cashMethod);
        layoutInkiPayMethod.setOnClickListener(inkiPayMethod);
        blinkAnimObject = Utility.getInstance().manageBlinkEffect(layoutPromo, Utility.getColor(getResources(), R.color.amber_100, null));
    }


    private void renderView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolbar(toolbar, getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.indo_ojek), null);
        layoutBooking = (LinearLayout) findViewById(R.id.layoutBooking);
        layoutHeaderInput = (CardView) findViewById(R.id.layoutHeaderInput);
        layoutPaymentPromoTip = (LinearLayout) findViewById(R.id.layoutPaymentPromoTip);
        layoutCoordinator = (CoordinatorLayout) findViewById(R.id.layoutCoordinator);
        layoutLoading = (LinearLayout) findViewById(R.id.layoutLoading);
        layoutFooterOutput = (LinearLayout) findViewById(R.id.layoutFooterOutput);
        layoutOrigin = (LinearLayout) findViewById(R.id.layoutOrigin);
        layoutDestination = (LinearLayout) findViewById(R.id.layoutDestination);
        tvFromPlace = (TextView) findViewById(R.id.tvFromPlace);
        tvToPlace = (TextView) findViewById(R.id.tvToPlace);
        edNotes = (EditText) findViewById(R.id.edNotes);

        layoutPromo = (LinearLayout) findViewById(R.id.layoutPromo);
        tvPromo = (TextView) findViewById(R.id.tvPromo);
        layoutTip = (LinearLayout) findViewById(R.id.layoutTip);
        tvTip = (TextView) findViewById(R.id.tvTip);

        tvDistance = (TextView) findViewById(R.id.tvDistance);
        progressBarPrice = (ProgressBar) findViewById(R.id.progressBarPrice);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvPriceAfterPromo= (TextView) findViewById(R.id.tvPriceAfterPromo);
        tvPayWithDeposit = (TextView) findViewById(R.id.tvPayWithDeposit);
        tvPaymentOutput = (TextView) findViewById(R.id.tvPaymentOutput);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        layoutPayWithDeposit = (RelativeLayout) findViewById(R.id.layoutPayWithDeposit);
        btnOrder = (RelativeLayout) findViewById(R.id.btnOrder);
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        icSuccessPromo = (ImageView) findViewById(R.id.icSuccessPromo);
        icPromo = (ImageView)findViewById(R.id.icPromo);

        tvEstimateCashPrice = (TextView) findViewById(R.id.tvEstimateCashPrice);
        tvEstimateInkiPayPrice = (TextView) findViewById(R.id.tvEstimateInkiPayPrice);

        layoutCashMethod = (LinearLayout) findViewById(R.id.layoutCashMethod);
        layoutInkiPayMethod = (LinearLayout) findViewById(R.id.layoutInkiPayMethod);
        ivCashMethod = (ImageView) findViewById(R.id.ivCashMethod);
        ivInkiPayMethod = (ImageView) findViewById(R.id.ivInkiPayMethod);
    }

    private OnClickListener tipRiderListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(OrderTransportNew.this, TipRiderActivity.class);
            i.putExtra(Constants.INTENT_TIP_RIDER, bookingDataParcelable.tip);
            startActivityForResult(i, TIP_FOR_RIDER);
        }
    };

    private OnClickListener promoClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(bookingDataParcelable.distanceValue>0&&bookingDataParcelable.distanceValue<= 25&&bookingDataParcelable.price!=null){
                if(promoCodeDataParcelable!=null&&!isDefaultPromo) {
                    Intent i = new Intent(Constants.USE_PROMO_LATER);
                    i.setClass(OrderTransportNew.this, PromoDetail.class);
                    i.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
                    startActivityForResult(i, SELECT_PROMO);
                }else{
                    Intent i = new Intent(OrderTransportNew.this, PromoOrderActivity.class);
                    i.putExtra(Constants.REQUEST_TYPE, Constants.TRANSPORT);
                    startActivityForResult(i, SELECT_PROMO);
                }
            }else{
                Toast.makeText(OrderTransportNew.this, getResources().getString(R.string.complete_place), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initSession(){
        session = new SessionManager(this);
        depositData = session.getUserDetails().get(session.KEY_DEPOSIT);
        locationSession = new LocationSessionManager(this);
        bookingDataParcelable.district = locationSession.getUserDistrictIdCentral();
        state = locationSession.getUserState();
        if(session.isLogin()) {
            tvOrder.setText(getResources().getString(R.string.order));
        }else{
            tvOrder.setText(getResources().getString(R.string.not_login)+", "+getResources().getString(R.string.click_here));
        }
    }

    private String error_type;

    private OnClickListener bookHandle = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(session.isLogin()) {
                bookingDataParcelable.bookingTransportDatas.locationDetail = edNotes.getText().toString();
                requestTransport();
            }else {
                Intent i = new Intent(OrderTransportNew.this, LoginActivity.class);
                startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                showToast(getResources().getString(R.string.please_login));
            }
        }
    };
    private final int SELECT_FROM_PLACE = 1;
    private final int SELECT_TO_PLACE = 2;
    private OnClickListener selectFromPlace = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(OrderTransportNew.this, PlaceSelectionTab.class);
            i.putExtra(Constants.LATITUDE, bookingDataParcelable.latUser);
            i.putExtra(Constants.LONGITUDE, bookingDataParcelable.lngUser);
            i.putExtra(Constants.TRANSPORTATION_TYPE, bookingDataParcelable.transportation);
            i.putExtra(Constants.STATE, state);
            i.putExtra(Constants.PLACE_TYPE, "from");
            i.putExtra(Constants.AUTOLOAD, "true");
            i.putExtra(Constants.REQUEST_TYPE, Constants.TRANSPORT);
            startActivityForResult(i, SELECT_FROM_PLACE);
        }
    };

    private OnClickListener selectToPlace = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(OrderTransportNew.this, PlaceSelectionTab.class);
            i.putExtra(Constants.LATITUDE, bookingDataParcelable.latUser);
            i.putExtra(Constants.LONGITUDE, bookingDataParcelable.lngUser);
            i.putExtra(Constants.TRANSPORTATION_TYPE, bookingDataParcelable.transportation);
            i.putExtra(Constants.STATE, state);
            i.putExtra(Constants.PLACE_TYPE, "to");
            i.putExtra(Constants.AUTOLOAD, "false");
            i.putExtra(Constants.REQUEST_TYPE, Constants.TRANSPORT);
            startActivityForResult(i, SELECT_TO_PLACE);
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

    private void renderLoadingView() {
        layoutLoading.setVisibility(View.VISIBLE);
        layoutBooking.setVisibility(View.GONE);
    }

    private void tryAgainSnackbarListener(){
        if (error_type != null && error_type.equals("getcurrentaddress")) {
            getCurrentAddress(String.valueOf(bookingDataParcelable.latUser), String.valueOf(bookingDataParcelable.lngUser));
        }else {
            getTimeFee();
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
    public OnClickListener topUpInkiPaySnackbar = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(OrderTransportNew.this, DepositTopUpList.class);
            startActivity(intent);
        }
    };

    public void getIntentExtra() {
        Intent intent = getIntent();
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(intent.getAction())) {
            bookingDataParcelable = intent.getParcelableExtra(Constants.BOOKING_DATA_PARCELABLE);
            setOrigin(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom, bookingDataParcelable.fromPlace, bookingDataParcelable.from);
            setDestination(bookingDataParcelable.latTo, bookingDataParcelable.lngTo, bookingDataParcelable.toPlace, bookingDataParcelable.to);
            edNotes.setText(bookingDataParcelable.bookingTransportDatas.locationDetail);
            layoutFooterOutput.setVisibility(View.VISIBLE);
            layoutPaymentPromoTip.setVisibility(View.VISIBLE);
            layoutFooterOutput.setVisibility(View.VISIBLE);
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
        }else{
            bookingDataParcelable.transportation = intent.getStringExtra(Constants.TRANSPORTATION_TYPE);
            getCurrentAddress(String.valueOf(bookingDataParcelable.latUser), String.valueOf(bookingDataParcelable.lngUser));
        }
    }

    private void renderPriceLoadingView() {
        tvPrice.setText(getResources().getString(R.string.calculate));
        tvPrice.setPaintFlags(tvPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        tvPriceAfterPromo.setVisibility(View.GONE);
        progressBarPrice.setVisibility(View.VISIBLE);
    }

    public void initializeMap() {
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
        layoutBooking.setVisibility(View.VISIBLE);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bookingDataParcelable.latUser, bookingDataParcelable.lngUser), 13.0f));
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

    private void checkFeeParamsCompleteness(BookingDataParcelable bookingDataParcelable){
        if((Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.from)||Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.fromPlace))&&
                (Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.to)||Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.toPlace))){
            if(canUseDeposit(depositData)){
                bookingDataParcelable.payment = "3";
                ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
                ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
            }
            getTimeFee();
            layoutPaymentPromoTip.setVisibility(View.VISIBLE);
            layoutFooterOutput.setVisibility(View.VISIBLE);
        }
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
    TimeFeeInterface timeFeeInterface = new TimeFeeInterface() {
        @Override
        public void onSuccessGetTimeFee(TimeFeeCallback timeFeeCallback) {
            int sukses = timeFeeCallback.getSukses();
            if (sukses == 2) {

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
                setPaymentInfo();
                setDirectionRoute(timeFeeCallback);
                initBookButton();
            } else if (sukses == 4) {
                Utility.getInstance().showSimpleAlertDialog(OrderTransportNew.this,null, timeFeeCallback.getPesan(),
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
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(OrderTransportNew.this, getResources().getString(R.string.relogin));
                } else {
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.failed_to_get_price_data),
                            getResources().getString(R.string.try_refresh), tryAgainSnackbar, Snackbar.LENGTH_INDEFINITE);
                    layoutFooterOutput.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void callCancel(Call<TimeFeeCallback> timeFeeCallbackCall) {
            call = timeFeeCallbackCall;
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
            String pesan = timeFeeCallback.getPesan();
            bookingDataParcelable.promoCode = timeFeeCallback.getKode_promo();
            tvPromo.setText(bookingDataParcelable.promoCode);
            tvPromo.setTextColor(Utility.getColor(getResources(),R.color.black,null));
            Utility.getInstance().showSimpleAlertDialog(OrderTransportNew.this, getResources().getString(R.string.promo_code_right2), pesan +
                            "\nHarga awal: Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.originalPrice) + "\nHarga akhir: " + priceText+" "+term_km,
                    "OK", positivePromoDialogListener,
                    null, null, false);
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrice.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.originalPrice));
            tvPriceAfterPromo.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
            tvPriceAfterPromo.setVisibility(View.VISIBLE);
            icPromo.setVisibility(View.GONE);
            icSuccessPromo.setVisibility(View.VISIBLE);
        }else  {
            if(timeFeeCallback.getPesan()==null){
                deleteCodePromo();
            }else {
                showPromoErrorAlertDialog(timeFeeCallback.getPesan());
            }
            tvPrice.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
        }
    }
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
                    Intent i = new Intent(OrderTransportNew.this, LoginActivity.class);
                    startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                    showToast(getResources().getString(R.string.please_login));
                }
            }
        }
    };
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
        layoutFooterOutput.setVisibility(View.VISIBLE);
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

    public void getCurrentAddress(String latitude_from, String longitude_from)
    {
        String origin = latitude_from+","+longitude_from;
        utilityController.getcurrentaddress(utilityController.geocoderParams(origin),currentAddressInterface);
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
                    layoutBooking.setVisibility(View.VISIBLE);
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
                    Utility.getInstance().showInvalidApiKeyAlert(OrderTransportNew.this, getResources().getString(R.string.relogin));
                } else {
                    error_type = "getcurrentaddress";
                    indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.cannot_determine),
                            getResources().getString(R.string.try_refresh), tryAgainSnackbar, Snackbar.LENGTH_INDEFINITE);
                }
            }
        }
    };
    Call<TimeFeeCallback> call;
    public void getTimeFee() {
        if(call!=null){
            call.cancel();
        }
        renderPriceLoadingView();
        String api = Utility.getInstance().getTokenApi(OrderTransportNew.this);
        disabledBookButton();
        bookingController.getTimeFee(bookingController.timeFeeParams(bookingDataParcelable), api, timeFeeInterface);
    }

    private void deleteCodePromo(){
        promoCodeDataParcelable = null;
        icSuccessPromo.setVisibility(View.GONE);
        icPromo.setVisibility(View.VISIBLE);
        bookingDataParcelable.promoCode = "";
        tvPromo.setText(getResources().getString(R.string.promo));
        tvPromo.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
    }
    private void showPromoErrorAlertDialog(String pesan){
        deleteCodePromo();
        Utility.getInstance().showSimpleAlertDialog(OrderTransportNew.this, getResources().getString(R.string.promo_code_wrong), pesan,
                "OK", positivePromoDialogErrorListener,
                null, null, false);
    }

    private void setPaymentInfo() {

        if (bookingDataParcelable.payment.equals("1")) {
            layoutPayWithDeposit.setVisibility(View.GONE);
            tvPaymentOutput.setText(getResources().getString(R.string.total_paid_in_cash));
            tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.cashPaid));
        }  else if (bookingDataParcelable.payment.equals("3")) {
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.cashPaid)&&bookingDataParcelable.cashPaid.equals("0")){
                layoutPayWithDeposit.setVisibility(View.GONE);
                tvPaymentOutput.setText(getResources().getString(R.string.total_paid_in_deposit));
                tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.depositPaid));
            }else{
                layoutPayWithDeposit.setVisibility(View.VISIBLE);
                tvPayWithDeposit.setText("- Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.depositPaid));
                tvPaymentOutput.setText(getResources().getString(R.string.total_paid_in_cash));
                tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.cashPaid));
            }
        }
        if (bookingDataParcelable.price == null || bookingDataParcelable.price.equals("0")) {
            tvTotalPrice.setText(getResources().getString(R.string.free));
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

    public boolean canUseDeposit(String depositData){
        if (!Utility.getInstance().checkIfStringIsNotNullOrEmpty(depositData)||depositData.equals("0"))
            return false;
        return true;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ORIGIN_PLACE) {
            if (resultCode == RESULT_OK) {
                if(indotikiOjekSnackbar!=null){
                    indotikiOjekSnackbar.dismiss();
                }
                String latitudefrom = data.getStringExtra(Constants.LATITUDE);
                String longitudefrom = data.getStringExtra(Constants.LONGITUDE);
                bookingDataParcelable.fromPlace = data.getStringExtra(Constants.PLACE);
                bookingDataParcelable.from = data.getStringExtra(Constants.PLACE_DETAILS);
                bookingDataParcelable.latFrom = Utility.getInstance().parseDecimal(latitudefrom);
                bookingDataParcelable.lngFrom = Utility.getInstance().parseDecimal(longitudefrom);
                setOrigin(bookingDataParcelable.latFrom, bookingDataParcelable.lngFrom,  bookingDataParcelable.fromPlace, bookingDataParcelable.from);
                checkFeeParamsCompleteness(bookingDataParcelable);
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == DESTINATION_PLACE) {
            if (resultCode == RESULT_OK) {
                if(indotikiOjekSnackbar!=null){
                    indotikiOjekSnackbar.dismiss();
                }
                String latitudeto = data.getStringExtra(Constants.LATITUDE);
                String longitudeto = data.getStringExtra(Constants.LONGITUDE);
                bookingDataParcelable.toPlace = data.getStringExtra(Constants.PLACE);
                bookingDataParcelable.to = data.getStringExtra(Constants.PLACE_DETAILS);
                bookingDataParcelable.latTo = Utility.getInstance().parseDecimal(latitudeto);
                bookingDataParcelable.lngTo = Utility.getInstance().parseDecimal(longitudeto);
                setDestination(bookingDataParcelable.latTo, bookingDataParcelable.lngTo,  bookingDataParcelable.toPlace, bookingDataParcelable.to);
                checkFeeParamsCompleteness(bookingDataParcelable);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == Constants.STATE_LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
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
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == TIP_FOR_RIDER) {
            if (resultCode == RESULT_OK) {
                bookingDataParcelable.tip = data.getStringExtra(Constants.INTENT_TIP_RIDER);
                if(bookingDataParcelable.tip!=null&&bookingDataParcelable.tip.equals("0")){
                    tvTip.setText(getResources().getString(R.string.tip_for_rider));
                    tvTip.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
                }
                else{
                    tvTip.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.tip));
                    tvTip.setTextColor(Utility.getColor(getResources(),R.color.black,null));
                }
                getTimeFee();
            } else if (resultCode == RESULT_CANCELED) {

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
                    bookingDataParcelable.promoCode = "";
                    tvPromo.setText(getResources().getString(R.string.promo));
                    tvPromo.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
                    icSuccessPromo.setVisibility(View.GONE);
                    icPromo.setVisibility(View.VISIBLE);
                    getTimeFee();
                    blinkAnimObject = Utility.getInstance().manageBlinkEffect(layoutPromo, Utility.getColor(getResources(), R.color.amber_100, null));
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        switch (requestCode) {
            case GPSTracker.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d("YES","OK");
                        // All required changes were successfully made
                        GPSTracker.getInstance().startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d("CANCELLED","NO");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void setOrigin(double latitudeFrom, double longitudeFrom, String fromPlace, String from){
        if (fromPlace == null) {
            tvFromPlace.setText(from);
        } else {
            tvFromPlace.setText(fromPlace + ", " + from);
        }
        tvFromPlace.setTextColor(Utility.getColor(getResources(),R.color.black,null));
        setMarkerFrom(latitudeFrom, longitudeFrom);
    }

    private void setDestination(double latitudeTo, double longitudeTo, String toPlace, String to){
        if (toPlace == null) {
            tvToPlace.setText(to);
        } else {
            tvToPlace.setText(toPlace + ", " + to);
        }
        tvToPlace.setTextColor(Utility.getColor(getResources(),R.color.black,null));
        setMarkerTo(latitudeTo, longitudeTo);
    }
    int headerHeight;
    int layoutFooterOutputHeight;
    public void centerIncidentRouteOnMap(double latitude_from, double longitude_from, double latitude_to, double longitude_to) {
        setMapPadding();
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

    private void setMapPadding() {
        layoutHeaderInput.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        headerHeight = layoutHeaderInput.getMeasuredHeight()+Utility.getInstance().getDimensionFromValuesResources(getResources(), R.dimen.small_padding_margin);
        layoutFooterOutput.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        layoutFooterOutputHeight = layoutFooterOutput.getMeasuredHeight()/*+Utility.getInstance().getDimensionFromValuesResources(getResources(), R.dimen.tiny_padding_margin)*/;
        googleMap.setPadding(0,headerHeight,0,layoutFooterOutputHeight);
    }

    private void setMarkerFrom(double lat, double lng) {
        if (markerfrom != null) {
            markerfrom.remove();
        }
        final MarkerOptions markerfromOption = new MarkerOptions().position(
                new LatLng(lat, lng)).draggable(false);

        // changing marker color
        markerfromOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_from_place));//icon(BitmapDescriptorFactory.fromResource(R.drawable.schools_maps));

        runOnUiThread(new Runnable() {
            public void run() {
                markerfrom = googleMap.addMarker(markerfromOption);
            }
        });
    }

    private void setMarkerTo(double lat, double lng) {
            if (markerto != null) {
                markerto.remove();
            }
            final MarkerOptions markertoOption = new MarkerOptions().position(
                    new LatLng(lat, lng)).draggable(false);
            // changing marker color
            markertoOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_to_place));//icon(BitmapDescriptorFactory.fromResource(R.drawable.schools_maps));
            runOnUiThread(new Runnable() {
                public void run() {
                    markerto = googleMap.addMarker(markertoOption);
                }
            });
    }




    public void requestTransport() {
        btnOrder.setEnabled(false);
        progressBarPrice.setVisibility(View.VISIBLE);
        pDialog = new ProgressDialog(OrderTransportNew.this);
        pDialog.setMessage("Mohon Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        String api = Utility.getInstance().getTokenApi(OrderTransportNew.this);
        bookingController.requestTransport(bookingController.requestTransportParams(bookingDataParcelable), api, requestTransportInterface);
        return;
    }

    BaseGenericInterface requestTransportInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> requestTransportCallback) {
            pDialog.dismiss();
            btnOrder.setEnabled(true);
            bookingDataParcelable.id = (String) requestTransportCallback.getData();
            jikaSukses = requestTransportCallback.getSukses();
            String pesan = requestTransportCallback.getPesan();
            //String pesan = requestCourierCallback.getPesan();
            progressBarPrice.setVisibility(View.GONE);
            if (jikaSukses == 1) {
                Toast.makeText(OrderTransportNew.this, getResources().getString(R.string.service_unavailable), Toast.LENGTH_LONG).show();
            } else if (jikaSukses == 2) {
                SessionManager sessionManager = new SessionManager(OrderTransportNew.this);
                sessionManager.setTimerRepeatData(bookingDataParcelable.id, Constants.TRANSPORT, System.currentTimeMillis());
                Intent service = new Intent(OrderTransportNew.this, TimeService.class);
                startService(service);
                Intent intent = new Intent(OrderTransportNew.this, SearchDriverNew.class);
                intent.putExtra("request_id", bookingDataParcelable.id);
                intent.putExtra("request_type", Constants.TRANSPORT);
                intent.putExtra("transportation", bookingDataParcelable.transportation);
                startActivity(intent);
            } else {
                progressBarPrice.setVisibility(View.GONE);
                Toast.makeText(OrderTransportNew.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            progressBarPrice.setVisibility(View.GONE);
            btnOrder.setEnabled(true);
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(OrderTransportNew.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(OrderTransportNew.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

//    private void getPositionDriverList() {
//        removeCurrentDriverMarkers(markerDrivers);
//        String api = Utility.getInstance().getTokenApi(OrderTransportNew.this);
//        bookingController.getPositionDriverList(bookingController.positionDriverParameters(bookingDataParcelable), api, driverPositionInterface);
//    }
//
//    private void removeCurrentDriverMarkers(List<Marker> markerDrivers) {
//        for (int i = 0; i < markerDrivers.size(); i++) {
//            markerDrivers.get(i).remove();
//        }
//    }

//    List<Marker> markerDrivers;
//    Marker markerDriver;
//    private DriverPositionInterface driverPositionInterface = new DriverPositionInterface() {
//        @Override
//        public void onSuccessGetDriverPosition(BaseGenericCallback<DriverPositionListModel> driverPositionBookingCallback) {
//            int sukses = driverPositionBookingCallback.getSukses();
//            if (sukses == 2) {
//                DriverPositionListModel data = driverPositionBookingCallback.getData();
//                List<DriverPositionListData> driverPositionListDatas = data.getResultArray();
//                for (int i = 0; i < driverPositionListDatas.size(); i++) {
//                    String lat = driverPositionListDatas.get(i).getLat();
//                    String lng = driverPositionListDatas.get(i).getLng();
//                    final double latitude = Utility.getInstance().parseDecimal(lat);
//                    final double longitude = Utility.getInstance().parseDecimal(lng);
//                    final MarkerOptions markerDriverOption= new MarkerOptions();
//
//                    if (bookingDataParcelable.transportation.equals("motorcycle_taxi")) {
//                        markerDriverOption.position(
//                                new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_motorcycle));
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                markerDriver = googleMap.addMarker(markerDriverOption);
//                            }
//                        });
//                    }
//                    markerDrivers.add(markerDriver);
//                }
//            }
//        }
//
//        @Override
//        public void onErrorGetDriverPosition(APIErrorCallback apiErrorCallback) {
//            if (apiErrorCallback.getError() != null) {
//                if (apiErrorCallback.getError().equals("Invalid API key ")) {
//                    Log.d("Unauthorized", "Jalannn");
//                    SessionManager session = new SessionManager(OrderTransportNew.this);
//                    session.logoutUser();
//                    Utility.getInstance().showInvalidApiKeyAlert(OrderTransportNew.this, getResources().getString(R.string.relogin));
//                } else {
//                }
//            }
//        }
//    };
}

