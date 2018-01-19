package com.pmberjaya.indotiki.app.event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.models.event.EventPromoData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class  EventDetailPromo extends BaseActivity implements ViewPager.OnPageChangeListener{
    private Toolbar toolbar;
    private ImageView ivEvent;
    private TextView tvTitle;
    private FloatingActionButton fab_share;
    private String id;
    private String avatar_app;
    private String title;
    private String description;
    private String avatar;
    private String share_description;
    private TextView tvPromoCode;
    private String termsAndConditions;
    private String promoCode;
    private EventPromoData eventPromoData;
    private String howToUse;
    private LinearLayout layoutPromoCode;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NestedScrollView nested_scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_promo_main);
        GetIntent();
        RenderView();
        initToolbar();
        SetDataFromIntent();
    }

    private void RenderView() {
        ivEvent = findViewById(R.id.ivEvent);
        tvTitle = findViewById(R.id.tvTitle);
        tvPromoCode = findViewById(R.id.tvPromoCode);
        layoutPromoCode = findViewById(R.id.layoutPromoCode);
        fab_share = findViewById(R.id.fab_share);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareGambarEvent();
            }
        });


    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(EventDetailPromo.this.getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("description", description);
        bundle.putString("howtouse", howToUse);
        bundle.putString("tc", termsAndConditions);

        EventDetailPromoDescription eventDetailPromoDescription = new EventDetailPromoDescription();
        eventDetailPromoDescription.setArguments(bundle);
        EventDetailPromoHowToUse eventDetailPromoHowToUse= new EventDetailPromoHowToUse();
        eventDetailPromoHowToUse.setArguments(bundle);
        EventDetailPromoTermAndCond eventDetailPromoTC= new EventDetailPromoTermAndCond();
        eventDetailPromoTC.setArguments(bundle);

        adapter.addFragment(eventDetailPromoDescription, getResources().getString(R.string.description));
        adapter.addFragment(eventDetailPromoHowToUse, getResources().getString(R.string.how_to_use));
        adapter.addFragment(eventDetailPromoTC, getResources().getString(R.string.t_and_c));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

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
    private void GetIntent() {
        Intent in = getIntent();
        eventPromoData = in.getParcelableExtra("parcelable");
        id = eventPromoData.getId();
        avatar_app = eventPromoData.getAvatar_app();
        title = eventPromoData.getTitle();
        description = eventPromoData.getDescription();
        promoCode = eventPromoData.getCode_promo();
        termsAndConditions = eventPromoData.getTerms_and_cond();
        avatar = eventPromoData.getAvatar();
        howToUse = eventPromoData.getHow_to_use();
        share_description = Html.fromHtml(description).toString();
    }

    ArrayList<String> tabLayoutTitle = new ArrayList<>();
    ArrayList<String> viewPagerContent= new ArrayList<>();
    private void SetDataFromIntent() {
        Glide.with(this).load(avatar).into(ivEvent);
        tvTitle.setText(title);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(promoCode)) {
            tvPromoCode.setText(promoCode);
            layoutPromoCode.setVisibility(View.VISIBLE);
        }
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
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
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share EventListPromoFragment"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Toast.makeText(EventDetailPromo.this, "fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Toast.makeText(EventDetailPromo.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.promo_code_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
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
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.promo));
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