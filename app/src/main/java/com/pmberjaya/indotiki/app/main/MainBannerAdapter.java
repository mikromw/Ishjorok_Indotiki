package com.pmberjaya.indotiki.app.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.event.EventDetailPromo;
import com.pmberjaya.indotiki.models.event.EventPromoData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willy on 3/30/2016.
 */
public class MainBannerAdapter extends PagerAdapter {

    private List<EventPromoData> image_resources = new ArrayList<>();
    private Context ctx;
    private LayoutInflater layoutInflater;
    private  Activity activity;

    public MainBannerAdapter(Context ctx, List<EventPromoData> data)
    {
        this.ctx=ctx;
        this.image_resources=data;
        activity = (Activity) ctx;
    }
    @Override
    public int getCount() {
        return image_resources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        Log.d("JALAN","JALAN");
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.main_banner_adapter,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.album_cover);
        if(image_resources.get(position).getAvatar_app() ==null) {
            PicassoLoader.loadDefaultEvent(ctx,imageView);
        }
        else{
            PicassoLoader.loadImage(ctx,image_resources.get(position).getAvatar_app(),imageView);
            item_view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //this will log the page number that was click
                    Log.i("TAG", "This page was clicked: " + position);
                    EventPromoData eventPromoData = image_resources.get(position);
                    Intent in = new Intent(ctx, EventDetailPromo.class);
                    in.putExtra("parcelable", eventPromoData);
                    activity.startActivity(in);
                }
            });
        }
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
