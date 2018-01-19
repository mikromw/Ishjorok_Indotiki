package com.pmberjaya.indotiki.config;

import android.content.Intent;

/**
 * Created by GleasonK on 6/8/15.
 *
 * Constants used by this chatting application.
 * TODO: Replace PUBLISH_KEY and SUBSCRIBE_KEY with your personal keys.
 * TODO: Register app for GCM and replace GCM_SENDER_ID
 */
public class Constants {
    public static final String CHAT_OWNERID    = "chatownerid";
    public static final String CHAT_USERNAME    = "username";
    public static final String CHAT_MESSAGE    = "message";
    public static final String CHAT_LINK_IMAGE   = "link_image";
    public static final String CHAT_LINK_IMAGE_SMALL   = "link_image_small";
    public static final String CHAT_LOCATION_IMAGE= "location_image";
    public static final String CHAT_USER_MODEL= "userChatModel";
    public static final String CHAT_FILE_MODEL= "fileModel";
    public static final String CHAT_MAP_MODEL= "mapModel";

    public static final String STATE_LOGIN = "login";
    public static final String STATE_EDITPROFILE= "editProfile";
    public static final String STATE_TYPE = "stateType";
    public static final String STATE_TIME = "stateTime";

    public static final String item_type= "item_type"; // Get this from
    public static final String GCM_USER_FROM = "user_name"; // Get this from
    public static final String GCM_USER_PHONE_FROM = "user_phone"; // Get this from
    public static final String GCM_USER_AVATAR_FROM = "user_avatar"; // Get this from
    public static final String GCM_MESSAGE_FROM= "message"; // Get this from
    public static final String GCM_REQUEST_ID= "request_id"; // Get this from
    public static final String GCM_REQUEST_TYPE= "request_type"; // Get this from
    public static final String GCM_USER_ID= "user_id_booking"; // Get this from
    public static final String GCM_CHANNEL= "channel"; // Get this from
    public static final String LONGITUDE="longitude";
    public static final String LATITUDE="latitude";
    public static final String ADDRESS="address";
    public static final String GENERAL_PROMO="generalPromo";
    public static final String MY_PROMO="myPromo";

    public static final String TRANSPORT_MOTORCYCLE="motorcycle_taxi";

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String TRANSPORT = "transport";
    public static final String COURIER = "courier";
    public static final String FOOD= "food";
    public static final String TAXI="taxi";
    public static final String CAR="car";
    public static final String MART = "mart";

    public static final String TRANSPORTATION_TYPE = "transportation_type";

    public static final String BOOKING_DATA_PARCELABLE = "booking_data_parcelable";
    public static final String PROMO_CODE_PARCELABLE = "promo_code_parcelable";
    public static final String RESTAURANT_CATEGORY_ENUM = "restaurant_category_enum";
    public static final String RESTAURANT_SUB_CATEGORY_ARRAY = "restaurant_sub_category_array";
    public static final String RESTAURANT_NEARME_ARRAY_DATA = "restaurant_nearme_array_data";

    public static final int STATE_LOGIN_CODE = 106;
    public static final String TAG = "GCM Android Example";

    // Broadcast reciever name to show gcm registration messages on screen
    public static final String BROADCAST_REALTIME_DB_RECEIVER = "com.pmberjaya.indotiki.gcm";
    public static final String BROADCAST_TIMER = "com.pmberjaya.indotiki.timer";
    public static final String BROADCAST_VERIFICATION_SMS= "com.pmberjaya.indotiki.VerificationLogin";

    // Service name to show user messages on screen
    public static final String REALTIME_DB_SERVICE = "com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler";
    public static final String TIME_SERVICE = "com.pmberjaya.indotiki.services.fcm.TimeService";
    public static final String BROADCAST_DOWNLOAD_RECEIVER = "com.pmberjaya.indotiki.download";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String SMS_MATCH = "kode aktivasi indotiki adalah ";


    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String REQUEST_TYPE = "request_type";
    public static final String USE_PROMO_LATER = "use_promo_later";
    public static final String INTENT_TIP_RIDER = "tip_rider";

    public static final String STATE = "state";
    public static final String AUTOLOAD = "autoload";
    public static final String PLACE_TYPE = "place_type";
    public static final String PLACE= "place";
    public static final String PLACE_DETAILS= "place_details";
    public static final String APP_MEMBER = "0";

    public static final String PROMO_POPUP_CODE = "autoload";
    public static final String RESTAURANT_DETAIL_NONE_ACTION = "restaurant_detail_none_action";
    public static final String RESTAURANT_DETAIL_SIGNATURE_ACTION =  "restaurant_detail_signature_action";
    public static final String RESTAURANT_DETAIL_SEARCH_ACTION =  "restaurant_detail_search_action";
    public static final String MENU_CATEGORY_ID = "menu_category_id";
    public static final String MENU_CATEGORY_NAME= "menu_category_name";
    public static final String MENU_ID = "menu_id";
    public static final String ACCOUNT_VERIF_REGISTER = "account_verif_register";
    public static final String ACCOUNT_VERIF_EDIT_PROFILE= "account_verif_editprofile";
    public static final String ACCOUNT_VERIF_LOGIN= "account_verif_login";
    public static final String USER_DATA = "userData";
    public static final String TOKEN_VERIF = "verificationToken";
    public static final String SEARCH_DRIVER = "search_driver";
}
