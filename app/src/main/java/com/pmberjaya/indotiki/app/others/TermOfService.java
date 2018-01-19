package com.pmberjaya.indotiki.app.others;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;

/**
 * Created by edwin on 11/07/2017.
 */

public class TermOfService extends BaseActivity{
    private WebView web;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_of_services);
        initToolbar();
        web = (WebView) findViewById(R.id.webv);
        web.getSettings().setJavaScriptEnabled(true);
        String url = Config.APP_WEB_URL+"/termsofservice";
        web.loadUrl(url);

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
            getSupportActionBar().setTitle(getResources().getString(R.string.title_term_of_service));
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


}
