package com.pmberjaya.indotiki.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.pmberjaya.indotiki.config.Constants;

/**
 * Created by edwin on 25/07/2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = SMSReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message

                    // verification code from sms36
                    String verificationCode = getVerificationCode(message);
                    if(verificationCode == null){
                        return;
                    }
                    Log.e(TAG, "OTP received: " + verificationCode);
                    Intent hhtpIntent = new Intent(context, HttpService.class);
                    hhtpIntent.putExtra("otp", verificationCode);
                    context.startService(hhtpIntent);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        String msgMatch = message.substring(0,30).toLowerCase();
        if(msgMatch.equals(Constants.SMS_MATCH)){
            code = message.substring(30,message.length()).toString();
        }
        else{
            code = null;
        }
        return code;
    }
}
