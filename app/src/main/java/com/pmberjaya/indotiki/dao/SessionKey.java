package com.pmberjaya.indotiki.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by edwin on 3/30/2016.
 */

public class SessionKey {
        // Shared Preferences
        private SharedPreferences keyPreferences;
        // Editor for Shared preferences
        private SharedPreferences.Editor keyPrefsEditor;
        // Context

        Context _context;
        String api;
        ProgressDialog pDialog;
        // Shared pref mode
        int PRIVATE_MODE = 0;

        // Sharedpref file name
        private static final String PREF_NAME = "keyPrefs";

        // All Shared Preferences Keys
        private static final String IS_HAVE_KEY = "IsHaveKey";

        // User name (make variable public to access from outside)
        public static final String KEY = "key";


        // Constructor
        public SessionKey(Context context){
            this._context = context;
            keyPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            keyPrefsEditor = keyPreferences.edit();
        }

        /**
         * Create login session
         **/

        public void createKey(String key){
            // Storing login value as TRUE
            keyPrefsEditor.putBoolean(IS_HAVE_KEY, true);

            // Storing email in pref
            keyPrefsEditor.putString(KEY, key);



            // commit changes
            keyPrefsEditor.commit();
        }

        /**
         * Check login method wil check user login status
         * If false it will redirect user to login page
         * Else won't do anything
         * */
        public void checkHaveKey(){
            // Check login status
            if(!this.isHaveKey()){
                // user is not logged in redirect him to LoginActivity Activity
			/* Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			i.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Toast.makeText(_context, "Silahkan login untuk menampilkan pencarian tersimpan", Toast.LENGTH_LONG).show();
			// Staring LoginActivity Activity
			_context.startActivity(i); */
            }

        }

        /**
         * Get stored session data
         * */
        public HashMap<String, String> getKey(){
            HashMap<String, String> key = new HashMap<String, String>();
            // user name
            key.put(KEY, keyPreferences.getString(KEY, null));


            // return user
            return key;
        }



        /*public void logoutKey(Activity a){
            // Clearing all data from Shared Preferences
            api = Token.getInstance(_context).getToken();
            dologoutkey(api, a);

            // After logout redirect user to Loing Activity

        }*/

        public void updateKey(String key){
            // Storing login value as TRUE
            // Storing email in pref
            keyPrefsEditor.putString(KEY, key);



            // commit changes
            keyPrefsEditor.commit();
        }


        public void DeleteKey(){
            // Clearing all data from Shared Preferences
            keyPrefsEditor.clear();
            keyPrefsEditor.commit();

            // After logout redirect user to Loing Activity

        }



    /*   private void dologoutkey(String api, Activity a)
        {
            if (Utility.getInstance().isInternetOn(_context))
            {
                Log.d("dologoutkey", "Jalannn");
                pDialog = ProgressDialog.show(a, "", "loading...");
                UserController.getInstance(_context).logoutuser(api, a, pDialog);
                return ;
            }
            else{
                // pDialog.dismiss();
                Toast.makeText(_context, _context.getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }


        }*/



        /**
         *
         * Quick check for login
         * **/
        // Get LoginActivity State
        public boolean isHaveKey(){
            return keyPreferences.getBoolean(IS_HAVE_KEY, false);
        }
    }


