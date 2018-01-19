package com.pmberjaya.indotiki.utilities;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;

import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.dao.SessionManager;

/**
 * Created by edwin on 25/07/2016.
 */
public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();
    private ProgressDialog pDialog;
    private String token;
    private SessionManager session;
    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            session = new SessionManager(this);
            String otp = intent.getStringExtra("otp");
            verifyOtp(otp);
            /*if(session.isInVerificationSection()) {
                String otp = intent.getStringExtra("otp");
                verifyOtp(otp);
            }*/
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp) {
        Intent broadcastIntent = new Intent(Constants.BROADCAST_VERIFICATION_SMS);
        broadcastIntent.putExtra("verification_code",otp);
        sendBroadcast(broadcastIntent);
    }

}