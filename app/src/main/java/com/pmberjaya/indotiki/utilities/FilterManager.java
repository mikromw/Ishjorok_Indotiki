package com.pmberjaya.indotiki.utilities;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class FilterManager {
	// Shared Preferences
	private SharedPreferences filterPreferences;
	// Editor for Shared preferences
    private Editor filterPrefsEditor;
	// Context
    
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "filterPrefs";
	
	// All Shared Preferences Keys
	
	// User name (make variable public to access from outside)
	public static final String KEY_FILTER = "filter";
	public static final String KEY_FILTER_GENDER = "filter_gender";
	public static final String KEY_FILTER_BOOKING_COMPLETE = "filter_complete";
	
	
	
	// Constructor
	public FilterManager(Context context){
		this._context = context;
		filterPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		filterPrefsEditor = filterPreferences.edit();
	}
	 
	/**
	 * Create login session
	 **/
	public void createFilterSession(String filter){
		// Storing login value as TRUE
		
		filterPrefsEditor.putString(KEY_FILTER, filter);

		// commit changes
		filterPrefsEditor.commit();
	}

	public void createFilterBookingCompleteSession(String filter){
		// Storing login value as TRUE

		filterPrefsEditor.putString(KEY_FILTER_BOOKING_COMPLETE, filter);

		// commit changes
		filterPrefsEditor.commit();
	}
	
	
	public  String getFilter(){
		String filter =  filterPreferences.getString(KEY_FILTER, null);
		// return filter
		return filter; 
	}

	public  String getFilterBookingComplete(){
		String filter =  filterPreferences.getString(KEY_FILTER_BOOKING_COMPLETE, null);
		// return filter
		return filter;
	}
	
	
}
