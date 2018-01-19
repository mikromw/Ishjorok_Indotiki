package com.pmberjaya.indotiki.app.promo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.pmberjaya.indotiki.models.promo.PromoListData;
import com.pmberjaya.indotiki.models.promo.PromoListModel;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;

/**
 * Created by Gilbert on 18/07/2017.
 */

public class MyPromoActivity extends BaseActivity{
    private Toolbar toolbar;
    private LinearLayout loadinglayout;
    private UserController userController;
    private RelativeLayout error_layout;
    private LinearLayout bt_try_again;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_my_activity);
        userController = UserController.getInstance(MyPromoActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolbar(toolbar, getString(R.string.code_promo), null);
        renderViews();
        getCodePromoList();
    }

    private void renderViews() {
        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        error_layout = (RelativeLayout) findViewById(R.id.layoutError);
        bt_try_again = (LinearLayout) findViewById(R.id.btnError);
        bt_try_again.setOnClickListener(bt_try_againListener);
    }

    private View.OnClickListener bt_try_againListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getCodePromoList();
            error_layout.setVisibility(View.GONE);
        }
    };
    private void getCodePromoList() {
        loadinglayout.setVisibility(View.VISIBLE);
        String api = Utility.getInstance().getTokenApi(MyPromoActivity.this);
        userController.getPromoCodeList(api, userController.promoListParams(""), getCodePromoListInterface);
    }
    private PromoListModel data;
    BaseGenericInterface getCodePromoListInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            int sukses = baseGenericCallback.getSukses();
            String pesan = baseGenericCallback.getPesan();
            if (sukses == 2) {
                data = (PromoListModel) baseGenericCallback.getData();
                ArrayList<PromoListData> myPromoData = data.getMy_promo();
                Bundle bundleMyPromo = new Bundle();
                bundleMyPromo.putParcelableArrayList(Constants.MY_PROMO, myPromoData);
                MyPromoFragment myPromoFragment = new MyPromoFragment();
                myPromoFragment.setArguments(bundleMyPromo);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.code_promo_list_fragment, myPromoFragment).commitAllowingStateLoss();
                loadinglayout.setVisibility(View.GONE);
            } else {
                Toast.makeText(MyPromoActivity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            error_layout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

}
