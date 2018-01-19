package com.pmberjaya.indotiki.app.others;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by willy on 7/22/2016.
 */
public class InviteFriends extends BaseActivity {
    private boolean runSendReferral = false;
    private LinearLayout btn_share;
    private String data;
    private ShareButton shareButton;
    private LinearLayout loadinglayout;
    private RelativeLayout error_layout;
    private LinearLayout btn_tryagain;
    private LinearLayout fb_share;
    private LinearLayout invitefriend_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.invite_friends);

        btn_share = (LinearLayout)findViewById(R.id.share);
        shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        loadinglayout = (LinearLayout)findViewById(R.id.layout_loading);
        error_layout = (RelativeLayout)findViewById(R.id.layoutError);
        btn_tryagain = (LinearLayout)findViewById(R.id.btnError);
        invitefriend_layout = (LinearLayout)findViewById(R.id.invite_friends_layout);
        sendReferral();
        fb_share =  (LinearLayout)findViewById(R.id.fb_share);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.invite));
        }

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Sudah coba Indotiki? Jasa transportasi ojek, antar makan, kirim paket. Daftar di: "+data);
                sharingIntent.setType("text/plain");
                startActivity(sharingIntent);
            }
        });

        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderloadingView();
                sendReferral();
            }
        });
    }

    public void sendReferral()
    {
            runSendReferral = true;
            renderloadingView();
            String api = Utility.getInstance().getTokenApi(InviteFriends.this);
            UserController.getInstance(InviteFriends.this).sendReferral(api, sendReferralInterface);
            return;
    }
    BaseGenericInterface sendReferralInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> referralCallback) {
            runSendReferral= false;
            data = (String) referralCallback.getData();
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(data))
                    .build();
            invitefriend_layout.setVisibility(View.VISIBLE);
            shareButton.setShareContent(content);
            fb_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareButton.performClick();
                }
            });
            loadinglayout.setVisibility(View.GONE);
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(InviteFriends.this, getResources().getString(R.string.relogin));
                } else {
                    renderErrorView();
                    Toast.makeText(InviteFriends.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        invitefriend_layout.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        invitefriend_layout.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(runSendReferral == true){
            sendReferral();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
