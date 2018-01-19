package com.pmberjaya.indotiki.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.login.Login;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.adapter.SubMainMenuAdapter;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.interfaces.misc.RecyclerClickListener;
import com.pmberjaya.indotiki.models.main.SubMenuData;
import com.pmberjaya.indotiki.views.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Created by Gilbert on 12/4/2017.
 */

public class SubMainMenu extends BaseActivity{

    private ArrayList<SubMenuData> subMenu;
    private LinearLayout layoutList;
    private RecyclerView listSubMenu;
    private SubMainMenuAdapter adapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_sub_main_menu);
        initToolBar();
        renderView();
        initSession();
        getIntentExtra();
        setListener();
    }

    private void renderView() {
        layoutList = findViewById(R.id.layoutList);
        listSubMenu = findViewById(R.id.listSubMenu);
    }

    private void initSession() {
        session = new SessionManager(this);
    }

    private void getIntentExtra() {
        Intent i = getIntent();
        subMenu = i.getParcelableArrayListExtra("subMenu");
        setListViewAdapter();
    }

    private void setListViewAdapter() {
        adapter = new SubMainMenuAdapter(this, subMenu);
        GridLayoutManager lLayout = new GridLayoutManager(this, 2);
        listSubMenu.setHasFixedSize(true);
        listSubMenu.setNestedScrollingEnabled(false);
        listSubMenu.setLayoutManager(lLayout);
        listSubMenu.setAdapter(adapter);
    }

    private void setListener() {
       listSubMenu.addOnItemTouchListener(new RecyclerTouchListener(this, listSubMenu, new RecyclerClickListener() {
           @Override
           public void onClick(View view, int position, ViewGroup parent) {
               if (subMenu.get(position).getDisplay_submenu().equals("INDO_PLN")) {
//                   Intent i = new Intent(SubMainMenu.this, PlnMain.class);
//                   startActivity(i);
               }
           }
       }));
    }

    public void initToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
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
            getSupportActionBar().setTitle(getResources().getString(R.string.bill));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pulsa_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_transaction){
            if (session.isLogin()) {
//                Intent intent = new Intent(this, HistoryPln.class);
//                startActivity(intent);
            } else {
                Intent i = new Intent(SubMainMenu.this, Login.class);
                startActivityForResult(i, Constants.STATE_LOGIN_CODE);
                showToast(getResources().getString(R.string.please_login));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}