package com.pmberjaya.indotiki.dao;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

public class LocationSessionManager {

    // Shared Preferences
    private SharedPreferences loginPreferences;
    // Editor for Shared preferences
    private Editor loginPrefsEditor;
    // Context

    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "locationPrefs";

    public static final String KEY_STATE_ID = "stateId";
    public static final String KEY_STATE = "state";
    public static final String KEY_DISTRICT_ID= "districtId";
    public static final String KEY_DISTRICT_ID_CENTRAL= "districtIdCentral";
    public static final String KEY_DISTRICT= "district";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    // Constructor
    public LocationSessionManager(Context context){
        this._context = context;
        loginPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        loginPrefsEditor = loginPreferences.edit();
    }

    public void setUserLocation(String stateId, String state, String districtId, String district, String districtIdCentral){
        loginPrefsEditor.putString(KEY_STATE, state);
        loginPrefsEditor.putString(KEY_STATE_ID, stateId);
        loginPrefsEditor.putString(KEY_DISTRICT, district);
        loginPrefsEditor.putString(KEY_DISTRICT_ID, districtId);
        loginPrefsEditor.putString(KEY_DISTRICT_ID_CENTRAL, districtIdCentral);
        loginPrefsEditor.commit();
    }

    public String getUserDistrict(){
        String district =loginPreferences.getString(KEY_DISTRICT, null);
        return district;
    }
    public String getUserDistrictIdCentral(){
        String districtId =loginPreferences.getString(KEY_DISTRICT_ID_CENTRAL, null);
        return districtId;
    }
    public String getUserState(){
        String state =loginPreferences.getString(KEY_STATE, null);
        return state;
    }
    public String getUserStateId(){
        String state =loginPreferences.getString(KEY_STATE_ID, null);
        return state;
    }
    public void deleteUserLocation(){
        loginPrefsEditor.remove(KEY_STATE_ID);
        loginPrefsEditor.remove(KEY_STATE);
        loginPrefsEditor.remove(KEY_DISTRICT_ID);
        loginPrefsEditor.remove(KEY_DISTRICT);
        loginPrefsEditor.commit();
    }
    public void setCurrentLatLng(String latitude, String longitude) {
        loginPrefsEditor.putString(KEY_LATITUDE, latitude);
        loginPrefsEditor.putString(KEY_LONGITUDE, longitude);
        loginPrefsEditor.commit();
    }

    public HashMap<String, String> getLatLng() {
        HashMap<String, String> latlng = new HashMap<String, String>();
        latlng.put(KEY_LATITUDE, loginPreferences.getString(KEY_LATITUDE, null));
        latlng.put(KEY_LONGITUDE, loginPreferences.getString(KEY_LONGITUDE, null));

        return latlng;
    }
}
