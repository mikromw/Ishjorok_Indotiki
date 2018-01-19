package com.pmberjaya.indotiki.app.others.contactus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pmberjaya.indotiki.R;

/**
 * Created by edwin on 19/12/2016.
 */

public class OfficeInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_us_office_information_fragment, container, false);
        return rootView;
    }

}