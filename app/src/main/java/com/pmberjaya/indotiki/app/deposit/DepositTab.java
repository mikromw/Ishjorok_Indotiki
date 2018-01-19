package com.pmberjaya.indotiki.app.deposit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.deposit.DepositData.CheckDepositData;
import com.pmberjaya.indotiki.models.parcelables.DepositDataParcelable;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.app.others.StandardImageItem;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DepositTab extends BaseActivity {
    private DepositDataParcelable depositDataParcelable;
    private TextView tv_remaining_balance;
    private SessionManager session;
    private String deposit;
    String activity;
    private String currentTab;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MenuItem item;
    private AlertDialog dialog;
    private String nominal_deposit;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_tab);
        GetIntent();
        initSession();
        initToolbar();
    }

    private void initSession() {
        session = new SessionManager(this);
        HashMap<String,String> userData = session.getUserDetails();
        deposit = userData.get(SessionManager.KEY_DEPOSIT);
        tv_remaining_balance = (TextView) findViewById(R.id.tv_remaining_balance);
        tv_remaining_balance.setText(Utility.getInstance().convertPrice(deposit));
        checkDeposit();
    }

    private void GetIntent() {
        Intent i = getIntent();
        currentTab = i.getStringExtra("tab");
        if(currentTab==null){
            currentTab="0";
        }
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
            getSupportActionBar().setTitle(getResources().getString(R.string.deposit));
        }
        setViewPager();
    }

    private void setViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(Integer.parseInt(currentTab));
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(DepositTab.this.getSupportFragmentManager());
        adapter.addFragment(new DepositMainFragment(), getResources().getString(R.string.deposit_confirmation));
        adapter.addFragment(new DepositLogFragment(), getResources().getString(R.string.deposit_log));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.deposit_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if(id == R.id.action_help){
//            @SuppressLint("RestrictedApi") View alertView = View.inflate(new ContextThemeWrapper(DepositTab.this, R.style.AlertDialog_AppCompat), R.layout.deposit_help_alert_dialog, null);
//            AlertDialog.Builder builder = new AlertDialog.Builder(DepositTab.this);
//            ImageView img = (ImageView) alertView.findViewById(R.id.img_rekening);
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(DepositTab.this,StandardImageItem.class);
//                    intent.putExtra("activity","Inki-Pay");
//                    startActivity(intent);
//                }
//            });
//            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int id) {
//                }
//            });
//            builder.setView(alertView);
//            dialog = builder.create();
//            dialog.setCancelable(false);
//            dialog.show();
//        }
//        return super.onOptionsItemSelected(item);
//    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    public void checkDeposit(){
        String api = Utility.getInstance().getTokenApi(DepositTab.this);
        UserController.getInstance(DepositTab.this).getCheckDeposit(api, checkDepositInterface);
    }

    BaseGenericInterface checkDepositInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> checkDepositCallback) {
            int sukses = checkDepositCallback.getSukses();
            String pesan = checkDepositCallback.getPesan();
            if (sukses == 2) {
                CheckDepositData data = (CheckDepositData) checkDepositCallback.getData();
                String deposit = data.getDeposit();
                tv_remaining_balance.setText(Utility.getInstance().convertPrice(deposit));
                session.createLoginSession(null,null,null,null,null, deposit, null);
            } else {
                Toast.makeText(DepositTab.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(DepositTab.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(DepositTab.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DepositTab.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}