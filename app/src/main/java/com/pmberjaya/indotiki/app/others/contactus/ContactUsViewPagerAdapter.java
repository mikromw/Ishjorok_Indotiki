package com.pmberjaya.indotiki.app.others.contactus;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContactUsViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;
    ArrayList<String> filter;

    public ContactUsViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

//        if(position == 0)
//        {
//            EventListPromoFragment lp = new EventListPromoFragment();
//            return lp;
//        }
//        else
//        {
//            News lp = new News();
//            return lp;
//        }
        return null;
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