package com.pmberjaya.indotiki.app.bookingData.bookingFragment;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by edwin on 09/11/2016.
 */

public class BookingTransportFragment extends Fragment {
    private TextView tv_from_place;
    private TextView tv_from_detail;
    private TextView tv_to_place;
    private TextView tv_to_detail;
    private TextView tv_location;
    private TextView tv_request_time;
    private TextView tv_distance;
    private TextView tv_title;
    private TextView tv_accept_time;
    private TextView tvBookingDetail;

    private LinearLayout deposit_payment_layout;
    private TextView tv_total_paid;
    private TextView tv_total_paid_title;
    private TextView tv_transport_fee;
    private TextView tv_pay_with_deposit;
    private TextView tv_total_fee;
    private TextView tv_tip_for_rider;
    private LinearLayout tip_layout;
    private TextView tv_transport_fee_discount;
    private LinearLayout promo_code_layout;
    private TextView tv_promo_code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.booking_data_transport_fragment, container, false);
        renderView(rootView);
        setData();
        return rootView;
    }
    private void renderView(View rootView){
        deposit_payment_layout = (LinearLayout)rootView.findViewById(R.id.deposit_payment_layout);
        tv_tip_for_rider = (TextView)rootView.findViewById(R.id.tip_fee);
        tv_from_place = (TextView) rootView.findViewById(R.id.from);
        tv_from_detail = (TextView) rootView.findViewById(R.id.fromDetail);
        tv_to_place = (TextView) rootView.findViewById(R.id.to);
        tv_to_detail = (TextView) rootView.findViewById(R.id.toDetail);
        tv_location = (TextView) rootView.findViewById(R.id.location_detail);
        tv_total_paid= (TextView) rootView.findViewById(R.id.tvTotalPaid);
        tv_total_paid_title= (TextView) rootView.findViewById(R.id.tvTotalPaidTitle);
        tv_transport_fee= (TextView) rootView.findViewById(R.id.transportFee);
        tv_request_time = (TextView) rootView.findViewById(R.id.request_time);
        tv_accept_time= (TextView) rootView.findViewById(R.id.accept_time);
        tv_distance = (TextView) rootView.findViewById(R.id.distance);
        tv_title = (TextView) rootView.findViewById(R.id.tvTitle);
        tv_transport_fee_discount = (TextView) rootView.findViewById(R.id.transportFeeDiscount);
        tv_pay_with_deposit= (TextView) rootView.findViewById(R.id.tv_pay_with_deposit);
        tv_total_fee= (TextView) rootView.findViewById(R.id.tv_total_fee);
        promo_code_layout = (LinearLayout)rootView.findViewById(R.id.promo_code_layout);
        tv_promo_code = (TextView) rootView.findViewById(R.id.promo_code);
        Typeface custom_font = Typeface.createFromAsset(getResources().getAssets(),  "fonts/BenchNine-Bold.ttf");
        tvBookingDetail = (TextView) rootView.findViewById(R.id.tvBookingDetail);
        tip_layout = (LinearLayout)rootView.findViewById(R.id.tip_fee_layout);
        tv_title.setTypeface(custom_font);
        tvBookingDetail.setTypeface(custom_font);
    }

    BookingDataParcelable bookingDataParcelable;
    private void setData(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bookingDataParcelable= bundle.getParcelable(Constants.BOOKING_DATA_PARCELABLE);
            renderBookingCommonInfo();
            setTotalPaidInfo();
        }
    }

    private void setTotalPaidInfo() {
        double total_fee = Utility.getInstance().parseDecimal(bookingDataParcelable.price);
        double total_paid_cash = Utility.getInstance().parseDecimal(bookingDataParcelable.cashPaid);
        if(bookingDataParcelable.promoCode!=null) {
            if(Utility.getInstance().parseDecimal(bookingDataParcelable.price)<=0){
                tv_transport_fee_discount.setText(getResources().getString(R.string.free));
            }else{
                tv_transport_fee_discount.setText("Rp. " + Utility.getInstance().convertPrice(bookingDataParcelable.price));
            }
            tv_transport_fee_discount.setVisibility(View.VISIBLE);
            tv_transport_fee.setVisibility(View.VISIBLE);
            tv_transport_fee.setPaintFlags(tv_transport_fee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_promo_code.setText(getResources().getString(R.string.promo_code)+" : "+bookingDataParcelable.promoCode);
            promo_code_layout.setVisibility(View.VISIBLE);
        }else{
            promo_code_layout.setVisibility(View.GONE);
        }
        tv_transport_fee.setText("Rp. " + Utility.getInstance().convertPrice(Double.parseDouble(bookingDataParcelable.originalPrice)));
        tv_total_fee.setText("Rp. "+Utility.getInstance().convertPrice(total_fee));
        setPaymentInfo(total_paid_cash);

    }

    private void renderBookingCommonInfo() {
        if(bookingDataParcelable.promoCode!=null) {
            tv_title.setText(getResources().getString(R.string.promo_booking));
            tv_title.setTextColor(Utility.getColor(getResources(), R.color.colorPrimaryDark, null));
        }else{
            tv_title.setText(getResources().getString(R.string.regular_booking));
            tv_title.setTextColor(Utility.getColor(getResources(), R.color.black, null));
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
        if(bookingDataParcelable.bookingTransportDatas.locationDetail!=null&&!bookingDataParcelable.bookingTransportDatas.locationDetail.equals("")) {
            tv_location.setText(bookingDataParcelable.bookingTransportDatas.locationDetail);
        }else{
            tv_location.setVisibility(View.GONE);
        }
        tv_request_time.setText(bookingDataParcelable.requestTime);
        tv_distance.setText("("+bookingDataParcelable.distance+")");
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
}
