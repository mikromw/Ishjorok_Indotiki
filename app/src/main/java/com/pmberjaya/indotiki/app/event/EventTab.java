package com.pmberjaya.indotiki.app.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.services.fcm.FCMAuthLoginHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/10/2017.
 */

public class EventTab extends BaseActivity implements ViewPager.OnPageChangeListener{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MenuItem item;
    private LinearLayout loading;
    SessionManager session;
    private AppBarLayout appBarLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_tab);
        session = new SessionManager(EventTab.this);
        loading = (LinearLayout)findViewById(R.id.layout_loading);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        appBarLayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        initToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onBackPressed();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.news));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventListPromoFragment(), getResources().getString(R.string.promo));
        adapter.addFragment(new EventListNewFragment(),getResources().getString(R.string.event_new));
        adapter.addFragment(new EventListOtherFragment(),getResources().getString(R.string.event_other));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position == 0){
            item.setVisible(false);
        }
    }
    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            item.setVisible(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        item = menu.findItem(R.id.action_filter);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onBackPressed()
    {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(EventTab.this);
        builder.setTitle(null);
        builder.setMessage(R.string.exit_confirmation);
        builder.setIcon(R.mipmap.ic_launcher);
        // Set the action buttons
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
//                  stopService(new Intent(MainMenuActivity.this, FCMRealtimeDatabaseHandler.class));
                stopService(new Intent(EventTab.this, FCMAuthLoginHandler.class));
                EventTab.this.finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}


