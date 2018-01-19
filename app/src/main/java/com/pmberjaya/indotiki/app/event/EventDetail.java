package com.pmberjaya.indotiki.app.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.models.event.EventNewData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class EventDetail extends AppCompatActivity {
    private Toolbar toolbar;

    private String id;
    private String avatar_app;
    private String title;
    private String description;
    private String avatar;
    private String share_description;
    private ImageView ivEvent;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvDescription;
    private FloatingActionButton fabShare;
    private String date;
    private EventNewData eventNewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_main);
        renderView();
        initToolbar();
        getIntentExtras();
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareGambarEvent();
            }
        });
    }

    private void renderView() {
        ivEvent = findViewById(R.id.ivEvent);
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvDescription = findViewById(R.id.tvDescription);
        fabShare = findViewById(R.id.fabShare);
    }

    private void getIntentExtras() {
        Intent in = getIntent();
        eventNewData = in.getParcelableExtra("parcelable");
        id = eventNewData.getId();
        avatar_app = eventNewData.getAvatar();
        title = eventNewData.getTopic();
        description = eventNewData.getNews();
        avatar = eventNewData.getAvatar();
        date = eventNewData.getDate();
        share_description = Html.fromHtml(description).toString();
        SetDataFromIntent();
    }

    private void SetDataFromIntent() {
        Glide.with(this).load(avatar_app).into(ivEvent);
        tvTitle.setText(title);
        tvDate.setText(date);
        tvDescription.setText(Html.fromHtml(description).toString());
    }

    private void shareGambarEvent() {
        String url = avatar_app;
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, " "+share_description +"\n"+
                        "https://play.google.com/store/apps/details?id=com.pmberjaya.indotiki");
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, Utility.getInstance().getLocalBitmapUri(bitmap, EventDetail.this));
                startActivity(Intent.createChooser(i, "Share EventListPromoFragment"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Toast.makeText(EventDetail.this, "fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Toast.makeText(EventDetail.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.event));
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
