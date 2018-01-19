package com.pmberjaya.indotiki.app.bookingData.bookingFragment;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.app.others.StandardImageItem;

/**
 * Created by edwin on 09/11/2016.
 */

public class BookingCourierFragment extends Fragment {
    private TextView tv_from_place;
    private TextView tv_from_detail;
    private TextView tv_to_place;
    private TextView tv_to_detail;
    private TextView tv_request_time;
    private TextView tv_accept_time;
    private TextView tvBookingDetail;
    private TextView tv_distance;
    private ImageView ivItemPhoto;
    private TextView tv_location_detail_from;
    private LinearLayout location_detail_from_layout;

    private TextView tv_fullname_from;
    private TextView tv_phone_from;
    private TextView tv_location_detail_to;
    private LinearLayout location_detail_to_layout;
    private TextView tv_fullname_to;
    private TextView tv_phone_to;
    private TextView tv_items_to_deliver;
    private TextView tv_delivery_fee;
    private TextView tv_title;
    private TextView tvItemsToDeliver;
    private LinearLayout deposit_payment_layout;
    private TextView tv_total_paid;
    private TextView tv_pay_with_deposit;
    private TextView tv_total_fee;
    private TextView tv_tip_for_rider;
    private LinearLayout tip_layout;
    private TextView tv_delivery_fee_discount;
    private LinearLayout promo_code_layout;
    private TextView tv_promo_code;
    private String deposit_paid;
    private TextView tv_total_paid_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.booking_data_courier_fragment, container, false);
        renderView(rootView);
        setData();
        return rootView;
    }
    private void renderView(View rootView){
        deposit_payment_layout = (LinearLayout)rootView.findViewById(R.id.deposit_payment_layout);
        tv_from_place = (TextView) rootView.findViewById(R.id.from);
        tv_from_detail = (TextView) rootView.findViewById(R.id.fromDetail);
        tv_to_place = (TextView) rootView.findViewById(R.id.to);
        tv_to_detail = (TextView) rootView.findViewById(R.id.toDetail);
        tv_total_paid= (TextView) rootView.findViewById(R.id.tvTotalPaid);
        tv_total_paid_title= (TextView) rootView.findViewById(R.id.tvTotalPaidTitle);
        tv_request_time = (TextView) rootView.findViewById(R.id.request_time);
        tv_accept_time = (TextView) rootView.findViewById(R.id.accept_time);
        tv_distance = (TextView) rootView.findViewById(R.id.distance);
        ivItemPhoto = (ImageView) rootView.findViewById(R.id.ivItemPhoto);
        tv_location_detail_from = (TextView) rootView.findViewById(R.id.location_detail_from);
        tv_fullname_from = (TextView) rootView.findViewById(R.id.fullname_from);
        tv_phone_from = (TextView) rootView.findViewById(R.id.phone_from);
        tv_location_detail_to = (TextView) rootView.findViewById(R.id.location_detail_to);
        tv_fullname_to = (TextView) rootView.findViewById(R.id.fullname_to);
        tv_phone_to = (TextView) rootView.findViewById(R.id.phone_to);
        tv_items_to_deliver = (TextView) rootView.findViewById(R.id.items_to_deliver);
        tv_delivery_fee= (TextView) rootView.findViewById(R.id.delivery_fee);
        tv_pay_with_deposit= (TextView) rootView.findViewById(R.id.tv_pay_with_deposit);
        tv_total_fee= (TextView) rootView.findViewById(R.id.tv_total_fee);
        tv_delivery_fee_discount= (TextView) rootView.findViewById(R.id.delivery_fee_discount);
        tv_tip_for_rider = (TextView)rootView.findViewById(R.id.tip_fee);
        tip_layout = (LinearLayout)rootView.findViewById(R.id.tip_fee_layout);
        tv_title = (TextView) rootView.findViewById(R.id.tvTitle);
        tvBookingDetail = (TextView) rootView.findViewById(R.id.tvBookingDetail);
        tvItemsToDeliver = (TextView) rootView.findViewById(R.id.tvItemsToDeliver);
        promo_code_layout = (LinearLayout)rootView.findViewById(R.id.promo_code_layout);
        location_detail_from_layout = (LinearLayout)rootView.findViewById(R.id.location_detail_from_layout);
        location_detail_to_layout = (LinearLayout)rootView.findViewById(R.id.location_detail_to_layout);
        tv_promo_code = (TextView) rootView.findViewById(R.id.promo_code);
        Typeface custom_font = Typeface.createFromAsset(getResources().getAssets(),  "fonts/BenchNine-Bold.ttf");
        tvBookingDetail.setTypeface(custom_font);
        tvItemsToDeliver.setTypeface(custom_font);
        tv_title.setTypeface(custom_font);
    }
    BookingDataParcelable bookingDataParcelable;
    private void setData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bookingDataParcelable = bundle.getParcelable(Constants.BOOKING_DATA_PARCELABLE);
            renderBookingCommonInfo();
            setTotalPaidInfo();
        }
    }
    private View.OnClickListener intentPhotoDetail = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(bookingDataParcelable.bookingCourierDatas.item_photo!=null){
                Intent intent = new Intent(getActivity(),StandardImageItem.class);
                intent.putExtra("item",bookingDataParcelable.bookingCourierDatas.item_photo);
                intent.putExtra("activity","courier_complete");
                startActivity(intent);
            }
        }
    };
    private void renderBookingCommonInfo(){
        if(bookingDataParcelable.promoCode!=null) {
            tv_title.setText(getResources().getString(R.string.promo_booking));
            tv_title.setTextColor(Utility.getColor(getResources(), R.color.colorPrimaryDark, null));
        }else{
            tv_title.setText(getResources().getString(R.string.regular_booking));
            tv_title.setTextColor(Utility.getColor(getResources(), R.color.black, null));
        }

        if (bookingDataParcelable.bookingCourierDatas.item_photo != null && !bookingDataParcelable.bookingCourierDatas.item_photo.equals("")) {
            Glide.with(getActivity()).load(bookingDataParcelable.bookingCourierDatas.item_photo).apply(Utility.getInstance().setGlideOptions(300,400)).into(ivItemPhoto);
//            PicassoLoader.loadImage(getActivity(), bookingDataParcelable.bookingCourierDatas.item_photo, ivItemPhoto);
            ivItemPhoto.setOnClickListener(intentPhotoDetail);
        } else {
            PicassoLoader.loadImageFail(getActivity(), ivItemPhoto);
        }

        if (bookingDataParcelable.fromPlace == null) {
            tv_from_place.setVisibility(View.GONE);
        } else {
            tv_from_place.setText(bookingDataParcelable.fromPlace);
        }
        if (bookingDataParcelable.toPlace == null) {
            tv_to_place.setVisibility(View.GONE);
        } else {
            tv_to_place.setText(bookingDataParcelable.toPlace);
        }
        if (bookingDataParcelable.tip==null||bookingDataParcelable.tip.equals("0")){
            tip_layout.setVisibility(View.GONE);
//            tv_total_paid_title.setText(getResources().getString(R.string.total_paid_in_cash));
        }else {
            tip_layout.setVisibility(View.VISIBLE);
            tv_tip_for_rider.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.tip) );

//            tv_total_paid_title.setText(getResources().getString(R.string.total_paid_in_cash)+" + "+getResources().getString(R.string.tip));
        }

        tv_from_detail.setText(bookingDataParcelable.from);
        tv_to_detail.setText(bookingDataParcelable.to);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.bookingCourierDatas.location_detail_sender)){
            tv_location_detail_from.setText(bookingDataParcelable.bookingCourierDatas.location_detail_sender);
        }else{
            location_detail_from_layout.setVisibility(View.GONE);
        }

        tv_fullname_from.setText(bookingDataParcelable.bookingCourierDatas.name_sender);
        tv_phone_from.setText(bookingDataParcelable.bookingCourierDatas.phone_sender);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.bookingCourierDatas.location_detail_receiver)){
            tv_location_detail_to.setText(bookingDataParcelable.bookingCourierDatas.location_detail_receiver);
        }else{
            location_detail_to_layout.setVisibility(View.GONE);
        }
        tv_fullname_to.setText(bookingDataParcelable.bookingCourierDatas.name_receiver);
        tv_phone_to.setText(bookingDataParcelable.bookingCourierDatas.phone_receiver);
        tv_items_to_deliver.setText(bookingDataParcelable.bookingCourierDatas.item);

        tv_request_time.setText(bookingDataParcelable.requestTime);
        tv_accept_time.setText(bookingDataParcelable.acceptTime);
        tv_distance.setText("("+bookingDataParcelable.distance+")");

    }
    private void setPaymentInfo(double total_paid_cash) {
        if (total_paid_cash == 0 && bookingDataParcelable.payment.equals("3")) {
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
    private void setTotalPaidInfo(){
        double total_fee = Utility.getInstance().parseDecimal(bookingDataParcelable.price);
        double total_paid = Utility.getInstance().parseDecimal(bookingDataParcelable.cashPaid);

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
        setPaymentInfo(total_paid);
    }
}
