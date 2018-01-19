package com.pmberjaya.indotiki.utilities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.SplashScreen;
import com.pmberjaya.indotiki.dao.SessionKey;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.models.account.DeviceDataModel;
import com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Utility
{
    private static Utility instance = null;

    public static Utility getInstance()
    {
        if (instance == null) {
            instance = new Utility();
        }
        return instance;
    }
    public int parseInteger(String valueString){
        int valueInt=0;
        try {
            try {
                double valueDouble = Double.parseDouble(valueString);
                valueInt = (int) valueDouble;
            }catch (NullPointerException ne){
            }
        } catch (NumberFormatException e) {
        }
        return valueInt;
    }
    public double parseDecimal(String valueString)
    {
        double valueDouble = 0.0;
        try
        {
            valueDouble = Double.parseDouble(valueString);

        }
        catch (Exception localException)
        {
        }
        return valueDouble;
    }
    public int getDisplayHeight(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        Activity activity = (Activity) context ;
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight= displaymetrics.heightPixels;
        return screenHeight;
    }
    public int getDisplayWidth(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        Activity activity = (Activity) context ;
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth= displaymetrics.widthPixels;
        return screenWidth;
    }
    public void setBackgroundResource(View view, int backgroundResource){
        int pL = view.getPaddingLeft();
        int pT = view.getPaddingTop();
        int pR = view.getPaddingRight();
        int pB = view.getPaddingBottom();

        view.setBackgroundResource(backgroundResource);
        view.setPadding(pL, pT, pR, pB);
    }
    public LinearLayout.LayoutParams getFoodRestaurantItemHeight(Context context, double multiplier){
        Activity activity = (Activity)context;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float screenWidth = displaymetrics.widthPixels;
        float screenHeight = displaymetrics.heightPixels;
        double imageHeight = screenHeight / multiplier;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) imageHeight); //WRAP_CONTENT param can be FILL_PARENT
//        Float density = context.getResources().getDisplayMetrics().density;
//        if (density <= 0.75) { // LDPI
//            leftMarginWidth = screenWidth * 0.7;
//            topMarginHeight = screenHeight * 0.28;
//        } else if (density <= 1.0) { // MDPI
//            leftMarginWidth = screenWidth * 0.7;
//            topMarginHeight = screenHeight * 0.28;
//        } else if (density <= 1.5) { // HDPI
//            leftMarginWidth = screenWidth * 0.7;
//            topMarginHeight = screenHeight * 0.3;
//        } else if (density <= 2.0) { // XHDPI
//            leftMarginWidth = screenWidth * 0.7;
//            topMarginHeight = screenHeight * 0.32;
//        } else if (density <= 3.0) { // XXHDPI
//            leftMarginWidth = screenWidth * 0.7;
//            topMarginHeight = screenHeight * 0.31;
//        } else if (density <= 4.0) { // XXXHDPI
//            leftMarginWidth = screenWidth * 0.7;
//            topMarginHeight = screenHeight * 0.31;
//        }
        return params;
    }

    public String getPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri,
                projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }



    public static String getAppVersionName(Context paramContext)
    {
        try
        {
            String str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
            return str;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            localNameNotFoundException.printStackTrace();
        }
        return "";
    }
    public  int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public  void intentActivity(Activity activity, String StringClassname) {
        Class<?> className = null;
        if(StringClassname != null) {
            StringClassname = "com.pmberjaya.indotiki.app."+StringClassname;
            try {
                className = Class.forName(StringClassname);
                Intent intent = new Intent(activity, className);
                activity.startActivity(intent);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public String getTokenApi(Context context) {
        SessionKey key= new SessionKey(context);
        HashMap<String,String> apikey = new HashMap<String,String>();
        apikey= key.getKey();
        String token = apikey.get(SessionKey.KEY);
//        String token = "g4o88sckc4ck080gocckwgkc8ckscw8c884swg88";
        return token;
    }

    public static String formatTimeStamp(long timeStamp, String formatTime){
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(formatTime);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return formatter.format(calendar.getTime());
    }

    public String uppercaseText(String str){
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

    public String getDayName(Context context){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        String thisDay = null;
        if(day==1){
            thisDay=context.getResources().getString(R.string.sunday);
        }else if(day==2){
            thisDay=context.getResources().getString(R.string.monday);
        }else if(day==3){
            thisDay=context.getResources().getString(R.string.tuesday);
        }else if(day==4){
            thisDay=context.getResources().getString(R.string.wednesday);
        }else if(day==5){
            thisDay=context.getResources().getString(R.string.thursday);
        }else if(day==6) {
            thisDay = context.getResources().getString(R.string.friday);
        }else if(day==7){
            thisDay= context.getResources().getString(R.string.saturday);
        }
        return thisDay;
    }


    public void showSimpleAlertDialog( Context context, String title, String msg, String positiveMessage,DialogInterface.OnClickListener positiveListener, String negativeMessage, DialogInterface.OnClickListener negativeListener, boolean isCancelable) {
        AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(title!=null) {
            builder.setTitle(title);
        }
        builder.setMessage(msg)
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher);
        if(positiveMessage!=null&&positiveListener!=null) {
            builder.setPositiveButton(positiveMessage, positiveListener);
        }else if(positiveMessage!=null){
            builder.setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if(negativeMessage!=null&&negativeListener!=null) {
            builder.setNegativeButton(negativeMessage, negativeListener);
        }else if(negativeMessage!=null){
            builder.setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog = builder.create();
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }

    }

    int invalidApiKeyDialogCount = 0;

    public void showInvalidApiKeyAlert(final Context context, String msg) {
        if(invalidApiKeyDialogCount==0) {
            invalidApiKeyDialogCount = 1;
            final Activity activity = (Activity) context;
            SessionKey session = new SessionKey(context);
            session.DeleteKey();
            SessionManager sessionManager = new SessionManager(context);
            sessionManager.logoutUser();
            activity.stopService(new Intent(context, FCMRealtimeDatabaseHandler.class));
            AlertDialog dialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.alert))
                    .setMessage(msg)
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                            dialogInterface.dismiss();
                            invalidApiKeyDialogCount = 0;
                        }
                    });
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    public boolean isValidUntilNow(String timeData){
        Calendar secondsTime = Calendar.getInstance();
        int secondsTimes = Integer.parseInt(timeData);
        secondsTime.setTimeInMillis((long)secondsTimes*1000);

        int day = secondsTime.get(Calendar.DAY_OF_MONTH);
        int month = secondsTime.get(Calendar.MONTH)+1;
        int year = secondsTime.get(Calendar.YEAR);
        if(year>=2017){
            if (month >= 4) {
                if (day >= 1) {
                    return true;
                }
            }
        }else {
            return false;
        }
        return false;
    }
    public boolean isOpened(String openTime, String closeTime){
        boolean isOpened = false;
        if(openTime==null||closeTime==null){
            isOpened = false;
        }else {
            Calendar c = Calendar.getInstance();
            int hour_of_day = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            String[] openTimeArray = openTime.split(":");
            String[] closeTimeArray = closeTime.split(":");
            int openTimeHour = Integer.parseInt(openTimeArray[0]);
            int openTimeMinute = Integer.parseInt(openTimeArray[1]);
            int closeTimeHour = Integer.parseInt(closeTimeArray[0]);
            int closeTimeMinute = Integer.parseInt(closeTimeArray[1]);
            if(openTimeHour>closeTimeHour){
                if ((hour_of_day > openTimeHour && hour_of_day < 23) || hour_of_day < closeTimeHour) {
                    isOpened = true;
                }else if (hour_of_day == openTimeHour || hour_of_day == closeTimeHour) {
                    if (hour_of_day == openTimeHour) {
                        if (minute >= openTimeMinute && minute <= 59) {
                            isOpened = true;
                        } else {
                            isOpened = false;
                        }
                    } else if (hour_of_day == closeTimeHour) {
                        if (minute >= 0 && minute <= closeTimeMinute) {
                            isOpened = true;
                        } else {
                            isOpened = false;
                        }
                    }
                } else {
                    isOpened = false;
                }
            }else if(openTimeHour<closeTimeHour){
                if (hour_of_day > openTimeHour && hour_of_day < closeTimeHour) {
                    isOpened = true;
                } else if (hour_of_day == openTimeHour || hour_of_day == closeTimeHour) {
                    if (hour_of_day == openTimeHour) {
                        if (minute >= openTimeMinute && minute <= 59) {
                            isOpened = true;
                        } else {
                            isOpened = false;
                        }
                    } else if (hour_of_day == closeTimeHour) {
                        if (minute >= 0 && minute <= closeTimeMinute) {
                            isOpened = true;
                        } else {
                            isOpened = false;
                        }
                    }
                } else {
                    isOpened = false;
                }
            }

        }
        return isOpened;
    }

    public boolean isTextNotNullEmpty(String text) {
        if (text == null || TextUtils.isEmpty(text)) {
            return false;
        }
        return true;
    }

    public boolean isInternetOn(Context paramContext)
    {
        NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo == null) || (!localNetworkInfo.isAvailable())) {
            return false;
        }
        else{
            return true;
        }
    }
    public long getTimeStamp(String date) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(date);

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        long time = c.getTimeInMillis();
        long curr = System.currentTimeMillis();
        long diff = time;    //Time difference in milliseconds
        return diff/1000;
    }
    public boolean checkPlayServices(Context paramContext) {
        Activity activity = (Activity)paramContext;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(paramContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else
                activity.finish();
            return false;
        }
        return true;
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public String convertPrice(double paramString)
    {
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            otherSymbols.setDecimalSeparator(',');
            otherSymbols.setGroupingSeparator('.');
            DecimalFormat dfnd = new DecimalFormat("#,###", otherSymbols);

            String a = dfnd.format(paramString);
            return a;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "0";
    }
    public String convertPrice(String paramString)
    {
        try {
            BigDecimal localBigDecimal = new BigDecimal(paramString);
            DecimalFormatSymbols localDecimalFormatSymbols = DecimalFormatSymbols.getInstance();
            localDecimalFormatSymbols.setGroupingSeparator('.');
            return new DecimalFormat("###,###.##", localDecimalFormatSymbols).format(localBigDecimal.longValue());
        }catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "0";
    }
    public String convertPriceString(String paramString)
    {
        try {
            BigDecimal localBigDecimal = new BigDecimal(paramString);

        }catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "0";
    }
    public String convertint(String paramString)
    {
        try {
            BigDecimal localBigDecimal = new BigDecimal(paramString);
            DecimalFormatSymbols localDecimalFormatSymbols = DecimalFormatSymbols.getInstance();
            localDecimalFormatSymbols.setGroupingSeparator('.');
            return new DecimalFormat("########", localDecimalFormatSymbols).format(localBigDecimal.longValue());
        }catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "0";
    }
    public String shuffle(){
        String input = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return "inki_"+output.toString().substring(0,8);
    }

    public String getCarrierOperatorSim(Context mContext){
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName()+", "+manager.getSimOperatorName()   ;
        return carrierName;
    }

    public String formatDate(String paramString) {
        try
        {
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("in"));
            String str = localSimpleDateFormat.format(localSimpleDateFormat.parse(paramString));
            return str;
        }
        catch (Exception localException)
        {
        }
        return paramString;
    }

    public static long toAscii(String s){
        StringBuilder sb = new StringBuilder();
        String ascString = null;
        long asciiInt;
        for (int i = 0; i < s.length(); i++){
            sb.append((int)s.charAt(i));
            char c = s.charAt(i);
        }
        ascString = sb.toString();
        asciiInt = Long.parseLong(ascString);
        return asciiInt;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            float px = 300 * (listView.getResources().getDisplayMetrics().density);
            listItem.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static int getColor(Resources paramResources, int paramInt, Resources.Theme paramTheme)
            throws Resources.NotFoundException
    {
        if (Build.VERSION.SDK_INT >= 23) {
            return paramResources.getColor(paramInt, paramTheme);
        }
        return paramResources.getColor(paramInt);
    }

    public int getDimensionFromValuesResources(Resources paramResources, int paramInt){
        int valueInPixels = (int) paramResources.getDimension(paramInt);
        return valueInPixels;
    }
    public int getDimensionFromValuesResourcesInDp(Resources paramResources, int paramInt){
        int valueInDp = (int) (paramResources.getDimension(paramInt) /paramResources.getDisplayMetrics().density);
        return valueInDp;
    }
    public String phoneFormat(String phone)
    {
        if (phone!=null&&phone.substring(0, 1).equals("0")) {
            phone= phone.substring(0, 1).replace("0", "+62") + phone.substring(1, phone.length());
        } else if (phone!=null&&phone.substring(0, 2).equals("62")) {
            phone = phone.substring(0, 2).replace("62", "+62") + phone.substring(2, phone.length());
        } else if (phone!=null&&phone.substring(0, 3).equals("620")) {
            phone = phone.substring(0, 3).replace("620", "+62") + phone.substring(3, phone.length());
        }else if (phone!=null&&phone.substring(0, 4).equals("+620")) {
            phone = phone.substring(0, 4).replace("+620", "+62") + phone.substring(4, phone.length());
        }
        return phone;
    }
    public boolean checkIfStringIsNotNullOrEmpty(String text) {
        if(text!=null&&text.trim().length()!=0){
            return true;
        }else {
            return false;
        }
    }

    public InputFilter getEditTextFilterEmoji()
    {
        return new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                CharSequence sourceOriginal = source;
                source = replaceEmoji(source);
                end = source.toString().length();

                if (end == 0) return ""; //Return empty string if the input character is already removed

                if (! sourceOriginal.toString().equals(source.toString()))
                {
                    char[] v = new char[end - start];
                    TextUtils.getChars(source, start, end, v, 0);

                    String s = new String(v);

                    if (source instanceof Spanned)
                    {
                        SpannableString sp = new SpannableString(s);
                        TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
                        return sp;
                    }
                    else
                    {
                        return s;
                    }
                }
                else
                {
                    return null; // keep original
                }
            }

            private String replaceEmoji(CharSequence source)
            {

                String notAllowedCharactersRegex = "[^a-zA-Z0-9@#\\$%\\&\\-\\+\\(\\)\\*;:!\\?\\~`£\\{\\}\\[\\]=\\.,_/\\\\\\s'\\\"<>\\^\\|÷×]";
                return source.toString()
                        .replaceAll(notAllowedCharactersRegex, "");
            }

        };
    }
    public Uri getLocalBitmapUri(Bitmap bmp, Activity activity) {
        Uri bmpUri = null;
        try {
            File file =  new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public DeviceDataModel getDeviceData(Context context){
        DeviceDataModel deviceDataModel = new DeviceDataModel();

        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        deviceDataModel.imei = tManager.getDeviceId();
        deviceDataModel.simCard = tManager.getSimOperatorName();
        deviceDataModel.phoneNumber = tManager.getLine1Number();
        deviceDataModel.deviceName =  Build.MANUFACTURER
                + " " + Build.MODEL;
        deviceDataModel.os = Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        return deviceDataModel;
    }

    public void dismissProgressDialog(ProgressDialog progressDialog){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    public ObjectAnimator manageBlinkEffect(View objectView, int color) {
        ObjectAnimator anim = ObjectAnimator.ofInt(objectView, "backgroundColor", Color.WHITE, color,
                Color.WHITE);
        anim.setDuration(3000);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
        return anim;
    }

    public RequestOptions setGlideOptions(int width, int height) {
        RequestOptions widthHeightPhoto = new RequestOptions()
                .fitCenter()
                .override(width, height);
        return widthHeightPhoto;
    }
    public View showErrorLayout(Context context, RelativeLayout mainLayout, String errorMessage, View.OnClickListener errorClickListener, int layoutAboveId, int layoutBelowId) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater!=null) {
            View layout = inflater.inflate(R.layout.view_error_layout, null);
            TextView tvErrorMessage = layout.findViewById(R.id.tvErrorMessage);
            LinearLayout errorButton = layout.findViewById(R.id.btnError);
            if(errorMessage!=null) {
                tvErrorMessage.setText(errorMessage);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            if(layoutBelowId!=0) {
                params.addRule(RelativeLayout.BELOW, layoutBelowId);
            }
            if(layoutAboveId!=0) {
                params.addRule(RelativeLayout.ABOVE, layoutAboveId);
            }
            layout.setLayoutParams(params);
            errorButton.setOnClickListener(errorClickListener);
            mainLayout.addView(layout);
            return layout;
        }
        return null;
    }
    public View showErrorLayout(Context context, LinearLayout mainLayout, String errorMessage, View.OnClickListener errorClickListener) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater!=null) {
            View layout = inflater.inflate(R.layout.view_error_layout, null);
            TextView tvErrorMessage = layout.findViewById(R.id.tvErrorMessage);
            LinearLayout errorButton = layout.findViewById(R.id.btnError);
            if(errorMessage!=null) {
                tvErrorMessage.setText(errorMessage);
            }
            errorButton.setOnClickListener(errorClickListener);
            mainLayout.addView(layout);
            return layout;
        }
        return null;
    }

    public Snackbar showSnackbar(CoordinatorLayout layoutCoordinator, String message, String btnMessage, View.OnClickListener snackbarBtnClickListener, int duration) {
        final Snackbar snackbar = Snackbar.make(layoutCoordinator, message , duration);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        if(snackbarBtnClickListener==null){
            snackbarBtnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            };
        }
        snackbar.setAction(btnMessage, snackbarBtnClickListener);
        snackbar.show();
        return snackbar;
    }
}
