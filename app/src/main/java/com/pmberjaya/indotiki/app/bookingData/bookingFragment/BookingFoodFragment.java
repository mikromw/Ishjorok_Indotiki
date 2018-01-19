package com.pmberjaya.indotiki.app.bookingData.bookingFragment;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.deposit.DepositConfirmationEvidenceImageDetail;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by edwin on 09/11/2016.
 */

public class BookingFoodFragment extends Fragment {
    private TextView tv_from_place;
    private TextView tv_from_detail;
    private TextView tv_to_place;
    private TextView tv_to_detail;
    private TextView tv_request_time;
    private TextView tvBookingDetail;
    private TextView tv_distance;
    private TextView tv_delivery_fee;
    private TextView tv_title;
    private TextView tvItemsToDeliver;
    private LinearLayout foodData;
    private TextView tv_location;
    private TextView tv_total_harga_makanan_top;
    private TextView tv_total_harga_makanan;
    private TextView tv_pay_with_deposit;
    private LinearLayout deposit_payment_layout;
    private TextView payment_text;
    private TextView tv_total_fee;
    private TextView tv_total_paid;
    private TextView tv_total_paid_title;
    private TextView tv_delivery_fee_discount;
    private TextView tv_tip_for_rider;
    private LinearLayout tip_layout;
    private LinearLayout promo_code_layout;
    private TextView tv_promo_code;
    private CardView layout_price_changes;
    private ImageView iv_struk_pembayaran;
    private TextView tv_old_food_prices;
    private TextView tv_new_food_prices;
    private String deposit_paid;
    private String foodReceipt;
    private TextView tvPriceChangesTitle;
    private LinearLayout total_food_price_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.booking_data_food_fragment, container, false);
        renderView(rootView);
        setData();
        return rootView;
    }
    private void renderView(View rootView){
        foodData = (LinearLayout)rootView.findViewById(R.id.data);
        deposit_payment_layout = (LinearLayout)rootView.findViewById(R.id.deposit_payment_layout);
        tv_from_place = (TextView) rootView.findViewById(R.id.from);
        tv_from_detail = (TextView) rootView.findViewById(R.id.fromDetail);
        tv_to_place = (TextView) rootView.findViewById(R.id.to);
        tv_to_detail = (TextView) rootView.findViewById(R.id.toDetail);
        tv_location = (TextView) rootView.findViewById(R.id.location_detail);
        tv_total_paid_title= (TextView) rootView.findViewById(R.id.tvTotalPaidTitle);
        tv_total_paid= (TextView) rootView.findViewById(R.id.tvTotalPaid);
        tv_request_time = (TextView) rootView.findViewById(R.id.request_time);
        tv_distance = (TextView) rootView.findViewById(R.id.distance);
        payment_text= (TextView) rootView.findViewById(R.id.payment_text);
        tv_pay_with_deposit= (TextView) rootView.findViewById(R.id.tv_pay_with_deposit);
        tv_total_fee= (TextView) rootView.findViewById(R.id.tv_total_fee);
        tip_layout = (LinearLayout)rootView.findViewById(R.id.tip_fee_layout);
        promo_code_layout = (LinearLayout)rootView.findViewById(R.id.promo_code_layout);
        total_food_price_layout = (LinearLayout)rootView.findViewById(R.id.total_food_price_layout);

        tv_total_harga_makanan_top = (TextView) rootView.findViewById(R.id.total_harga_makanan_top);
        tv_total_harga_makanan = (TextView) rootView.findViewById(R.id.total_harga_makanan);
        tv_delivery_fee = (TextView) rootView.findViewById(R.id.delivery_fee);
        tv_delivery_fee_discount = (TextView) rootView.findViewById(R.id.delivery_fee_discount);
        tv_promo_code = (TextView) rootView.findViewById(R.id.promo_code);
        layout_price_changes = (CardView) rootView.findViewById(R.id.layout_price_changes);
        iv_struk_pembayaran = (ImageView) rootView.findViewById(R.id.iv_strruk_pembayaran);
        tv_old_food_prices = (TextView) rootView.findViewById(R.id.tv_old_food_price);
        tv_new_food_prices = (TextView) rootView.findViewById(R.id.tv_new_food_price);

        tv_title = (TextView) rootView.findViewById(R.id.tvTitle);
        tv_tip_for_rider = (TextView)rootView.findViewById(R.id.tip_fee);
        tvBookingDetail = (TextView) rootView.findViewById(R.id.tvBookingDetail);
        tvItemsToDeliver = (TextView) rootView.findViewById(R.id.tvItemsToDeliver);
        tvPriceChangesTitle = (TextView) rootView.findViewById(R.id.tvPriceChangesTitle);
        Typeface custom_font = Typeface.createFromAsset(getResources().getAssets(),  "fonts/BenchNine-Bold.ttf");
        tvBookingDetail.setTypeface(custom_font);
        tvItemsToDeliver.setTypeface(custom_font);
        tv_title.setTypeface(custom_font);
        tvPriceChangesTitle.setTypeface(custom_font);
    }

    BookingDataParcelable bookingDataParcelable;
    private void setData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bookingDataParcelable= bundle.getParcelable(Constants.BOOKING_DATA_PARCELABLE);
            renderBookingCommonInfo();
            renderBookingFoodInfo();
            setTotalPaidInfo();
            setPriceChangesDataView();
        }
    }




    private void renderBookingCommonInfo() {
        if(bookingDataParcelable.promoCode!=null) {
            tv_title.setText(getResources().getString(R.string.promo_booking));
            tv_title.setTextColor(Utility.getColor(getResources(), R.color.colorPrimaryDark, null));
        }else{
            tv_title.setText(getResources().getString(R.string.regular_booking));
            tv_title.setTextColor(Utility.getColor(getResources(), R.color.black, null));
        }

        if (bookingDataParcelable.toPlace == null) {
            tv_to_place.setVisibility(View.GONE);
        } else {
            tv_to_place.setText(bookingDataParcelable.toPlace);
        }
        if (bookingDataParcelable.tip==null||bookingDataParcelable.tip.equals("0")){
            tip_layout.setVisibility(View.GONE);
        }else {
            tv_tip_for_rider.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.tip));
            tip_layout.setVisibility(View.VISIBLE);
        }
        tv_request_time.setText(bookingDataParcelable.requestTime);
        tv_distance.setText("("+bookingDataParcelable.distance+")");
//        tv_from_detail.setText(bookingDataParcelable.bookingFoodDatas.restaurantData.getStore_address());
        tv_to_detail.setText(bookingDataParcelable.to);
        if (bookingDataParcelable.bookingFoodDatas.locationDetail == null) {
            tv_location.setVisibility(View.GONE);
        } else {
            tv_location.setText(bookingDataParcelable.bookingFoodDatas.locationDetail);
        }
    }

    private void setTotalPaidInfo() {
        double total_fee = Utility.getInstance().parseDecimal(bookingDataParcelable.price)+bookingDataParcelable.bookingFoodDatas.foodCost;
        double total_paid_cash = Double.parseDouble(bookingDataParcelable.cashPaid);
        if(bookingDataParcelable.promoCode!=null) {
            if(Utility.getInstance().parseDecimal(bookingDataParcelable.price)<=0){
                tv_delivery_fee_discount.setText(getResources().getString(R.string.free));
            }else{
                tv_delivery_fee_discount.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
            }
            tv_delivery_fee_discount.setVisibility(View.VISIBLE);
            tv_delivery_fee.setVisibility(View.VISIBLE);
            tv_delivery_fee.setPaintFlags(tv_delivery_fee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_promo_code.setText(getResources().getString(R.string.promo_code)+" : "+bookingDataParcelable.promoCode);
            promo_code_layout.setVisibility(View.VISIBLE);
        }else{
            promo_code_layout.setVisibility(View.GONE);
        }
        tv_delivery_fee.setText("Rp. " + Utility.getInstance().convertPrice(Double.parseDouble(bookingDataParcelable.originalPrice)));
        tv_total_fee.setText("Rp. "+Utility.getInstance().convertPrice(total_fee));
        setPaymentInfo(total_paid_cash);
    }
    private void setPaymentInfo(double total_paid_cash) {
        if (total_paid_cash <= 0&&bookingDataParcelable.payment.equals("3")) {
            tv_total_paid.setText("Rp. " + Utility.getInstance().convertPrice(String.valueOf(bookingDataParcelable.depositPaid)));
            tv_total_paid_title.setText(getResources().getString(R.string.total_paid_in_deposit));
            tv_total_paid_title.setTextColor(Utility.getColor(getResources(), R.color.colorPrimaryDark, null));
            deposit_payment_layout.setVisibility(View.GONE);
        } else if (total_paid_cash > 0&&bookingDataParcelable.payment.equals("3")){
            tv_pay_with_deposit.setText("- Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.depositPaid));
            tv_total_paid_title.setText(getResources().getString(R.string.pay_with_cash));
            tv_total_paid.setText("Rp. " + Utility.getInstance().convertPrice(String.valueOf(total_paid_cash)));
            deposit_payment_layout.setVisibility(View.VISIBLE);
        }else{
            tv_total_paid.setText("Rp. " + Utility.getInstance().convertPrice(String.valueOf(total_paid_cash)));
            tv_total_paid_title.setText(getResources().getString(R.string.total_paid_in_cash));
            deposit_payment_layout.setVisibility(View.GONE);
        }
    }

    public void renderBookingFoodInfo(){
        if(bookingDataParcelable.bookingFoodDatas.foodItemTempDatas!=null) {
            for (int i = 0; i <bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.size(); i++) {
                View child = getActivity().getLayoutInflater().inflate(R.layout.booking_food_menu_list_adapter, null);
                TextView tv_nama_makanan = (TextView) child.findViewById(R.id.nama_makanan);
                TextView tv_harga_makanan = (TextView) child.findViewById(R.id.harga_makanan);
                TextView tv_menu_quantity = (TextView) child.findViewById(R.id.tv_menu_quantity);
                TextView tv_total_harga = (TextView) child.findViewById(R.id.total_harga);
                TextView tv_note = (TextView) child.findViewById(R.id.tv_note);
                LinearLayout notes_layout = (LinearLayout) child.findViewById(R.id.notes_layout);
                tv_note.setEnabled(false);
                if (bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getDescription_note()!=null) {
                    tv_note.setText(bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getDescription_note());
                    notes_layout.setVisibility(View.VISIBLE);
                }
                tv_harga_makanan.setText("@ Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getPrice()));
                tv_nama_makanan.setText(bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getLabel());
                tv_menu_quantity.setText("Qty: " + bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getQuantity());
                int total_price = Integer.parseInt(bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getPrice()) * bookingDataParcelable.bookingFoodDatas.foodItemTempDatas.get(i).getQuantity() ;
                tv_total_harga.setText(Utility.getInstance().convertPrice(total_price));
                foodData.addView(child);
            }
        }
        if(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas!=null){
            for(int i = 0; i<bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.size();i++) {
                View child = getActivity().getLayoutInflater().inflate(R.layout.booking_food_menu_list_adapter,null);
                TextView tv_nama_makanan = (TextView) child.findViewById(R.id.nama_makanan);
                TextView tv_harga_makanan = (TextView) child.findViewById(R.id.harga_makanan);
                TextView tv_menu_quantity = (TextView) child.findViewById(R.id.tv_menu_quantity);
                TextView tv_total_harga = (TextView) child.findViewById(R.id.total_harga);
                TextView tv_note = (TextView)child.findViewById(R.id.tv_note);
                LinearLayout notes_layout = (LinearLayout)child.findViewById(R.id.notes_layout);
                tv_harga_makanan.setText("@ Rp " + Utility.getInstance().convertPrice(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getPrice()));
                tv_nama_makanan.setText(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getLabel());
                tv_menu_quantity.setText("Qty: "+bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getQuantity());
                int total_price = Integer.parseInt(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getPrice() )*bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getQuantity();
                tv_total_harga.setText(Utility.getInstance().convertPrice(total_price));
                if(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getDescription_note()!=null&&!bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getDescription_note().equals("null")){
                    tv_note.setText(bookingDataParcelable.bookingFoodDatas.foodItemTempManuallyDatas.get(i).getDescription_note());
                    notes_layout.setVisibility(View.VISIBLE);
                }
                foodData.addView(child);
            }
        }
        tv_total_harga_makanan.setText(Utility.getInstance().convertPrice(bookingDataParcelable.bookingFoodDatas.foodCost));
    }

    public void setPriceChangesData(String oldFoodPrice, String newFoodPrice, String foodReceipt, String cashPaid, String depositPaid) {

        bookingDataParcelable.bookingFoodDatas.total_item_price_before = oldFoodPrice;
        bookingDataParcelable.bookingFoodDatas.food_receipt = foodReceipt;
        bookingDataParcelable.bookingFoodDatas.foodCost = Utility.getInstance().parseInteger(newFoodPrice);
        bookingDataParcelable.cashPaid = cashPaid;
        bookingDataParcelable.depositPaid = depositPaid;
        setPriceChangesDataView();
        setTotalPaidInfo();
    }
    private void setPriceChangesDataView() {
        if (bookingDataParcelable.bookingFoodDatas.food_receipt != null) {
            layout_price_changes.setVisibility(View.VISIBLE);
            total_food_price_layout.setVisibility(View.GONE);
        }else {
            layout_price_changes.setVisibility(View.GONE);
            total_food_price_layout.setVisibility(View.VISIBLE);
        }

        tv_old_food_prices.setText("Rp " + Utility.getInstance().convertPrice(bookingDataParcelable.bookingFoodDatas.total_item_price_before));
        tv_new_food_prices.setText("Rp " + Utility.getInstance().convertPrice(bookingDataParcelable.bookingFoodDatas.foodCost));
        Glide.with(this).load(bookingDataParcelable.bookingFoodDatas.food_receipt)
                .apply(Utility.getInstance().setGlideOptions(100, 100))
                .into(iv_struk_pembayaran);
        iv_struk_pembayaran.setOnClickListener(seeDetailReceipt);
        tv_total_harga_makanan_top.setText("Rp. "+Utility.getInstance().convertPrice(bookingDataParcelable.bookingFoodDatas.foodCost));
    }
    private View.OnClickListener seeDetailReceipt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (bookingDataParcelable.bookingFoodDatas.food_receipt != null) {
                Intent intent = new Intent(getContext(), DepositConfirmationEvidenceImageDetail.class);
                intent.putExtra("payment_receipt", bookingDataParcelable.bookingFoodDatas.food_receipt);
                startActivity(intent);
            }
        }
    };
}