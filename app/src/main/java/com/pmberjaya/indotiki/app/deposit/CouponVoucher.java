package com.pmberjaya.indotiki.app.deposit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.promo.MyPromoActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by user on 7/6/2017.
 */

public class CouponVoucher  extends BaseActivity {
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    private EditText ed_voucher_code;
    private String coupon_code;
    private Button btn_redeem;
    private String imei;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_voucher);
        userController = UserController.getInstance(CouponVoucher.this);
        initToolbar();
        renderView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.voucher));
        }
    }

    private String getImeiDevice(){
        TelephonyManager tManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tManager.getDeviceId();
        return imei;
    }

    private void renderView(){
        imei = getImeiDevice();
        ed_voucher_code = (EditText)findViewById(R.id.ed_voucher_code);
        btn_redeem = (Button) findViewById(R.id.btn_voucher_redeem);

        btn_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon_code = ed_voucher_code.getText().toString();
                if (coupon_code == null || coupon_code.equals("")) {
                    Toast.makeText(CouponVoucher.this, getResources().getString(R.string.please_enter_coupon_code), Toast.LENGTH_SHORT).show();
                } else {
                    sendvoucherCode();
                }
            }
        });
    }

    private void sendvoucherCode()
    {
        String api = Utility.getInstance().getTokenApi(CouponVoucher.this);
        pDialog = ProgressDialog.show(CouponVoucher.this, "", "loading...");
        userController.sendVouchercode(imei, coupon_code,api, sendVoucherCodeInterface);
        return;
    }

    BaseGenericInterface sendVoucherCodeInterface = new BaseGenericInterface () {

        @Override
        public <T> void onSuccess(BaseGenericCallback<T> sendVoucherCode) {
            int sukses = sendVoucherCode.getSukses();
            String pesan = sendVoucherCode.getPesan();
            pDialog.dismiss();
            if(sukses==2) {
                Intent i = new Intent(CouponVoucher.this, MyPromoActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);
                Toast.makeText(CouponVoucher.this, pesan, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(CouponVoucher.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(CouponVoucher.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(CouponVoucher.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CouponVoucher.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };



}