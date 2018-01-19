package com.pmberjaya.indotiki.app.others;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.views.polites.GestureImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by willy on 4/13/2016.
 */
public class StandardImageItem extends Activity {
    protected GestureImageView imagelayout;
//    private String avatar;
    Bitmap image1;
    ProgressDialog pd;
    LayoutParams params;
    private String link_image;
    private String activity;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.standard_image_item);
        final LinearLayout loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        Intent i = getIntent();

        activity = i.getStringExtra("activity");
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.img_no_image)        //	Display Stub Image
                .showImageForEmptyUri(R.mipmap.img_no_image)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        File cacheDir = StorageUtils.getCacheDirectory(StandardImageItem.this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(StandardImageItem.this)
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
                .imageDownloader(new BaseImageDownloader(StandardImageItem.this)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        if(activity.equals("news")){
            String avatar = i.getStringExtra("item");
            link_image = avatar.replace("350","900");
            Log.d("avatar_img",""+avatar);

            //imagelayout = (GestureImageView) findViewById(R.id.image);
            if(avatar==null||avatar.equals("null")){
                imagelayout=(GestureImageView) findViewById(R.id.image);
                imagelayout.setVisibility(View.VISIBLE);
                loadinglayout.setVisibility(View.GONE);
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                imagelayout = new GestureImageView(this);
                // Load image, decode it to Bitmap and return Bitmap to callback
                imageLoader.loadImage(link_image, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        loadinglayout.setVisibility(View.GONE);
                        imagelayout.setImageBitmap(loadedImage);
                        imagelayout.setLayoutParams(params);
                        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
                        layout.addView(imagelayout);
                        imagelayout = (GestureImageView) findViewById(R.id.image);
                        imagelayout.setVisibility(View.GONE);
                    }
                });
            }

        }
        else if(activity.equals("courier_confirm")){
            String avatar = i.getStringExtra("item");
            if(avatar==null||avatar.equals("null")){
                imagelayout=(GestureImageView) findViewById(R.id.image);
                imagelayout.setVisibility(View.VISIBLE);
                loadinglayout.setVisibility(View.GONE);
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                imagelayout = new GestureImageView(this);
                // Load image, decode it to Bitmap and return Bitmap to callback
                Log.d("STE",""+avatar);
                imageLoader.loadImage(avatar, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        loadinglayout.setVisibility(View.GONE);
                        imagelayout.setImageBitmap(loadedImage);
                        imagelayout.setLayoutParams(params);
                        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
                        layout.addView(imagelayout);
                        imagelayout = (GestureImageView) findViewById(R.id.image);
                        imagelayout.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Log.d("error upload image",""+failReason.getCause());
                    }
                });
            }
        }
        else if(activity.equals("Inki-Pay")){
            loadinglayout.setVisibility(View.GONE);
            imagelayout=(GestureImageView) findViewById(R.id.image);
            Picasso.with(StandardImageItem.this)
                    .load(R.drawable.img_about_deposit)
                    .error(R.mipmap.img_no_image)
                    .into(imagelayout);
        }else if(activity.equals("chat")){
            imagelayout=(GestureImageView) findViewById(R.id.image);
            String filePath = i.getStringExtra("filePath");
            File imgFile = new File(filePath);
            if(imgFile.exists()){
                Picasso.with(StandardImageItem.this)
                        .load(imgFile)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imagelayout, new Callback() {
                            @Override
                            public void onSuccess() {
                                loadinglayout.setVisibility(View.GONE);
                                imagelayout.setVisibility(View.VISIBLE);
                            }
                            @Override
                            public void onError() {
                                loadinglayout.setVisibility(View.GONE);
                                imagelayout.setImageResource(R.mipmap.img_no_image);
                            }

                        });
            }
        }else if(activity.equals("courier_complete")){
            String avatar = i.getStringExtra("item");
            if(avatar==null||avatar.equals("null")){
                imagelayout=(GestureImageView) findViewById(R.id.image);
                imagelayout.setVisibility(View.VISIBLE);
                loadinglayout.setVisibility(View.GONE);
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                imagelayout = new GestureImageView(this);
                // Load image, decode it to Bitmap and return Bitmap to callback
                Log.d("STE",""+avatar);
                imageLoader.loadImage(avatar, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        loadinglayout.setVisibility(View.GONE);
                        imagelayout.setImageBitmap(loadedImage);
                        imagelayout.setLayoutParams(params);
                        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
                        layout.addView(imagelayout);
                        imagelayout = (GestureImageView) findViewById(R.id.image);
                        imagelayout.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Log.d("error upload image",""+failReason.getCause());
                    }
                });
            }
        }
    }

}

