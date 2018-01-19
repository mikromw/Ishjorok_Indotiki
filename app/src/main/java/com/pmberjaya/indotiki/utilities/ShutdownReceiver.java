package com.pmberjaya.indotiki.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler;

/**
 * Created by edwin on 27/12/2016.
 */

public class ShutdownReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
            context.stopService(new Intent(context, FCMRealtimeDatabaseHandler.class));
        }
    }
}
