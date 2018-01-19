package com.pmberjaya.indotiki.app.bookingNew;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.account.login.LoginActivity;
import com.pmberjaya.indotiki.app.bookingNew.place.PlaceSelectionTab;
import com.pmberjaya.indotiki.app.deposit.DepositTopUpList;
import com.pmberjaya.indotiki.app.promo.PromoDetail;
import com.pmberjaya.indotiki.app.promo.PromoOrderActivity;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.TimeFeeCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.interfaces.bookingNew.TimeFeeInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressMemberData;
import com.pmberjaya.indotiki.models.bookingNew.DistanceData;
import com.pmberjaya.indotiki.models.bookingNew.PriceCashData;
import com.pmberjaya.indotiki.models.bookingNew.PriceDepositData;
import com.pmberjaya.indotiki.models.bookingNew.PriceEstimateData;
import com.pmberjaya.indotiki.models.deposit.DepositData.CheckDepositData;
import com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData.Result;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.FoodItemTempData;
import com.pmberjaya.indotiki.models.parcelables.PromoCodeDataParcelable;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by edwin on 4/13/2016.
 */
public class OrderFoodNew extends BaseActivity {
    private static final int SELECT_TO_PLACE = 2;
    SessionManager session;
    FoodItemTempData dataData;
    private ScrollView layoutBooking;
    private RelativeLayout fab_minus;
    private RelativeLayout fab_plus;
    private RelativeLayout btnOrder;
    private TextView tv_menu_quantity;
    private TextView tvTotalFoodPrice;
    private TextView tvPrice;
    private TextView tvTo;
    private TextView tvOrder;
    private TextView tv_nama_makanan;
    private EditText editText;
    private LinearLayout layoutFoodItemData;
    private List views;
    private EditText etLocationDetail;
    //---------------------------------------------------------------------------String------------------------------------------------------------
    private String userId;
    private String requestType = Constants.FOOD;
    private List<String> listNamaMakanan;
    private List<String> listDetail;
    private List<String> listIdMakanan;
    private List<String> listPosition;
    private int positionMenu;
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------int----------------------------------------------------------------
    public int tempQuantity;
    private int i;
    private ProgressDialog pDialog;
    private TextView tvDistance;
    private ArrayList<FoodItemTempData> foodItemTempCompoundDatas;
    private TextView tvPromoCode;
    private ProgressBar progressBarPrice;
    private TextView tvItemsToDeliver;
    private TextView tvDeliverTo;
    private TextView tvPaymentDetail;
    private LinearLayout total_price_per_item_layout;
    private TextView tv_total_price_per_item;
    private TextView tvTotalPrice;
    private LinearLayout layoutPayWithDeposit;
    private LinearLayout payment_layout;
    private TextView tvPriceAfterPromo;
    private TextView tvTip;
    private LinearLayout layoutTip;
    private BookingController bookingController;
    private int TIP_FOR_RIDER = 3;
    private LinearLayout layoutPromo;
    private ImageView icSuccessPromo;
    private DBController dbController;
    ArrayList<BookingInProgressMemberData> onGoingBookingInProgressMembers;
    private String term_km;
    private UtilityController utilityController;
    private BookingDataParcelable bookingDataParcelable;
    private String id_makanan;
    private String depositData;
    private PromoCodeDataParcelable promoCodeDataParcelable;
    private int SELECT_PROMO = 4;
    private boolean isDefaultPromo = false;

    private TextView tvPaymentOutput;
    private TextView tvPayWithDeposit;
    private ImageView icPromo;
    Call<TimeFeeCallback> call;
    private String distanceValue;
    private String distanceText;
    private boolean readyorder = false;
    private Toolbar toolbar;

    private TextView tvEstimateCashPrice;
    private TextView tvEstimateInkiPayPrice;
    private LinearLayout layoutCashMethod;
    private LinearLayout layoutInkiPayMethod;
    private ImageView ivCashMethod;
    private ImageView ivInkiPayMethod;
    private ObjectAnimator blinkAnimObject;
    private LinearLayout layoutDestination;
    private CoordinatorLayout layoutCoordinator;
    private boolean isTriggerFromPromoCode = false;
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_food_new);
        renderView();
        initController();
        initDB();
        initSession();
        init();
        initContent();
    }
    public void initController(){
        bookingController = BookingController.getInstance(this);
        utilityController = UtilityController.getInstance(this);
    }
    public void initDB(){
        dbController = DBController.getInstance(this);
        onGoingBookingInProgressMembers = dbController.getBookingInProgress();
    }
    public void initContent(){
        getIntentExtra();
        initFoodItemList();
    }

    private void initSession(){
        session = new SessionManager(this);
        HashMap<String,String> mapData = session.getUserDetails();
        userId = mapData.get(SessionManager.KEY_ID);
        depositData = mapData.get(SessionManager.KEY_DEPOSIT);
    }
    private void renderView(){
        toolbar = findViewById(R.id.tool_bar);
        initToolbar(toolbar, getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.indo_food), null);
        layoutCoordinator = findViewById(R.id.layoutCoordinator);
        layoutBooking = findViewById(R.id.layoutBooking);
        layoutFoodItemData = findViewById(R.id.layoutFoodItemData);
        tvTotalFoodPrice = findViewById(R.id.tvTotalFoodPrice);
        tvPrice = findViewById(R.id.tvPrice);
        tvPriceAfterPromo = findViewById(R.id.tvPriceAfterPromo);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        layoutPayWithDeposit = findViewById(R.id.layoutPayWithDeposit);
        layoutDestination = findViewById(R.id.layoutDestination);
        tvTo = findViewById(R.id.tvTo);
        etLocationDetail = findViewById(R.id.etLocationDetail);
        btnOrder = findViewById(R.id.btnOrder);
        tvOrder = findViewById(R.id.tvOrder);
        tvDistance = findViewById(R.id.tvDistance);
        layoutTip = findViewById(R.id.layoutTip);
        layoutPromo = findViewById(R.id.layoutPromo);
        icSuccessPromo = findViewById(R.id.icSuccessPromo);
        tvTip = findViewById(R.id.tvTip);
        tvTip.setSelected(true);

        tvPromoCode = findViewById(R.id.tvPromoCode);
        tvPromoCode.setSelected(true);
        progressBarPrice = findViewById(R.id.progressBarPrice);

        tvItemsToDeliver = findViewById(R.id.tvItemsToDeliver);
        tvDeliverTo = findViewById(R.id.tvDeliverTo);

        tvPaymentDetail = findViewById(R.id.tvPaymentDetail);
        tvPayWithDeposit = findViewById(R.id.tvPayWithDeposit);

        tvPaymentOutput  = findViewById(R.id.tvPaymentOutput);
        icPromo = findViewById(R.id.icPromo);
        tvEstimateCashPrice = (TextView) findViewById(R.id.tvEstimateCashPrice);
        tvEstimateInkiPayPrice = (TextView) findViewById(R.id.tvEstimateInkiPayPrice);

        Typeface custom_font = Typeface.createFromAsset(getResources().getAssets(), "fonts/BenchNine-Bold.ttf");
        tvItemsToDeliver.setTypeface(custom_font);
        tvDeliverTo.setTypeface(custom_font);
        tvPaymentDetail.setTypeface(custom_font);

        layoutCashMethod = findViewById(R.id.layoutCashMethod);
        layoutInkiPayMethod = findViewById(R.id.layoutInkiPayMethod);
        ivCashMethod = findViewById(R.id.ivCashMethod);
        ivInkiPayMethod = findViewById(R.id.ivInkiPayMethod);
    }
    private void init(){
        tvTo.setOnClickListener(selectPlace);
        layoutTip.setOnClickListener(tipRiderListener);
        layoutPromo.setOnClickListener(promoListener);
        layoutCashMethod.setOnClickListener(cashMethod);
        layoutInkiPayMethod.setOnClickListener(inkiPayMethod);
        blinkAnimObject = Utility.getInstance().manageBlinkEffect(layoutPromo, Utility.getColor(getResources(), R.color.amber_100, null));
    }
    private View.OnClickListener tipRiderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(OrderFoodNew.this, TipRiderActivity.class);
            i.putExtra(Constants.INTENT_TIP_RIDER, bookingDataParcelable.tip);
            startActivityForResult(i, TIP_FOR_RIDER);
        }
    };
    private View.OnClickListener promoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!tvTo.getText().equals(getResources().getString(R.string.place_name))){
                if(distanceValue!=null&&Double.parseDouble(distanceValue)<=25&&bookingDataParcelable.price!=null){
                    if(promoCodeDataParcelable!=null&&!isDefaultPromo) {
                        Intent i = new Intent(Constants.USE_PROMO_LATER);
                        i.setClass(OrderFoodNew.this, PromoDetail.class);
                        i.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
                        startActivityForResult(i, SELECT_PROMO);
                    }else{
                        Intent i = new Intent(OrderFoodNew.this, PromoOrderActivity.class);
                        i.putExtra(Constants.REQUEST_TYPE, Constants.FOOD);
                        startActivityForResult(i, SELECT_PROMO);
                    }
                }
            }else{
                show_place_valid_listener();
            }
        }
    };
    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(error_type.equals("getTimeFee")){
                getTimeFee(isTriggerFromPromoCode);
            }
        }
    };
    private void getCheckDeposit() {
        String api = Utility.getInstance().getTokenApi(OrderFoodNew.this);
        UserController.getInstance(OrderFoodNew.this).getCheckDeposit(api, checkDepositInterface);
    }
    BaseGenericInterface checkDepositInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> checkDepositCallback) {
            int sukses = checkDepositCallback.getSukses();
            String pesan = checkDepositCallback.getPesan();
            if (sukses == 2) {
                CheckDepositData data = (CheckDepositData) checkDepositCallback.getData();
                depositData = data.getDeposit();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(OrderFoodNew.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(OrderFoodNew.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public void show_place_valid_listener () {
        Toast.makeText(OrderFoodNew.this, getResources().getString(R.string.complete_place_destination), Toast.LENGTH_SHORT).show();
        tvTo.clearFocus();
        tvTo.requestFocus();
    }
    private void initFoodItemList(){
        listNamaMakanan = new ArrayList<String>();
        listDetail = new ArrayList<String>();
        listIdMakanan = new ArrayList<String>();
        listPosition = new ArrayList<String>();
        views = new ArrayList();
        for (i = 0; i < foodItemTempCompoundDatas.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.order_food_new_menu_list_adapter, null);
            tv_nama_makanan = child.findViewById(R.id.nama_makanan);
            total_price_per_item_layout = child.findViewById(R.id.total_price_per_item_layout);
            tv_total_price_per_item = child.findViewById(R.id.total_price_per_item);
            final TextView tv_harga_makanan = child.findViewById(R.id.harga_makanan);
            tv_menu_quantity = child.findViewById(R.id.tv_menu_quantity);
            tv_menu_quantity.setText(String.valueOf(foodItemTempCompoundDatas.get(i).getQuantity()));

            int total_price_per_item = foodItemTempCompoundDatas.get(i).getQuantity()*Integer.parseInt(foodItemTempCompoundDatas.get(i).getPrice());
            if(total_price_per_item<=0){
                total_price_per_item_layout.setVisibility(View.GONE);
            }else{
                total_price_per_item_layout.setVisibility(View.VISIBLE);
                tv_total_price_per_item.setText("Rp "+Utility.getInstance().convertPrice(total_price_per_item));
            }
            tv_nama_makanan.setText(foodItemTempCompoundDatas.get(i).getLabel());
            tv_harga_makanan.setText("Rp "+Utility.getInstance().convertPrice(foodItemTempCompoundDatas.get(i).getPrice()));
            id_makanan = foodItemTempCompoundDatas.get(i).getId();
            listIdMakanan.add(id_makanan);
            listNamaMakanan.add(tv_nama_makanan.getText().toString());
            listPosition.add(String.valueOf(i));
            if(!tv_menu_quantity.getText().equals("0")){
                views.add(child);
            }
        }

        for (i = 0; i < views.size(); i++) {
            dataData = foodItemTempCompoundDatas.get(i);
            View view = ((View) views.get(i));
            fab_plus = view.findViewById(R.id.fab_plus);
            fab_minus = view.findViewById(R.id.fab_minus);


            editText = view.findViewById(R.id.edit_text);
            InputFilter[] filterEmoji = new InputFilter[] {Utility.getInstance().getEditTextFilterEmoji()};
            editText.setFilters(filterEmoji);
            editText.setTag(i);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        positionMenu = (int)v.getTag();
                        Log.d("Positiong",">"+String.valueOf(positionMenu));
                    }
                }
            });
            fab_plus.setTag(i);
            fab_minus.setTag(i);

            fab_plus.setOnClickListener(plusQuantityItem);
            fab_minus.setOnClickListener(minusQuantityItem);
            if(foodItemTempCompoundDatas.get(i).getDescription_note()!=null)
            {
                if(!foodItemTempCompoundDatas.get(i).getDescription_note().equals("")) {
                    editText.setText(foodItemTempCompoundDatas.get(i).getDescription_note());
                    editText.addTextChangedListener(noteTextWatcher);
                    listDetail.add(foodItemTempCompoundDatas.get(i).getDescription_note());
                }else{
                    editText.addTextChangedListener(noteTextWatcher);
                }
            }
            else
            {
                editText.setText(foodItemTempCompoundDatas.get(i).getDescription_note());
                editText.addTextChangedListener(noteTextWatcher);
            }
            layoutFoodItemData.addView((View) views.get(i));
        }
    }
    private String error_type;
    private TextWatcher noteTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            foodItemTempCompoundDatas.get(positionMenu).setDescription_note(s.toString());
        }
    };

    private void getIntentExtra(){
        Intent intent = getIntent();
        bookingDataParcelable = intent.getParcelableExtra(Constants.BOOKING_DATA_PARCELABLE);
//        bookingDataParcelable.latFrom = Utility.getInstance().parseDecimal(bookingDataParcelable.bookingFoodDatas.restaurantData.getStore_lat());
//        bookingDataParcelable.lngFrom = Utility.getInstance().parseDecimal(bookingDataParcelable.bookingFoodDatas.restaurantData.getStore_lng());
        foodItemTempCompoundDatas = new ArrayList<FoodItemTempData>();
        foodItemTempCompoundDatas.addAll(bookingDataParcelable.bookingFoodDatas.foodItemTempDatas);
        foodItemTempCompoundDatas.addAll(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas);
        tempQuantity = bookingDataParcelable.bookingFoodDatas.foodQuantityTotal;
        if (bookingDataParcelable.distance != null) {
            tvDistance.setText("(" + bookingDataParcelable.distance + ")");
        }
        if (bookingDataParcelable.bookingFoodDatas.locationDetail != null) {
            etLocationDetail.setText(bookingDataParcelable.bookingFoodDatas.locationDetail);
            etLocationDetail.setVisibility(View.VISIBLE);
        }
        tvTotalFoodPrice.setText("Rp. " + Utility.getInstance().convertPrice(String.valueOf(bookingDataParcelable.bookingFoodDatas.foodCost)));
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.toPlace) && Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.to)) {
            tvTo.setText(bookingDataParcelable.toPlace + ", " + bookingDataParcelable.to);
        } else if (!Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.toPlace) && Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.to)) {
            tvTo.setText(bookingDataParcelable.to);
        } else {
            getCurrentAddress(String.valueOf(bookingDataParcelable.latUser), String.valueOf(bookingDataParcelable.lngUser));
        }
        if(canUseDeposit(depositData)){
            bookingDataParcelable.payment = "3";
            ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
            ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
        }
        if (!tvTo.getText().equals(getResources().getString(R.string.pickup_location))) {
            getTimeFee(false);
        }

    }
    private View.OnClickListener selectPlace = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(OrderFoodNew.this, PlaceSelectionTab.class);
            i.putExtra(Constants.LATITUDE, bookingDataParcelable.latUser);
            i.putExtra(Constants.LONGITUDE, bookingDataParcelable.lngUser);
            i.putExtra(Constants.TRANSPORTATION_TYPE, bookingDataParcelable.transportation);
            i.putExtra(Constants.STATE, bookingDataParcelable.state);
            i.putExtra(Constants.PLACE_TYPE, "to");
            i.putExtra(Constants.AUTOLOAD, "true");
            i.putExtra(Constants.REQUEST_TYPE, bookingDataParcelable.requestType);
            startActivityForResult(i, SELECT_TO_PLACE);
        }
    };
    public void getCurrentAddress(String latitude_from, String longitude_from)
    {
        String origin = latitude_from+","+longitude_from;
        utilityController.getcurrentaddress(utilityController.geocoderParams(origin), currentAddressInterface);
        return;
    }

    private GeocoderLocationInterface currentAddressInterface = new GeocoderLocationInterface() {
        @Override
        public void onSuccessGetGeocoderLocation(GeocoderLocationGMapsCallback geocoderLocationGMapsCallback) {
            String status= geocoderLocationGMapsCallback.getStatus();
            if (status.equals("OK")) {
                List<Result> result =  geocoderLocationGMapsCallback.getResults();
                if(result.size()!=0) {
                    bookingDataParcelable.to= result.get(0).getFormattedAddress();
                    bookingDataParcelable.latTo = bookingDataParcelable.latUser;
                    bookingDataParcelable.lngTo = bookingDataParcelable.lngUser;
                    setDestination();
                }else{
                }
            } else {
            }
        }

        @Override
        public void onErrorGetGeocoderLocation(APIErrorCallback data) {

        }
    };

    private View.OnClickListener plusQuantityItem =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            View view = ((View) views.get(position));
            TextView tv_menu_quantity = view.findViewById(R.id.tv_menu_quantity);
            LinearLayout total_price_per_item_layout = view.findViewById(R.id.total_price_per_item_layout);
            TextView tv_total_price_per_item = view.findViewById(R.id.total_price_per_item);
            foodItemTempCompoundDatas.get(position).setQuantity(foodItemTempCompoundDatas.get(position).getQuantity() + 1);
            tv_menu_quantity.setText(String.valueOf(foodItemTempCompoundDatas.get(position).getQuantity()));
            bookingDataParcelable.bookingFoodDatas.foodCost += Integer.parseInt(foodItemTempCompoundDatas.get(position).getPrice());
            bookingDataParcelable.bookingFoodDatas.foodQuantityTotal += 1;
            int total_price_per_item = foodItemTempCompoundDatas.get(position).getQuantity()*Integer.parseInt(foodItemTempCompoundDatas.get(position).getPrice());
            if(total_price_per_item<=0){
                total_price_per_item_layout.setVisibility(View.GONE);
            }else{
                total_price_per_item_layout.setVisibility(View.VISIBLE);
                tv_total_price_per_item.setText("Rp "+Utility.getInstance().convertPrice(total_price_per_item));
            }
            tvTotalFoodPrice.setText("Rp " + Utility.getInstance().convertPrice(String.valueOf(bookingDataParcelable.bookingFoodDatas.foodCost)));
            getTimeFee(false);
        }
    };

    private View.OnClickListener minusQuantityItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            View view = ((View) views.get(position));
            if (foodItemTempCompoundDatas.get(position).getQuantity()==0) {
            } else {
                TextView tv_menu_quantity = view.findViewById(R.id.tv_menu_quantity);
                LinearLayout total_price_per_item_layout = view.findViewById(R.id.total_price_per_item_layout);
                TextView tv_total_price_per_item = view.findViewById(R.id.total_price_per_item);
                foodItemTempCompoundDatas.get(position).setQuantity(foodItemTempCompoundDatas.get(position).getQuantity() - 1);
                tv_menu_quantity.setText(String.valueOf(foodItemTempCompoundDatas.get(position).getQuantity()));
                bookingDataParcelable.bookingFoodDatas.foodCost -= Integer.parseInt(foodItemTempCompoundDatas.get(position).getPrice());
                bookingDataParcelable.bookingFoodDatas.foodQuantityTotal -= 1;
                int total_price_per_item = foodItemTempCompoundDatas.get(position).getQuantity()*Integer.parseInt(foodItemTempCompoundDatas.get(position).getPrice());
                if(total_price_per_item<=0){
                    total_price_per_item_layout.setVisibility(View.GONE);
                }else{
                    total_price_per_item_layout.setVisibility(View.VISIBLE);
                    tv_total_price_per_item.setText("Rp "+Utility.getInstance().convertPrice(total_price_per_item));
                }
                tvTotalFoodPrice.setText("Rp " + Utility.getInstance().convertPrice(String.valueOf(bookingDataParcelable.bookingFoodDatas.foodCost)));
                getTimeFee(false);
            }
        }
    };

    public DialogInterface.OnClickListener positiveDismissTipDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            bookingDataParcelable.tip = "0";
            tvTip.setTextColor(Utility.getColor(getResources(), R.color.grey_600, null));
            tvTip.setText(getResources().getString(R.string.tip_for_rider));
            getTimeFee(false);
            dialogInterface.dismiss();
        }
    };
    public DialogInterface.OnClickListener positiveDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };
    public View.OnClickListener topUpInkiPaySnackbar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(OrderFoodNew.this, DepositTopUpList.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener cashMethod = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.payment)&&!bookingDataParcelable.payment.equals("1")) {
                bookingDataParcelable.payment = "1";
                getTimeFee(false);
                ivCashMethod.setImageResource(R.mipmap.ic_success);
                ivInkiPayMethod.setImageResource(R.drawable.xml_border_circle_grey);
            }
        }
    };
    Snackbar indotikiOjekSnackbar;
    private View.OnClickListener inkiPayMethod = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.payment)&&!bookingDataParcelable.payment.equals("3")) {
                if (session.isLogin()) {
                    if (canUseDeposit(depositData)) {
                        bookingDataParcelable.payment = "3";
                        getTimeFee(false);
                        ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
                        ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
                    } else {
                        indotikiOjekSnackbar =  Utility.getInstance().showSnackbar(layoutCoordinator, getResources().getString(R.string.not_enough_inki_pay, Utility.getInstance().convertPrice(depositData)),
                                getResources().getString(R.string.top_up), topUpInkiPaySnackbar, Snackbar.LENGTH_LONG);
                    }
                } else {
                    Intent i = new Intent(OrderFoodNew.this, LoginActivity.class);
                    startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                    showToast(getResources().getString(R.string.please_login));
                }
            }
        }
    };
    public boolean canUseDeposit(String depositData){
        return !(!Utility.getInstance().checkIfStringIsNotNullOrEmpty(depositData) || depositData.equals("0"));
    }
    private void setEstimatePrice(TimeFeeCallback timeFeeCallback) {
        PriceEstimateData priceEstimateData = timeFeeCallback.getPrice_est();
        PriceCashData priceCashData = priceEstimateData.getCash();
        PriceDepositData priceDepositData = priceEstimateData.getDeposit();

        String cashEstimate = "Rp. " +Utility.getInstance().convertPrice(priceCashData.getPrice());
        String inkiPayEstimate = "Rp. " +Utility.getInstance().convertPrice(priceDepositData.getPrice());
        tvEstimateCashPrice.setText(cashEstimate);
        tvEstimateInkiPayPrice.setText(inkiPayEstimate);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SELECT_TO_PLACE) {
            if(resultCode == RESULT_OK){
                String latitudeto = data.getStringExtra(Constants.LATITUDE);
                String longitudeto = data.getStringExtra(Constants.LONGITUDE);
                bookingDataParcelable.toPlace = data.getStringExtra(Constants.PLACE);
                bookingDataParcelable.to = data.getStringExtra(Constants.PLACE_DETAILS);
                bookingDataParcelable.latTo = Utility.getInstance().parseDecimal(latitudeto);
                bookingDataParcelable.lngTo= Utility.getInstance().parseDecimal(longitudeto);
                setDestination();
            }
        }else  if (requestCode == Constants.STATE_LOGIN_CODE) {
            if(resultCode == RESULT_OK){
                initSession();
                if(canUseDeposit(depositData)){
                    bookingDataParcelable.payment = "3";
                    getTimeFee(false);
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
        else  if (requestCode == TIP_FOR_RIDER) {
            if(resultCode == RESULT_OK){
                bookingDataParcelable.tip = data.getStringExtra(Constants.INTENT_TIP_RIDER);
                if(bookingDataParcelable.tip!=null&&bookingDataParcelable.tip.equals("0")){
                    tvTip.setText(getResources().getString(R.string.tip_for_rider));
                    tvTip.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
                }
                else{
                    tvTip.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.tip));
                    tvTip.setTextColor(Utility.getColor(getResources(),R.color.black,null));
                }
                getTimeFee(false);
            }
            else if (resultCode == RESULT_CANCELED) {
            }
        }
        else if (requestCode == SELECT_PROMO) {
            if (resultCode == RESULT_OK) {
                promoCodeDataParcelable = data.getParcelableExtra(Constants.PROMO_CODE_PARCELABLE);
                if(promoCodeDataParcelable!=null) {
                    bookingDataParcelable.promoCode = promoCodeDataParcelable.title;
                    tvPromoCode.setText(bookingDataParcelable.promoCode);
                    tvPromoCode.setTextColor(Utility.getColor(getResources(),R.color.black,null));
                    if(blinkAnimObject!=null){
                        blinkAnimObject.cancel();
                        layoutPromo.setBackgroundColor(Utility.getColor(getResources(), R.color.white, null));
                    }
                    getTimeFee(true);
                }else{
                    icSuccessPromo.setVisibility(View.GONE);
                    icPromo.setVisibility(View.VISIBLE);
                    bookingDataParcelable.promoCode = "";
                    tvPromoCode.setText(getResources().getString(R.string.promo));
                    tvPromoCode.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
                    getTimeFee(false);
                    blinkAnimObject = Utility.getInstance().manageBlinkEffect(layoutPromo, Utility.getColor(getResources(), R.color.amber_100, null));
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void setDestination(){
        if(bookingDataParcelable.toPlace==null){
            tvTo.setText(bookingDataParcelable.to);
            bookingDataParcelable.toPlace="";
        }
        else{
            tvTo.setText(bookingDataParcelable.toPlace+", "+bookingDataParcelable.to);
        }

        if(canUseDeposit(depositData)){
            bookingDataParcelable.payment = "3";
            ivCashMethod.setImageResource(R.drawable.xml_border_circle_grey);
            ivInkiPayMethod.setImageResource(R.mipmap.ic_success);
        }
        getTimeFee(false);
    }

    public void disabledBookButton(String message){
        btnOrder.setBackgroundColor(Color.parseColor("#BBBBBB"));
        tvOrder.setText(message);
        btnOrder.setOnClickListener(null);
    }
    public void RequestFood(){
        if(session.isLogin()) {
            pDialog = new ProgressDialog(OrderFoodNew.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            disabledBookButton(getResources().getString(R.string.order));
            String api = Utility.getInstance().getTokenApi(OrderFoodNew.this);
//            bookingController.requestFood(bookingDataParcelable.bookingFoodDatas.restaurantData.getStore_id(), foodParams(), api, requestFoodInterface);
//            Log.d("id_restaurant", ">" + bookingDataParcelable.bookingFoodDatas.restaurantData.getStore_id() + "    " + foodParams());
            return;
        }else{
            Intent i = new Intent(OrderFoodNew.this, LoginActivity.class);
            startActivityForResult(i, Constants.STATE_LOGIN_CODE);
            showToast(getResources().getString(R.string.please_login));
        }
    }

    public void getTimeFee(boolean isTriggerFromPromoCode)
    {
        this.isTriggerFromPromoCode =  isTriggerFromPromoCode;
        if(call!=null){
            call.cancel();
        }
        renderPriceLoadingView();
        disabledBookButton(getResources().getString(R.string.order));
        String api = Utility.getInstance().getTokenApi(OrderFoodNew.this);
        Map<String,String> timeFeeParams = bookingController.timeFeeParams(bookingDataParcelable);
        bookingController.getTimeFee(timeFeeParams, api,timeFeeInterface);
        return;
    }

    TimeFeeInterface timeFeeInterface = new TimeFeeInterface() {
        @Override
        public void onSuccessGetTimeFee(TimeFeeCallback timeFeeCallback) {
            int sukses = timeFeeCallback.getSukses();
            if(sukses==2){
                bookingDataParcelable.originalPrice= timeFeeCallback.getOriginal_price();
                bookingDataParcelable.price = timeFeeCallback.getPrice();
                bookingDataParcelable.bookingFoodDatas.foodCost = Utility.getInstance().parseInteger(timeFeeCallback.getTotal_item_price());
                bookingDataParcelable.promoPrice = timeFeeCallback.getPromo_price();
                bookingDataParcelable.depositPaid = timeFeeCallback.getDeposit_paid();
                bookingDataParcelable.cashPaid = timeFeeCallback.getCash_paid();
                bookingDataParcelable.categoryVoucher= timeFeeCallback.getPromo_category_voucher();
                term_km = timeFeeCallback.getTerm_km();
                isDefaultPromo = timeFeeCallback.isDefault_promo();
                if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(term_km)) {
                    term_km = "(" + timeFeeCallback.getTerm_km() + " km )";
                }else{
                    term_km ="";
                }
                renderPriceLoadSuccessView();
                setPromoCode(timeFeeCallback);
                setEstimatePrice(timeFeeCallback);
                setDistance(timeFeeCallback);
                setPaymentInfo();
                initBookButton();
            }
            else if(sukses==4){
                renderPriceLoadSuccessView();
                initBookButton();
                Utility.getInstance().showSimpleAlertDialog(OrderFoodNew.this, getResources().getString(R.string.attention), timeFeeCallback.getPesan(),
                        "OK", positiveDismissTipDialogListener,
                        null, null, false);
            }else{
                renderPriceLoadSuccessView();
                initBookButton();
                Utility.getInstance().showSimpleAlertDialog(OrderFoodNew.this, getResources().getString(R.string.error), timeFeeCallback.getPesan(),
                        "OK", positiveDialogListener,
                        null, null, false);
            }
        }

        @Override
        public void onErrorGetTimefee(APIErrorCallback apiErrorCallback) {
            error_type = "getTimeFee";
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(OrderFoodNew.this, getResources().getString(R.string.relogin));
                } else {
                    renderPriceLoadErrorView();
                }
            }
        }

        @Override
        public void callCancel(Call<TimeFeeCallback> timeFeeCallbackCall) {
            call = timeFeeCallbackCall;
        }
    };

    private void setDistance(TimeFeeCallback timeFeeCallback) {
        DistanceData distanceData = timeFeeCallback.getDistance();
        distanceValue = distanceData.getValue();
        distanceText = distanceData.getText();

        bookingDataParcelable.distance = distanceText;
        tvDistance.setText(bookingDataParcelable.distance);
        tvDistance.setVisibility(View.VISIBLE);
        if (Double.parseDouble(distanceValue) > 25) {
            disabledBookButton(getResources().getString(R.string.distance_valid));
            bookingDataParcelable.promoCode = "";
        }
    }


    public DialogInterface.OnClickListener positiveDismissPromoDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            deleteCodePromo();
            dialogInterface.dismiss();
            getTimeFee(false);
        }
    };
    private void deleteCodePromo(){
        promoCodeDataParcelable = null;
        tvPrice.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
        icSuccessPromo.setVisibility(View.GONE);
        icPromo.setVisibility(View.VISIBLE);
        bookingDataParcelable.promoCode = "";
        tvPromoCode.setText(getResources().getString(R.string.promo));
        tvPromoCode.setTextColor(Utility.getColor(getResources(),R.color.grey_600,null));
    }
    private void setPaymentInfo(){
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
//                int total_fee = Utility.getInstance().parseInteger(bookingDataParcelable.price)+bookingDataParcelable.bookingFoodDatas.foodCost;
                tvTotalPrice.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.cashPaid));
            }
        }
    }

    public void renderPriceLoadingView(){
        tvPrice.setText(getResources().getString(R.string.calculate));
        tvPrice.setPaintFlags(tvPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        tvPriceAfterPromo.setVisibility(View.GONE);
    }
    public void renderPriceLoadSuccessView(){
    }
    public void renderPriceLoadErrorView(){
        tvPrice.setText(getResources().getString(R.string.failed));
        disabledBookButton(getResources().getString(R.string.failed_to_get_price_data));
    }


    private void setPromoCode(TimeFeeCallback timeFeeCallback) {
        if (timeFeeCallback.getKode_promo()!=null) {
            String priceText = "";
            if (bookingDataParcelable.price == null || bookingDataParcelable.price.equals("0")) {
                priceText = getResources().getString(R.string.free);
            } else {
                priceText = "Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price);
            }
            if(isTriggerFromPromoCode) {
                Utility.getInstance().showSimpleAlertDialog(OrderFoodNew.this, getResources().getString(R.string.promo_code_right2), timeFeeCallback.getPesan() +
                                "\nHarga awal: Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.originalPrice) + "\nHarga akhir: " + priceText + " " + term_km,
                        "OK", positiveDialogListener,
                        null, null, false);
            }
            bookingDataParcelable.promoCode = timeFeeCallback.getKode_promo();
            tvPromoCode.setVisibility(View.VISIBLE);
            icSuccessPromo.setVisibility(View.VISIBLE);
            icPromo.setVisibility(View.GONE);
            tvPromoCode.setText(bookingDataParcelable.promoCode);
            tvPromoCode.setTextColor(Utility.getColor(getResources(),R.color.black,null));

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
    private void showPromoErrorAlertDialog(String pesan){
       deleteCodePromo();
        Utility.getInstance().showSimpleAlertDialog(OrderFoodNew.this, getResources().getString(R.string.promo_code_wrong), pesan,
                "OK", positiveDialogListener,
                null, null, false);

    }
    public void initBookButton(){
        if(session.isLogin()) {
            tvOrder.setText(getResources().getString(R.string.order));
        }else{
            tvOrder.setText(getResources().getString(R.string.not_login)+", "+getResources().getString(R.string.click_here));
        }
        btnOrder.setBackgroundColor(Utility.getColor(getResources(), R.color.colorPrimaryDark, null));
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = tvTo.getText().toString();
                if(to.equals(getResources().getString(R.string.place_name))){
                    show_place_valid_listener();
                }else{
                    if(bookingDataParcelable.bookingFoodDatas.foodCost!=0){
                        if(onGoingBookingInProgressMembers.size()!=0){
                            Utility.getInstance().showSimpleAlertDialog(OrderFoodNew.this, getResources().getString(R.string.attention), getResources().getString(R.string.have_booking),
                                    getResources().getString(R.string.order),positiveBookDialogListener,
                                    getResources().getString(R.string.cancel), negativeBookDialogListener, false);
                        }else {
                            if (Double.parseDouble(distanceValue) < 25) {
                                RequestFood();
                            } else {
                                Toast.makeText(OrderFoodNew.this, getResources().getString(R.string.distance_valid), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else{
                        Toast.makeText(OrderFoodNew.this, getResources().getString(R.string.no_have_food_menu), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public DialogInterface.OnClickListener positiveBookDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            RequestFood();
        }
    };

    public DialogInterface.OnClickListener negativeBookDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.BOOKING_DATA_PARCELABLE, bookingDataParcelable);
        OrderFoodNew.this.setResult(OrderFoodNew.this.RESULT_OK,returnIntent);

        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCheckDeposit();
        if(readyorder == true){
            initBookButton();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }

//    @Override
//    public void onGPSLocationReceived(Location locationData) {
//        if(bookingDataParcelable.latTo==0.0||bookingDataParcelable.lngTo ==0.0) {
//            bookingDataParcelable.latTo = locationData.getLatitude();
//            bookingDataParcelable.lngTo = locationData.getLongitude();
//        }
//    }
//

}