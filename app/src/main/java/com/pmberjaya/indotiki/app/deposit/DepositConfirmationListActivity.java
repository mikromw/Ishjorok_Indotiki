package com.pmberjaya.indotiki.app.deposit;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.deposit.adapter.DepositConfirmationListAdapter;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationData;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationModel;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.EndlessListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepositConfirmationListActivity extends AppCompatActivity implements EndlessListView.EndlessListener {
    private EndlessListView list;
    private DepositConfirmationListAdapter adapter;
    private LinearLayout loadinglayout;
    private LinearLayout nobookinglayout;
    private LinearLayout listlayout;
    private int banyakdata;
    private int page = 0;
    private TextView tv_error_timeout;
    private LinearLayout bt_try_again;
    private RelativeLayout error_layout;
    private SwipeRefreshLayout swiperefresh;
    private boolean runGetDepositDriver = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_confirmation_list_activity);
        initToolbar();
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        nobookinglayout = (LinearLayout) findViewById(R.id.nobookinglayout);
        listlayout = (LinearLayout) findViewById(R.id.deposit_list_layout);
        list = (EndlessListView) findViewById(R.id.deposit_list);
        list.setListener(this);
//        getDepositDriver(String.valueOf(page));
        tv_error_timeout = (TextView) findViewById(R.id.tvErrorMessage);

        error_layout = (RelativeLayout) findViewById(R.id.layoutError);
        bt_try_again = (LinearLayout) findViewById(R.id.btnError);

        bt_try_again.setOnClickListener(try_again_listener);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                page=0;
                loadinglayout.setVisibility(View.VISIBLE);
                nobookinglayout.setVisibility(View.GONE);
                listlayout.setVisibility(View.GONE);
                adapter=null;
                getDepositDriver(String.valueOf(page));
            }
        });
    }
    public View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            renderloadingView();
            getDepositDriver(String.valueOf(page));
        }
    };

    public void SetListViewAdapter(List<DepositConfirmationData> depositConfirmationDatas) {
        if(adapter==null){
            nobookinglayout.setVisibility(View.GONE);
            loadinglayout.setVisibility(View.GONE);
            listlayout.setVisibility(View.VISIBLE);
            adapter = new DepositConfirmationListAdapter(DepositConfirmationListActivity.this, R.layout.deposit_confirmation_list_adapter, depositConfirmationDatas);
            list.setLoadingView(R.layout.view_loading_layout);
//            list.setDepositConfirmationAdapter(adapter);
        }
        else{
//            list.addNewDepositConfirmationData(depositConfirmationDatas);
            list.removeLoadingView();
        }
    }
    private void getDepositDriver(String page)
    {
        runGetDepositDriver = true;
        Log.d("Inki-Pay","JALAN");
        String api = Utility.getInstance().getTokenApi(DepositConfirmationListActivity.this);
        UserController.getInstance(DepositConfirmationListActivity.this).getDepositConfirmationData(depositParameters(page),api, baseGenericInterface);
        return;
    }
    public Map<String, String> depositParameters(String page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("offset", page );
        params.put("from_date", "");
        params.put("to_date", "");
        return  params;
    }
    BaseGenericInterface baseGenericInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            swiperefresh.setRefreshing(false);
            Log.d("DepositCallback", "Jalan");
            int sukses = baseGenericCallback.getSukses();
            Log.d("SUKSES",">"+sukses);
            if(sukses==2){
                page++;
                DepositConfirmationModel data = (DepositConfirmationModel) baseGenericCallback.getData();
                List<DepositConfirmationData> depositDatas = data.getResultArray();
                banyakdata = Integer.parseInt(data.getTotal());
                String depositText=null;
                banyakdata = Integer.parseInt(data.getTotal());
                Log.d("banyak data",""+banyakdata);
                if(banyakdata== 0){
                    loadinglayout.setVisibility(View.GONE);
                    listlayout.setVisibility(View.GONE);
                    nobookinglayout.setVisibility(View.VISIBLE);
                }
                else{
                    SetListViewAdapter(depositDatas);
                }
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(DepositConfirmationListActivity.this, getResources().getString(R.string.relogin));
                } else {
                    if(page>=1){
                        renderErrorListView();
                    }
                    else {
                        renderErrorView();
                    }
//                    Toast.makeText(this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }

    };


    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        listlayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        listlayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
        tv_error_timeout.setText(R.string.currently_unavailable);
    }
    public void renderErrorListView(){
        list.removeLoadingView();
        list.setErrorListView(R.layout.view_error_endless_listview);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(runGetDepositDriver == true){
            getDepositDriver(String.valueOf(page));
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void loadData() {
        int total = list.getAdapter().getCount();
        if(banyakdata>total){
            getDepositDriver(String.valueOf(page));
        }
        else{
            list.removeLoadingView();
        }
    }
    @Override
    public void onTryAgainAPICallback() {
        list.removeLoadingView();
        list.setLoadingView(R.layout.view_loading_list_layout);
        getDepositDriver(String.valueOf(page));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                onBackPressed();
                }
            });
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.transaction));
        }
    }

    public void onBackPressed() {
        finish();
    }
}