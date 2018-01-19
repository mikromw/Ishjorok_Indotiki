package com.pmberjaya.indotiki.app.deposit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.deposit.adapter.DepositTopUpAdapter;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositNewConfirmationCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.interfaces.deposit.DepositNewConfirmationInterface;
import com.pmberjaya.indotiki.interfaces.misc.RecyclerClickListener;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositTopUpListData;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.RecyclerTouchListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gilbert on 4/12/2017.
 */

public class DepositTopUpList extends BaseActivity {

    private RecyclerView top_up_list;
    private RelativeLayout layout_panel_bank;
    private LinearLayout linearlayout_panel;
    private TextView btn_cancel;
    private TextView tv_topup_nominal;
    private LinearLayout layout_bank_bca;
    private LinearLayout layout_bank_mandiri;
    private DepositTopUpAdapter adapter;
    private List<DepositTopUpListData> topUpListData;
    private String action;
    private boolean runGetTopUpList = false;
    private Animation animationSlideUp, animationSlideDown;
    private ProgressDialog pDialog;
    private SessionManager session;
    private String userName;
    private String nominal_deposit;
    private int date;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private int second;
    private String bankData;
    private String dateData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_top_up_list_activity);
        renderView();
        initToolbar();
        initSession();
        getDepositTopUpList();
        setOnClickListener();
    }

    private void renderView() {
        tv_topup_nominal = (TextView) findViewById(R.id.tv_topup_nominal);
        top_up_list = (RecyclerView) findViewById(R.id.top_up_list);
        layout_panel_bank = (RelativeLayout) findViewById(R.id.layout_panel_bank);
        linearlayout_panel = (LinearLayout) findViewById(R.id.linearlayout_panel);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        layout_bank_bca = (LinearLayout) findViewById(R.id.layout_bank_bca);
        layout_bank_mandiri = (LinearLayout) findViewById(R.id.layout_bank_mandiri);
        animationSlideUp = AnimationUtils.loadAnimation(DepositTopUpList.this, R.anim.enter_from_bottom);
        animationSlideDown = AnimationUtils.loadAnimation(DepositTopUpList.this, R.anim.exit_to_bottom);

    }

    private void initSession() {
        session = new SessionManager(this);
        HashMap<String, String> mapData = session.getUserDetails();
        userName = mapData.get(session.KEY_NAMA);
    }

    private void getDepositTopUpList() {
        runGetTopUpList = true;
        String api = Utility.getInstance().getTokenApi(this);
        action = "top_up_list";
        UserController.getInstance(this).getDepositTopUpList(action,api,getDepositTopUpListInterface);
    }

    BaseGenericInterface getDepositTopUpListInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            int sukses = baseGenericCallback.getSukses();
            String pesan = baseGenericCallback.getPesan();
            if (sukses == 2) {
                topUpListData = (List<DepositTopUpListData>) baseGenericCallback.getData();
                setListViewAdapter();
            } else {
                Toast.makeText(DepositTopUpList.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")){
                    Utility.getInstance().showInvalidApiKeyAlert(DepositTopUpList.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(DepositTopUpList.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DepositTopUpList.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void setListViewAdapter() {
        adapter = new DepositTopUpAdapter(this, topUpListData);
        GridLayoutManager lLayout = new GridLayoutManager(this, 2);
        top_up_list.setHasFixedSize(true);
        top_up_list.setNestedScrollingEnabled(false);
        top_up_list.setLayoutManager(lLayout);
        top_up_list.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.add_your_deposit));
        }
    }

    private void setOnClickListener() {
        top_up_list.addOnItemTouchListener(new RecyclerTouchListener(this, top_up_list, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position, ViewGroup parent) {
                layout_panel_bank.setOnClickListener(null);
                layout_panel_bank.setVisibility(View.VISIBLE);
                linearlayout_panel.setAnimation(animationSlideUp);
                linearlayout_panel.startAnimation(animationSlideUp);

                nominal_deposit = topUpListData.get(position).getNominal();
                String nominal_text = "Top Up Rp. " + Utility.getInstance().convertPrice(topUpListData.get(position).getNominal());
                tv_topup_nominal.setText(nominal_text);
            }
        }));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                linearlayout_panel.setAnimation(animationSlideDown);
                linearlayout_panel.startAnimation(animationSlideDown);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        layout_panel_bank.setVisibility(View.GONE);
                    }
                }, linearlayout_panel.getAnimation().getDuration());
            }
        });

        layout_bank_bca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankData = "BCA";
                postDriverDepositConfirmationData();
            }
        });

        layout_bank_mandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankData = "Mandiri";
                postDriverDepositConfirmationData();
            }
        });
    }

    private void postDriverDepositConfirmationData() {
        pDialog = ProgressDialog.show(DepositTopUpList.this, "", "loading...");
        String api = Utility.getInstance().getTokenApi(DepositTopUpList.this);
        UserController.getInstance(this).postDepositConfirmationData(depositConfirmParameters(), api, depositNewConfirmationInterface);
        return;
    }

    DepositNewConfirmationInterface depositNewConfirmationInterface = new DepositNewConfirmationInterface() {
        @Override
        public void onSuccess(DepositNewConfirmationCallback depositNewConfirmationCallback) {
            Utility.getInstance().dismissProgressDialog(pDialog);
            int sukses = depositNewConfirmationCallback.getSukses();
            String pesan = depositNewConfirmationCallback.getPesan();
            int transaction_id = depositNewConfirmationCallback.getRequest_id();
            if (sukses == 2) {
                Intent i = new Intent(DepositTopUpList.this, DepositConfirmationDetail.class);
                i.putExtra("transaction_id", String.valueOf(transaction_id));
                startActivity(i);
                finish();
            } else {
                Toast.makeText(DepositTopUpList.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(DepositTopUpList.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(DepositTopUpList.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public Map<String, String> depositConfirmParameters() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("bank_account_name", userName);
        params.put("amount", nominal_deposit);
        params.put("info", "isi Inki-Pay");
        params.put("bank", bankData);
        params.put("date", initDate());
        return params;
    }



    private String initDate() {
        Calendar currentTime = Calendar.getInstance();
        date = currentTime.get(Calendar.DATE);
        month = currentTime.get(Calendar.MONTH) + 1;
        year = currentTime.get(Calendar.YEAR);
        hour = currentTime.get(Calendar.HOUR_OF_DAY);
        minute = currentTime.get(Calendar.MINUTE);
        second = currentTime.get(Calendar.SECOND);
        dateData = String.format("%02d-%02d-%04d %02d:%02d:%02d", date, month, year, hour, minute, second);
        return dateData;
    }

    public void onBackPressed(){
        if (layout_panel_bank.getVisibility() == View.VISIBLE) {
            linearlayout_panel.setAnimation(animationSlideDown);
            linearlayout_panel.startAnimation(animationSlideDown);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    layout_panel_bank.setVisibility(View.GONE);
                }
            }, linearlayout_panel.getAnimation().getDuration());
        } else {
            super.onBackPressed();
            Intent i = new Intent(DepositTopUpList.this, DepositTab.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (runGetTopUpList == true) {
            getDepositTopUpList();
        }
    }

}
