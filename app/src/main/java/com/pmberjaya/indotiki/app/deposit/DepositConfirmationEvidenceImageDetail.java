package com.pmberjaya.indotiki.app.deposit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.views.polites.GestureImageView;

import java.io.File;

/**
 * Created by Gilbert on 02/06/2017.
 */

public class DepositConfirmationEvidenceImageDetail extends BaseActivity {

    Bitmap image1;
    ProgressDialog pd;
    LinearLayout.LayoutParams params;
    private String link_image;
    private String activity;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String payment_receipt;
    private GestureImageView iv_payment_receipt;
    private LinearLayout menu_description_layout;
    private TextView tv_menu_name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.deposit_confirmation_evidence_image_detail);
        final LinearLayout loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        Intent i = getIntent();
        final ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        payment_receipt = i.getStringExtra("payment_receipt");

        bindToolbar();

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.img_no_image)        //	Display Stub Image
                .showImageForEmptyUri(R.mipmap.img_no_image)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        File cacheDir = StorageUtils.getCacheDirectory(DepositConfirmationEvidenceImageDetail.this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DepositConfirmationEvidenceImageDetail.this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(DepositConfirmationEvidenceImageDetail.this)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
        if (payment_receipt == null || payment_receipt.equals("null")) {
            iv_payment_receipt = (GestureImageView) findViewById(R.id.iv_payment_receipt);
            iv_payment_receipt.setVisibility(View.VISIBLE);
            loadinglayout.setVisibility(View.GONE);
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            iv_payment_receipt = new GestureImageView(this);
            // Load image, decode it to Bitmap and return Bitmap to callback
            imageLoader.loadImage(payment_receipt, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Do whatever you want with Bitmap
                    loadinglayout.setVisibility(View.GONE);
                    iv_payment_receipt.setImageBitmap(loadedImage);
                    iv_payment_receipt.setLayoutParams(params);
                    layout.addView(iv_payment_receipt);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.d("error upload image", "" + failReason.getCause());
                }
            });
        }
    }

    private void bindToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_menu_name.setText(getResources().getString(R.string.payment_evidence));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public Animation.AnimationListener showDescriptionAnim = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) {
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            menu_description_layout.setVisibility(View.VISIBLE);
        }
    };

    public Animation.AnimationListener hideDescriptionAnim = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) {
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            menu_description_layout.setVisibility(View.GONE);
        }
    };

}
