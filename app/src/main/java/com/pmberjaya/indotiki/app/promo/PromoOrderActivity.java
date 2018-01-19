package com.pmberjaya.indotiki.app.promo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.models.parcelables.PromoCodeDataParcelable;
import com.pmberjaya.indotiki.models.promo.PromoListData;
import com.pmberjaya.indotiki.models.promo.PromoListModel;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;

/**
 * Created by edwin on 25/07/2017.
 */

public class PromoOrderActivity extends BaseActivity {
    private Toolbar toolbar;
    //    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String requestType;
    private UserController userController;
    private LinearLayout layoutLoading;
    private RelativeLayout layoutError;
    private LinearLayout btnError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_order_activity);
        userController = UserController.getInstance(PromoOrderActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolbar(toolbar, getString(R.string.code_promo), null);
        renderViews();
        getIntentExtra();
        getCodePromoList();
    }
    private void renderViews() {
        layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
        layoutError = (RelativeLayout) findViewById(R.id.layoutError);
        btnError = (LinearLayout) findViewById(R.id.btnError);
        btnError.setOnClickListener(bt_try_againListener);
    }

    private View.OnClickListener bt_try_againListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getCodePromoList();
            layoutError.setVisibility(View.GONE);
        }
    };

    private void getIntentExtra() {
        Intent i = getIntent();
        requestType = i.getStringExtra("request_type");
    }

    private void getCodePromoList() {
        layoutLoading.setVisibility(View.VISIBLE);
        String api = Utility.getInstance().getTokenApi(PromoOrderActivity.this);
        userController.getPromoCodeList(api, userController.promoListParams(requestType), getCodePromoListInterface);
    }

    private PromoListModel data;
    BaseGenericInterface getCodePromoListInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            int sukses = baseGenericCallback.getSukses();
            String pesan = baseGenericCallback.getPesan();
            if (sukses == 2) {
                data = (PromoListModel) baseGenericCallback.getData();
                String usePromo = "true";
                ArrayList<PromoListData> promoData = data.getGeneral_promo();
                ArrayList<PromoListData> myPromoData = data.getMy_promo();

                Bundle bundleMyPromo = new Bundle();
                bundleMyPromo.putParcelableArrayList(Constants.GENERAL_PROMO, promoData);
                bundleMyPromo.putParcelableArrayList(Constants.MY_PROMO, myPromoData);
                bundleMyPromo.putString("isUsePromo", usePromo);
                MyPromoFragment myPromoFragment = new MyPromoFragment();
                myPromoFragment.setArguments(bundleMyPromo);

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.code_promo_list_fragment, myPromoFragment).commitAllowingStateLoss();
                layoutLoading.setVisibility(View.GONE);
            } else {
                Toast.makeText(PromoOrderActivity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            layoutError.setVisibility(View.VISIBLE);
        }
    };

    private int SELECT_PROMO = 4;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PROMO) {
            if (resultCode == RESULT_OK) {
                PromoCodeDataParcelable promoCodeDataParcelable = data.getParcelableExtra(Constants.PROMO_CODE_PARCELABLE);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                PromoCodeDataParcelable promoCodeDataParcelable = data.getParcelableExtra(Constants.PROMO_CODE_PARCELABLE);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.PROMO_CODE_PARCELABLE, promoCodeDataParcelable);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
