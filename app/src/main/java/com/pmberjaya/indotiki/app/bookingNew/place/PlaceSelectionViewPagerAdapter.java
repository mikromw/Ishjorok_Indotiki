package com.pmberjaya.indotiki.app.bookingNew.place;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlaceSelectionViewPagerAdapter extends FragmentPagerAdapter {
 
    CharSequence Titles[];
    int NumbOfTabs;
    HashMap<String,String> data;
    public PlaceSelectionViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, HashMap<String,String> data) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.data = data;
    }
 
    @Override
    public Fragment getItem(int position) {
	       if(position == 0) 
	        {
				PlaceSelectionGoogleFragment lp = new PlaceSelectionGoogleFragment();
	        	Bundle bundle = new Bundle();
	            bundle.putString("place", data.get("place"));
	            bundle.putString("transportation", data.get("transportation"));
	            bundle.putString("latitude", data.get("latitude"));
	            bundle.putString("longitude", data.get("longitude"));
	            bundle.putString("activity", data.get("activity"));
	            bundle.putString("state", data.get("state"));
	            lp.setArguments(bundle);
	            return lp;
	        }
	        else if(position == 1) 
	        {
	            PlaceSelectionFavoriteFragment lp = new PlaceSelectionFavoriteFragment();
	    		Bundle bundle = new Bundle();
	            bundle.putString("place", data.get("place"));
	            bundle.putString("transportation", data.get("transportation"));
	            bundle.putString("latitude", data.get("latitude"));
	            bundle.putString("longitude", data.get("longitude"));
	            bundle.putString("activity", data.get("activity"));
	            lp.setArguments(bundle);
	            return lp;
	        }
	        else
	        {
	        	PlaceSelectionMapFragment lp = new PlaceSelectionMapFragment();
	        	Bundle bundle = new Bundle();
	            bundle.putString("place", data.get("place"));
	            lp.setArguments(bundle);
	            return lp;
	        }
    }
 
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
 
 
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}