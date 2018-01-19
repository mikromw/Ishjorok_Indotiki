package com.pmberjaya.indotiki.app.promo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.models.parcelables.PromoCodeDataParcelable;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by Gilbert on 19/07/2017.
 */

public class  PromoDetail extends BaseActivity {
    private Toolbar toolbar;
    private ImageView iv_gambar_event;
    private TextView tv_title;
    private TextView tv_description;
    private PromoCodeDataParcelable promoCodeDataParcelable;
    private RelativeLayout btn_use_voucher_code;
    private TextView tv_use_voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_code_detail);
        initToolbar();
        renderView();
        GetIntent();
    }

    private void renderView() {
        iv_gambar_event = (ImageView) findViewById(R.id.iv_gambar_event);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        btn_use_voucher_code = (RelativeLayout) findViewById(R.id.btn_use_voucher_code);
        tv_use_voucher = (TextView) findViewById(R.id.tvUseVoucher);

    }

    private void GetIntent() {
        Intent i = getIntent();
        String action = i.getAction();
        if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(action)&&action.equals("use_promo")) {
            btn_use_voucher_code.setVisibility(View.VISIBLE);
            btn_use_voucher_code.setOnClickListener(useThisVoucher);
        }else if (Utility.getInstance().checkIfStringIsNotNullOrEmpty(action)&&action.equals("use_promo_later")) {
            btn_use_voucher_code.setBackgroundColor(Utility.getColor(getResources(),R.color.red_900,null));
            btn_use_voucher_code.setVisibility(View.VISIBLE);
            tv_use_voucher.setText(getResources().getString(R.string.use_promo_later));
            btn_use_voucher_code.setOnClickListener(useThisVoucherLater);
        }else{
            btn_use_voucher_code.setVisibility(View.GONE);
        }
        promoCodeDataParcelable = i.getParcelableExtra(Constants.PROMO_CODE_PARCELABLE);
        tv_title.setText(promoCodeDataParcelable.title);
        tv_description.setText(promoCodeDataParcelable.description);
        if (promoCodeDataParcelable.cover != null && !promoCodeDataParcelable.cover.equals("")) {
            PicassoLoader.loadImage(this, promoCodeDataParcelable.cover, iv_gambar_event);
        } else {
            PicassoLoader.loadImageFail(this, iv_gambar_event);
        }
    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
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
            getSupportActionBar().setTitle(getResources().getString(R.string.promo_code_detail));
        }
    }

    private View.OnClickListener useThisVoucher = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    };
    private View.OnClickListener useThisVoucherLater = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            promoCodeDataParcelable = null;
            returnIntent.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    };
    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

}
