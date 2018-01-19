package com.pmberjaya.indotiki.app.chat;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.views.TouchImageView;


public class ChatImageFullscreenActivity extends AppCompatActivity {

    private TouchImageView mImageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_image_detail_activity);
        bindViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindViews(){
        progressDialog = new ProgressDialog(this);
        mImageView = (TouchImageView) findViewById(R.id.imageView);
    }

    private void setValues(){
        String filePath;
        filePath = getIntent().getStringExtra("filePath");
        Log.i("TAG","imagem recebida "+filePath);

        Glide.with(this).asBitmap().load(filePath).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                progressDialog.setMessage("Loading Image..");
                progressDialog.show();
            }
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                progressDialog.dismiss();
                mImageView.setImageBitmap(resource);
            }
        });
    }

}
