package com.pmberjaya.indotiki.app.bookingData.bookingProgress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingReasonCancelInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.bookingData.BookingReasonCancelData;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 24/02/2017.
 */

public class BookingReasonCancel extends BaseActivity implements BookingReasonCancelInterface {

    private RelativeLayout error_layout;
    private LinearLayout loadinglayout;
    private LinearLayout bt_try_again;
    private LinearLayout cancellayout;
    private RecyclerView booking_cancel_reason_list;
    private BookingReasonCancelAdapter bookingReasonCancelAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_reason_cancel_activity);
        BookingController.getInstance(BookingReasonCancel.this).setInterface(this);
        bindToolbar();
        bindViews();
        getReasonCancelBooking();
    }


    private void bindViews() {
        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        error_layout= (RelativeLayout) findViewById(R.id.layoutError);
        bt_try_again = (LinearLayout) findViewById(R.id.btnError);
        bt_try_again.setOnClickListener(try_again_listener);
        cancellayout = (LinearLayout) findViewById(R.id.cancellayout);
        cancellayout.setOnClickListener(cancelBooking);
        booking_cancel_reason_list = (RecyclerView) findViewById(R.id.reason_cancel_list);
        booking_cancel_reason_list.setHasFixedSize(true);
        booking_cancel_reason_list.setNestedScrollingEnabled(false);
        booking_cancel_reason_list.addItemDecoration(new SimpleDividerItemDecoration(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        booking_cancel_reason_list.setLayoutManager(linearLayoutManager);    }

    private View.OnClickListener cancelBooking = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bookingReasonCancelAdapter!=null) {
                if(bookingReasonCancelAdapter.getReason()==null){
                    Toast.makeText(BookingReasonCancel.this, getResources().getString(R.string.select_reason), Toast.LENGTH_SHORT).show();
                }else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("reason",bookingReasonCancelAdapter.getReason());
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
            }
        }
    };
    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            renderloadingView();
            getReasonCancelBooking();
        }
    };

    public void SetListViewAdapter(List<BookingReasonCancelData> bookingReasonCancelDatas) {
        if(bookingReasonCancelAdapter==null){
            booking_cancel_reason_list.setVisibility(View.VISIBLE);
            bookingReasonCancelAdapter = new BookingReasonCancelAdapter(BookingReasonCancel.this,bookingReasonCancelDatas);
            booking_cancel_reason_list.setAdapter(bookingReasonCancelAdapter);
        }
    }
    private void getReasonCancelBooking() {
            String api = Utility.getInstance().getTokenApi(BookingReasonCancel.this);
            BookingController.getInstance(BookingReasonCancel.this).getBookingReasonCancel(api);
            return;
    }
    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        cancellayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        cancellayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
    }

    public void renderSuccessView(){
        loadinglayout.setVisibility(View.GONE);
        cancellayout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
    }
    private void bindToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.cancel_reason));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSuccessGetBookingReasonCancel(BaseGenericCallback<List<BookingReasonCancelData>> baseGenericCallback) {
        int sukses = baseGenericCallback.getSukses();
        if(sukses==2){
            List<BookingReasonCancelData> bookingReasonCancelDatas = baseGenericCallback.getData();
            if (bookingReasonCancelDatas!= null) {
            } else {
                bookingReasonCancelDatas = new ArrayList<>();
            }
            renderSuccessView();
            BookingReasonCancelData bookingReasonCancelData = new BookingReasonCancelData();
            bookingReasonCancelData.setReason("Masukkan alasan lain");
            bookingReasonCancelDatas.add(bookingReasonCancelData);
            SetListViewAdapter(bookingReasonCancelDatas);
        }
        else {
            renderErrorView();
        }
    }

    @Override
    public void onErrorGetBookingReasonCancel(APIErrorCallback apiErrorCallback) {
        if(apiErrorCallback.getError()!=null) {
            if (apiErrorCallback.getError().equals("Invalid API key ")) {
                Log.d("Unauthorized", "Jalannn");
                SessionManager session = new SessionManager(this);
                session.logoutUser();
                Utility.getInstance().showInvalidApiKeyAlert(this, getResources().getString(R.string.relogin));
            } else {
                renderErrorView();
            }
        }
    }
}
