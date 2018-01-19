package com.pmberjaya.indotiki.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.pmberjaya.indotiki.dao.DBController;

public class SessionManager {

	// Shared Preferences
	private SharedPreferences loginPreferences;
	// Editor for Shared preferences
	private Editor loginPrefsEditor;
	// Context

	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "loginPrefs";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	private static final String IS_IN_CHAT= "isInChat";

	// User name (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	// Email address (make variable public to access from outside)
	public static final String KEY_NAMA = "nama";

	public static final String KEY_PHONE = "phone";

	public static final String KEY_ID = "id";

	public static final String KEY_AVATAR= "avatar";

	public static final String KEY_DEPOSIT = "Inki-Pay";


	public static final String KEY_TOKEN_VERIFICATION= "token_verification";

	public static final String KEY_PROPERTY_REG_ID="property_reg_id";

	public static final String KEY_CHECK_STATUS="check_status";

	public static final String KEY_REQUESTID = "requestId";

	public static final String KEY_REQUESTTYPE = "requestType";

	public static final String KEY_ANOTHER_LOGIN = "another_login";

	public static final String KEY_REQUEST_TIME = "requestTime";

	public static final String KEY_ANOTHER_LOGIN_TYPE = "another_login_type";

	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		loginPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		loginPrefsEditor = loginPreferences.edit();
	}

	public String getRegistrationId() {
		String registrationId = loginPreferences.getString(KEY_PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return null;
		}
		return registrationId;
	}
	public String getKeyCheckStatus() {
		String check = loginPreferences.getString(KEY_CHECK_STATUS,"");
		if (check.isEmpty()) {
			return null;
		}
		return check;
	}
	public void storeRegistrationId(String regId) {
		loginPrefsEditor.putString(KEY_PROPERTY_REG_ID, regId);
		loginPrefsEditor.apply();
	}
	/**
	 * Create login session
	 **/
	public void removeRegistrationId(Context context) throws Exception {
		loginPrefsEditor.remove(KEY_PROPERTY_REG_ID);
		loginPrefsEditor.apply();
	}
	public boolean isAnotherLogin() {
		return loginPreferences.getBoolean(KEY_ANOTHER_LOGIN, false);
	}
	public String getAnotherLoginType() {
		return loginPreferences.getString(KEY_ANOTHER_LOGIN_TYPE,null);
	}
	public void setKeyAnotherLogin(String type) {
		loginPrefsEditor.putBoolean(KEY_ANOTHER_LOGIN, true);
		loginPrefsEditor.putString(KEY_ANOTHER_LOGIN_TYPE, type);
		loginPrefsEditor.commit();
	}
	public void setOffKeyAnotherLogin(){
		loginPrefsEditor.remove(KEY_ANOTHER_LOGIN);
		loginPrefsEditor.remove(KEY_ANOTHER_LOGIN_TYPE);
		loginPrefsEditor.apply();
	}
	public void createLoginSession(String id, String nama, String phone, String avatar, String email, String deposit, String regId){
		// Storing login value as TRUE
		loginPrefsEditor.putBoolean(IS_LOGIN, true);

		if(id!=null) {
			loginPrefsEditor.putString(KEY_ID, id);
		}
		if(nama!=null) {
			loginPrefsEditor.putString(KEY_NAMA, nama);
		}
		if(phone!=null){
			loginPrefsEditor.putString(KEY_PHONE, phone);
		}
		if(avatar!=null) {
			loginPrefsEditor.putString(KEY_AVATAR, avatar);
		}
		if(email!=null) {
			loginPrefsEditor.putString(KEY_EMAIL, email);
		}
		if (deposit != null) {
			loginPrefsEditor.putString(KEY_DEPOSIT, deposit);
		}
		if (regId != null) {
			loginPrefsEditor.putString(KEY_PROPERTY_REG_ID, regId);
		}
		// commit changes
		loginPrefsEditor.commit();
	}
	public void deleteAvatarSession(){
		loginPrefsEditor.remove(KEY_AVATAR);
		loginPrefsEditor.commit();
	}
	public void setInChat(boolean status){
		// Storing login value as TRUE
		loginPrefsEditor.putBoolean(IS_IN_CHAT, status);

		// commit changes
		loginPrefsEditor.commit();
	}
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public HashMap<String, String> getUserTempDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_TOKEN_VERIFICATION, loginPreferences.getString(KEY_TOKEN_VERIFICATION, null));
		// return user
		return user;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_ID, loginPreferences.getString(KEY_ID, null));
		user.put(KEY_NAMA, loginPreferences.getString(KEY_NAMA, null));
		user.put(KEY_PHONE, loginPreferences.getString(KEY_PHONE, null));
		user.put(KEY_AVATAR, loginPreferences.getString(KEY_AVATAR, null));
		user.put(KEY_EMAIL, loginPreferences.getString(KEY_EMAIL, null));
		user.put(KEY_DEPOSIT, loginPreferences.getString(KEY_DEPOSIT, null));
		// return user
		return user;
	}
	/**
	 * Clear session details
	 * */

	public void logoutUser(){
		// Clearing all data from Shared Preferences
		DBController.getInstance(_context).deleteBookingHistory();
		DBController.getInstance(_context).deleteBookingProgress(null);
		loginPrefsEditor.clear();
		loginPrefsEditor.commit();
		// After logout redirect user to Loing Activity
	}
	/**
	 * Quick check for login
	 * **/
	// Get LoginActivity State
	public void setTimerRepeatData(String requestId,String requestType, long request_time){
		loginPrefsEditor.putString(KEY_REQUESTID,requestId);
		loginPrefsEditor.putString(KEY_REQUESTTYPE,requestType);
		loginPrefsEditor.putLong(KEY_REQUEST_TIME,request_time);
		loginPrefsEditor.commit();
	}
	public HashMap<String,String> getTimerRepeatData(){
		HashMap<String,String> data = new HashMap<String,String>();
		data.put(KEY_REQUESTID,loginPreferences.getString(KEY_REQUESTID,null));
		data.put(KEY_REQUESTTYPE,loginPreferences.getString(KEY_REQUESTTYPE,null));
		return data;
	}
	public long getRequestTime(){
		return loginPreferences.getLong(KEY_REQUEST_TIME,0);
	}
	public void deleteTimer(){
		ArrayList<String> idArray = new ArrayList<>();
		idArray.add(loginPreferences.getString(KEY_ID, null));
		DBController.getInstance(_context).deleteBookingProgress(idArray);
		loginPrefsEditor.remove(KEY_REQUESTID);
		loginPrefsEditor.remove(KEY_REQUESTTYPE);
		loginPrefsEditor.commit();
	}

	public boolean isLogin(){
		return loginPreferences.getBoolean(IS_LOGIN, false);
	}
	public boolean isInChat(){
		return loginPreferences.getBoolean(IS_IN_CHAT, false);
	}
}
