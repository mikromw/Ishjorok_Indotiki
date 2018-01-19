package com.pmberjaya.indotiki.app.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.callbacks.chat.UploadChatImageCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.controllers.UtilityController;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.interfaces.chat.ClickListenerChat;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.interfaces.chat.UploadChatImageInterface;
import com.pmberjaya.indotiki.interfaces.gmaps.GeocoderLocationInterface;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.models.chat.UserChatModel;
import com.pmberjaya.indotiki.models.chat.ChatMessage;
import com.pmberjaya.indotiki.models.chat.FileModel;
import com.pmberjaya.indotiki.models.gmaps.GeocoderLocationGmapsData.Result;
import com.pmberjaya.indotiki.models.chat.MapModel;
import com.pmberjaya.indotiki.models.others.CheckStatusBookingData;
import com.pmberjaya.indotiki.services.DownloadService;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ChatActivity extends BaseActivity implements ProgressRequestBody.UploadCallbacks, ClickListenerChat {
    private SessionManager session;
    //    private Pubnub mPubNub;
    private PubNub mPubnub4;
    private EditText mMessageET;
    private RecyclerView mListView;
    private ChatAdapter mChatAdapter;
    private LinearLayout loadinglayout;
    private LinearLayout msg_box;
    private String channel_pubnub;
    private String userid;
    private String username;
    private String request_id;
    private String request_type;
    private String driver_id;
    private String message;
    private TextView tv_error_timeout;
    private LinearLayout bt_try_again;
    private RelativeLayout error_layout;
    private boolean runSetNotification= false;
    DBController dbController;
    ArrayList<ChatMessage> chatMsgs;
    private String itemPhotoData;
    int checkitemoption=-1;
    private AlertDialog adialog;
    private Uri selectedImageUri;
    private CharSequence[] itemoption;
    private static final int RESULT_LOAD_IMAGE_REQUEST_CODE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private String imagepath;
    private String imageName;
    public static final int progress_bar_type = 0;
    private ProgressDialog progressBarNya;
    private File fileUpload;
    private RelativeLayout bt_upload_file;
    private String pathFile;
    private File savePhoto;
    private LinearLayout nobookinglayout;
    private RelativeLayout bookinglayout;
    private ChatMessage chatImage;
    private LinearLayoutManager linearLayoutManager;
    private int totalFileSize;
    private boolean onBackpress = true;
    private TextView tv_error_message;
    private String driver_name;
    private ImageView img_driver;
    private TextView tv_nama_driver;
    private TextView tv_nohp;
    private String driver_avatar;
    private String driver_nohp;
    double latitude;
    double longitude;
    private LinearLayout bt_sendlocation;
    private RelativeLayout call_driver;
    private String phone;
    private String avatar;
    private String status;
    private UtilityController utilityController;
    private LocationSessionManager locationSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        initToolbar();
        renderViews();
        getIntentExtra();
        initDB();
        initSession();
        utilityController = UtilityController.getInstance(ChatActivity.this);
    }
    String address;
    private void init() {
        mChatAdapter = new ChatAdapter(this, new ArrayList<ChatMessage>(),request_id, request_type, userid, this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(linearLayoutManager);
        mListView.setAdapter(mChatAdapter);
        bt_upload_file.setOnClickListener(uploadPhotoNew);
        bt_try_again.setOnClickListener(try_again_listener);
        bt_sendlocation.setOnClickListener(shareLocationListener);
        call_driver.setOnClickListener(callDriver);
        registerDownloadReceiver();
        initPubNub();
    }
    private View.OnClickListener callDriver = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent callintent = new Intent(Intent.ACTION_DIAL);
            callintent.setData(Uri.parse("tel:" + driver_nohp));
            startActivity(callintent);
        }
    };
    private void renderViews(){
        itemoption = new CharSequence[]{getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
        msg_box = findViewById(R.id.msg_box);
        loadinglayout = findViewById(R.id.layout_loading);
        bt_upload_file = findViewById(R.id.bt_upload_file);
        bt_try_again = findViewById(R.id.btnError);
        error_layout = findViewById(R.id.layoutError);
        tv_error_message = findViewById(R.id.tvErrorMessage);
        img_driver = findViewById(R.id.img_driver);
        tv_nama_driver = findViewById(R.id.tv_nama_driver);
        tv_nohp = findViewById(R.id.tv_nohp);
        this.mMessageET = findViewById(R.id.message_et);

        mListView = findViewById(R.id.list);
        mListView.setHasFixedSize(true);
        bt_sendlocation = findViewById(R.id.bt_send_location);
        call_driver = findViewById(R.id.call_driver);
        nobookinglayout = findViewById(R.id.nobookinglayout);
        bookinglayout = findViewById(R.id.bookinglayout);
    }

    private void setRiderProfileHeader() {
        tv_nama_driver.setText(driver_name);
        tv_nohp.setText(driver_nohp);
        if (driver_avatar!=null && !driver_avatar.equals("")) {
            Glide.with(this).load(driver_avatar).apply(Utility.getInstance().setGlideOptions(300,400)).into(img_driver);
        } else {
//            PicassoLoader.loadProfileFail(this, img_driver, R.mipmap.img_no_avatar_driver);
        }
    }

    private void initDB(){
        dbController = DBController.getInstance(ChatActivity.this);
    }
    private void initSession(){
        session = new SessionManager(ChatActivity.this);
        HashMap<String,String> userData = session.getUserDetails();
        userid = userData.get(SessionManager.KEY_ID);
        username = userData.get(SessionManager.KEY_NAMA);
        phone = userData.get(SessionManager.KEY_PHONE);
        avatar= userData.get(SessionManager.KEY_AVATAR);
        locationSessionManager= new LocationSessionManager(ChatActivity.this);
        HashMap<String,String> locationMap = locationSessionManager.getLatLng();
        latitude = Utility.getInstance().parseDecimal(locationMap.get(locationSessionManager.KEY_LATITUDE));
        longitude= Utility.getInstance().parseDecimal(locationMap.get(locationSessionManager.KEY_LONGITUDE));
    }
    private void getIntentExtra(){
        Intent i = getIntent();
        request_id= i.getStringExtra("request_id");
        request_type= i.getStringExtra("request_type");
        driver_id= i.getStringExtra("user_id_booking");
        channel_pubnub= i.getStringExtra("channel");
        driver_name=i.getStringExtra("user_name");
        driver_avatar=i.getStringExtra("user_avatar");
        driver_nohp =i.getStringExtra("user_phone");

        getCheckStatusBookingChat(request_id, request_type);
        setRiderProfileHeader();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.tool_bar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    //
    public void SetListViewAdapter(ArrayList<ChatMessage> chatMessages) {
        loadinglayout.setVisibility(View.GONE);
        if(mChatAdapter==null) {
            mChatAdapter = new ChatAdapter(this, new ArrayList<ChatMessage>(),request_id, request_type, userid, this);
        }
        mListView.setAdapter(mChatAdapter);
    }
    private View.OnClickListener shareLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String status = "pending";

            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(address)) {
                MapModel mapModel = new MapModel(String.valueOf(latitude), String.valueOf(longitude), address);
                UserChatModel userChatModel = new UserChatModel(userid, username, MEMBER_USERLEVEL);
                FileModel fileModel = new FileModel();
                ChatMessage chatMsg = new ChatMessage(null, System.currentTimeMillis(), status, null, userChatModel, fileModel, null, mapModel);
                String id = dbController.insertChatHistory(chatMsg, request_id, request_type);
                chatMsg.setChat_database_id(id);
                mChatAdapter.addMessage(chatMsg);
                message = "Hai, Lokasi saya di " + address;
                publish(chatMsg);
                mListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }else{
                getCurrentAddress(String.valueOf(latitude), String.valueOf(longitude));
            }
        }
    };

    private View.OnClickListener try_again_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            renderloadingView();
            history();
        }
    };

    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
    }
//    public void renderErrorView(){
//        tv_error_message.setText(getResources().getString(R.string.currently_unavailable));
//        msg_box.setVisibility(View.GONE);
//        loadinglayout.setVisibility(View.GONE);
//        error_layout.setVisibility(View.VISIBLE);
//    }

    public void renderErrorView(PNStatus status){
        PNErrorData pnErrorData = status.getErrorData();
        if(pnErrorData!=null) {
            String information = pnErrorData.getInformation();
            Exception exception = pnErrorData.getThrowable();
            String errorMessage=null;
            if(exception!=null) {
                errorMessage = exception.getMessage();
            }
            tv_error_message.setText("Error" + errorMessage+", info:"+information);
        }else {
            tv_error_message.setText("Error: something wrong");
        }
        msg_box.setVisibility(View.GONE);
        loadinglayout.setVisibility(View.GONE);
        bookinglayout.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
    }
    /**
     * Instantiate PubNub object with username as UUID
     *   Then subscribe to the current channel with presence.
     *   Finally, populate the listview with past messages from history
     */
    private void initPubNub(){
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Config.SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Config.PUBLISH_KEY);
        pnConfiguration.setSecure(true);
        pnConfiguration.setUuid(this.userid);
        pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY);
        pnConfiguration.setPresenceTimeoutWithCustomInterval(310,20);
        this.mPubnub4 = new PubNub(pnConfiguration);
        history();
        hereNow(false);
        this.mPubnub4.addListener(subscribeCallback);
        mPubnub4.subscribe()
                .channels(Arrays.asList(this.channel_pubnub)) // subscribe to channels information
                .withPresence()
                .execute();
        SetListViewAdapter(chatMsgs);
    }

    private String action;
    private int MEMBER_USERLEVEL = 0;
    private int DRIVER_USERLEVEL = 1;
    public SubscribeCallback subscribeCallback = new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {
            if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
//                hereNow(false);
//                String state_type = "login";
//                setState(state_type);
            }
            switch (status.getOperation()) {
                // let's combine unsubscribe and subscribe handling for ease of use
                case PNSubscribeOperation:
                case PNUnsubscribeOperation:
                    // note: subscribe statuses never have traditional
                    // errors, they just have categories to represent the
                    // different issues or successes that occur as part of subscribe
                    switch(status.getCategory()) {
                        case PNConnectedCategory:
                            // this is expected for a subscribe, this means there is no error or issue whatsoever
                        case PNReconnectedCategory:
                            // this usually occurs if subscribe temporarily fails but reconnects. This means
                            // there was an error but there is no longer any issue
                        case PNDisconnectedCategory:
                            // this is the expected category for an unsubscribe. This means there
                            // was no error in unsubscribing from everything
                        case PNUnexpectedDisconnectCategory:
                            // this is usually an issue with the internet connection, this is an error, handle appropriately
                            // retry will be called automatically
                        case PNAccessDeniedCategory:
                            // this means that PAM does allow this client to subscribe to this
                            // channel and channel group configuration. This is another explicit error
                        default:
                            // More errors can be directly specified by creating explicit cases for other
                            // error categories of `PNStatusCategory` such as `PNTimeoutCategory` or `PNMalformedFilterExpressionCategory` or `PNDecryptionErrorCategory`
                    }

                case PNHeartbeatOperation:
                    // heartbeat operations can in fact have errors, so it is important to check first for an error.
                    // For more information on how to configure heartbeat notifications through the status
                    // PNObjectEventListener callback, consult <link to the PNCONFIGURATION heartbeart config>
                    if (status.isError()) {
                        // There was an error with the heartbeat operation, handle here
                    } else {
                        // heartbeat operation was successful
                    }
                default: {
                    // Encountered unknown status type
                }
            }
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            Log.d("sadfdsa",""+message);
            if (message.getChannel() != null) {
                // Message has been received on channel group stored in
                // message.getChannel()
            }
            else {
                // Message has been received on channel stored in
                // message.getSubscription()
            }
            JsonNode jsonNode = message.getMessage();
            if(jsonNode!=null) {
                String json = jsonNode.toString();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(json);
                    Log.d("My App", obj.toString());
                    JSONObject userModelJObject= obj.getJSONObject(Constants.CHAT_USER_MODEL);
                    String chatowner_id = userModelJObject.getString(Constants.CHAT_OWNERID);
                    if(userid.equals(chatowner_id)){
                    }
                    else{
                        String name = userModelJObject.getString(Constants.CHAT_USERNAME);
                        String msg = obj.getString(Constants.CHAT_MESSAGE);
                        if(msg.equals("null")){
                            msg = null;
                        }
                        JSONObject fileModelJObject= obj.getJSONObject(Constants.CHAT_FILE_MODEL);
                        String link_image = fileModelJObject.getString(Constants.CHAT_LINK_IMAGE);
                        if(link_image.equals("null")){
                            link_image = null;
                        }
                        String link_image_small = fileModelJObject.getString(Constants.CHAT_LINK_IMAGE_SMALL);
                        if(link_image_small.equals("null")){
                            link_image_small = null;
                        }
                        JSONObject mapModelJObject= obj.getJSONObject(Constants.CHAT_MAP_MODEL);
                        String longitude =mapModelJObject.getString(Constants.LONGITUDE);
                        String latitude = mapModelJObject.getString(Constants.LATITUDE);
                        String address = mapModelJObject.getString(Constants.ADDRESS);
                        UserChatModel userChatModel = new UserChatModel(chatowner_id, name, DRIVER_USERLEVEL);
                        FileModel fileModel = new FileModel(link_image, link_image_small, null);
                        MapModel mapModel = new MapModel(String.valueOf(latitude),String.valueOf(longitude), address);
                        final long timeTokenInMilliseconds = message.getTimetoken()/10000;
                        String status = "success";
                        final ChatMessage chatMsg = new ChatMessage(null, timeTokenInMilliseconds, status, msg, userChatModel, fileModel, null,mapModel);
                        String id = dbController.insertChatHistory(chatMsg, request_id, request_type);
                        chatMsg.setChat_database_id(id);
                        dbController.updateChatTimeHistory(request_id, request_type, String.valueOf(message.getTimetoken()));

                        ChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatAdapter.addMessage(chatMsg);
                                String state_type = "read_chat";
                                setState(state_type,String.valueOf(timeTokenInMilliseconds));
                                mListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                            }
                        });
                    }
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                }
            }
        }
        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            Log.d("sadfdsa",""+presence);
            if(presence.getChannel()!=null){
                final String event = presence.getEvent();
                final String uuid = presence.getUuid();
                int occupancy = presence.getOccupancy();
                long timeToken = presence.getTimetoken(); //17 digits
                long timeStamp = presence.getTimestamp(); //10 digits
                if(!userid.equals(uuid)) {
                    if (event != null && event.equals("join")) {
                        action = "join";
                    } else if (event != null && event.equals("leave")) {
                        action = "leave";
                    } else if (event != null && event.equals("state-change")) {
                        JsonNode jsonNode = presence.getState();
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, String> result = mapper.convertValue(jsonNode, Map.class);
                        action = result.get(Constants.STATE_TYPE);
                    }else if (event != null && event.equals("timeout")) {
                        action = "leave";
                    }
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatAdapter.userPresence(uuid, action);
                        }
                    });
                }
            }
        }
    };
    public void publish(final ChatMessage chatMsg){
        this.mPubnub4.publish()
                .message(chatMsg)
                .channel(this.channel_pubnub)
                .shouldStore(true)
                .usePOST(true)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle publish result, status always present, result if successful
                        // status.isError to see if error happened
                        if(status.getStatusCode()==200){
                            final long timeToken = result.getTimetoken();
                            String statusChat = "success";
                            long timeTokenInMilliseconds = timeToken/10000;
                            FileModel fileModel = chatMsg.getFileModel();
                            String link_image = fileModel.getLink_image();
                            String link_image_small = fileModel.getLink_image_small();
                            String location_image = fileModel.getLocation_image();
                            mChatAdapter.updateChatAdapter(chatMsg.getChat_database_id(),statusChat,timeTokenInMilliseconds,fileModel);
                            dbController.updateChat(chatMsg.getChat_database_id(), String.valueOf(timeTokenInMilliseconds),statusChat,link_image,link_image_small, location_image);
                            dbController.updateChatTimeHistory(request_id, request_type, String.valueOf(timeToken));
                            dbController.updateChatHistoryStatus(chatMsg.getChat_database_id(), statusChat);
                            sendChatNotification();
                        }
                        else {
                            FileModel fileModel = chatMsg.getFileModel();
                            String statusChat = "failed";
                            mChatAdapter.updateChatAdapter(chatMsg.getChat_database_id(),statusChat,chatMsg.getTimeStamp(),fileModel);
                            dbController.updateChatHistoryStatus(chatMsg.getChat_database_id(), statusChat);
//                            Toast.makeText(ChatActivity.this, ""+status.getErrorData().getInformation(), Toast.LENGTH_SHORT).show();
                        }
//                        System.out.println("pub timetoken: " + result.getTimetoken());
//                        System.out.println("pub status code: " + status.getStatusCode());
                    }
                });
    }

    /**
     * Update here now number, uses a call to the pubnub hereNow function.
     * @param displayUsers If true, display a modal of users in room.
     */
    public void hereNow(final boolean displayUsers) {
        this.mPubnub4.hereNow()
                // tailor the next two lines to example
                .channels(Arrays.asList(this.channel_pubnub))
                .includeUUIDs(true)
                .async(new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {
                        if (status.isError()) {
                            // handle error
                            return;
                        }
                        for (PNHereNowChannelData channelData : result.getChannels().values()) {
                            System.out.println("---");
                            System.out.println("channel:" + channelData.getChannelName());
                            System.out.println("occupancy: " + channelData.getOccupancy());
                            System.out.println("occupants:");
                            final Set<String> usersOnline = new HashSet<String>();
                            usersOnline.add(userid);
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("uuid: " + occupant.getUuid() + " state: " + occupant.getState());
                                usersOnline.add(occupant.getUuid());
                            }
                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mChatAdapter.setOnlineNow(usersOnline);
//                                    if (displayUsers)
//                                        alertHereNow(usersOnline);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * Called at login time, sets meta-data of users' log-in times using the PubNub State API.
     *   Information is retrieved in getStateLogin
     */
    public void setState(String state_type,String state_time){
        Map<String, String> presence_state = new HashMap<>();
        presence_state.put(Constants.STATE_TYPE, state_type);
        presence_state.put(Constants.STATE_TIME, state_time);
        this.mPubnub4.setPresenceState()
                .channels(Arrays.asList(this.channel_pubnub))
                .state(presence_state) // the new state
                .async(new PNCallback<PNSetStateResult>() {
                    @Override
                    public void onResponse(PNSetStateResult result, PNStatus status) {
                        // on new state for those channels
                        Log.d("sadfdsa",""+result);
                    }
                });

    }


    /**
     * Get state information. Information is deleted when user unsubscribes from channel
     *   so display a user not online message if there is no UUID data attached to the
     *   channel's state
     * @param user
     */
    public void getState(final String user){
        this.mPubnub4.getPresenceState()
                .channels(Arrays.asList(this.channel_pubnub)) // channels to fetch state for
                .uuid(driver_id) // uuid of user to fetch, or omit own uuid
                .async(new PNCallback<PNGetStateResult>() {
                    @Override
                    public void onResponse(PNGetStateResult result, PNStatus status) {
                        // handle me
                        Log.d("sadfdsa",""+result);
                    }
                });


//        Callback callback = new Callback() {
//            @Override
//            public void successCallback(String channel, Object response) {
//                if (!(response instanceof JSONObject)) return; // Ignore if not JSON
//                try {
//                    JSONObject state = (JSONObject) response;
//                    final boolean online = state.has(Constants.STATE_LOGIN);
//                    final long loginTime = online ? state.getLong(Constants.STATE_LOGIN) : 0;
//
//                    ChatActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!online)
//                                Toast.makeText(ChatActivity.this, user + " is not online.", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(ChatActivity.this, user + " logged in since " + Utility.getInstance().formatTimeStamp(loginTime), Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//                    Log.d("PUBNUB", "State: " + response.toString());
//                } catch (JSONException e){ e.printStackTrace(); }
//            }
//        };
//        this.mPubNub.getState(this.channel_pubnub, user, callback);
    }


    /**
     * Subscribe to presence. When user join or leave are detected, update the hereNow number
     *   as well as add/remove current user from the chat adapter's userPresence array.
     *   This array is used to see what users are currently online and display a green dot next
     *   to users who are online.
     */
//    public void presence()  {
//        Callback callback = new Callback() {
//            @Override
//            public void successCallback(String channel, Object response) {
//                Log.i("PN-pres","Pres: " + response.toString() + " class: " + response.getClass().toString());
//                if (response instanceof JSONObject){
//                    JSONObject json = (JSONObject) response;
//                    Log.d("PN-main","Presence: " + json.toString());
//                    try {
//                        final int occ = json.getInt("occupancy");
//                        user = json.getString("uuid");
//
//                        final String action = json.getString("action");
//                        ChatActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mChatAdapter.userPresence(user, action);
//                            }
//                        });
//                    } catch (JSONException e){ e.printStackTrace(); }
//                }
//            }
//
//            @Override
//            public void errorCallback(String channel, PubnubError error) {
//                Log.d("Presence", "Error: " + error.toString());
//            }
//        };
//        try {
//            this.mPubNub.presence(this.channel_pubnub, callback);
//        } catch (PubnubException e) { e.printStackTrace();
//        }
//    }

    /**
     * Get last 100 messages sent on current channel from history.
     */
    public void history(){
        bookinglayout.setVisibility(View.VISIBLE);
        chatMsgs = new ArrayList<>();
        ArrayList<ChatMessage> chatFromDatabase = dbController.getChatHistory(request_id,request_type);
        int chatTotal = chatFromDatabase.size();
        if(chatTotal!=0) {
            insertToChatModelFromDatabase(chatTotal,chatFromDatabase);
        }else{
            insertToChatModelFromPubnub();
        }

    }

    private void insertToChatModelFromDatabase(int chatTotal, ArrayList<ChatMessage> chatMessageArray){
        for (int i = 0; i < chatTotal; i++) {
            if(chatMessageArray.get(i).getStatus()!=null&&chatMessageArray.get(i).getStatus().equals("pending")){
                if(chatMessageArray.get(i).getMessage()!=null){
                    publish(chatMessageArray.get(i));
                }else if(chatMessageArray.get(i).getFileModel().getLocation_image()!=null){
                    String newStatus = "failed";
                    chatMessageArray.get(i).setStatus(newStatus);
//                    mChatAdapter.updateChatAdapter(chatMessageArray.get(i).getChat_database_id(),newStatus, chatMessageArray.get(i).getTimeStamp(), chatMessageArray.get(i).getFileModel());
                    dbController.updateChatHistoryStatus(chatMessageArray.get(i).getChat_database_id(), newStatus);
                }
            }
            chatMsgs.add(chatMessageArray.get(i));
        }
        insertToChatModelFromPubnubContinue();
    }
    private void insertToChatModelFromPubnubContinue(){
//        long start = dbController.getNewestTimeChat(request_id,request_type);
        long start=0;
        long end = dbController.getNewestTimeChat(request_id,request_type);
        this.mPubnub4.history()
                .channel(this.channel_pubnub) // where to fetch history from
                .start(end) // first timestamp
                .includeTimetoken(true)
                .reverse(true) // should go in reverse?
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        boolean isError = status.isError();
                        int statusCode = status.getStatusCode();
                        if(isError==true||statusCode!=200){
                            renderErrorView(status);
                        }else {
                            if (result != null) {
                                Log.d("sadfdsa", "" + result);
                                long endTime = result.getEndTimetoken();
                                String state_type = "read_chat";
                                setState(state_type, String.valueOf(endTime));
                                if (endTime != 0) {
                                    dbController.updateChatTimeHistory(request_id, request_type, String.valueOf(endTime));
                                }
                                List<PNHistoryItemResult> message = result.getMessages();
                                int chatSize = message.size();
                                for (int i = 0; i < chatSize; i++) {
                                    PNHistoryItemResult messageItem = message.get(i);
                                    JsonNode jsonNode = messageItem.getEntry();
                                    if (jsonNode != null) {
                                        String json = jsonNode.toString();
                                        JSONObject obj = null;
                                        try {
                                            obj = new JSONObject(json);
                                            JSONObject userModelJObject = obj.getJSONObject(Constants.CHAT_USER_MODEL);
                                            String owner_id = userModelJObject.getString(Constants.CHAT_OWNERID);
                                            String name = userModelJObject.getString(Constants.CHAT_USERNAME);
                                            String msg = obj.getString(Constants.CHAT_MESSAGE);
                                            if (msg.equals("null")) {
                                                msg = null;
                                            }
                                            JSONObject fileModelJObject = obj.getJSONObject(Constants.CHAT_FILE_MODEL);
                                            String link_image = fileModelJObject.getString(Constants.CHAT_LINK_IMAGE);
                                            if (link_image.equals("null")) {
                                                link_image = null;
                                            }
                                            String link_image_small = fileModelJObject.getString(Constants.CHAT_LINK_IMAGE_SMALL);
                                            if (link_image_small.equals("null")) {
                                                link_image_small = null;
                                            }
                                            String location_image = null;
                                            int userlevel;
                                            if (owner_id.equals(userid)) {
                                                location_image = fileModelJObject.getString(Constants.CHAT_LOCATION_IMAGE);
                                                userlevel = MEMBER_USERLEVEL;
                                            }else{
                                                userlevel = DRIVER_USERLEVEL;
                                            }
                                            JSONObject mapModelJObject= obj.getJSONObject(Constants.CHAT_MAP_MODEL);
                                            String longitude =mapModelJObject.getString(Constants.LONGITUDE);
                                            String latitude = mapModelJObject.getString(Constants.LATITUDE);
                                            String address = mapModelJObject.getString(Constants.ADDRESS);

                                            UserChatModel userChatModel = new UserChatModel(owner_id,name,userlevel);
                                            FileModel fileModel = new FileModel(link_image, link_image_small, location_image);
                                            MapModel mapModel = new MapModel(latitude, longitude, address);

                                            long timeTokenInMilliseconds = messageItem.getTimetoken() / 10000;
                                            String statusChat = "success";
                                            ChatMessage chatMsg = new ChatMessage(null, timeTokenInMilliseconds, statusChat, msg, userChatModel, fileModel, null,mapModel);
                                            String chat_database_id = dbController.insertChatHistory(chatMsg, request_id, request_type);
                                            chatMsg.setChat_database_id(chat_database_id);
                                            chatMsgs.add(chatMsg);
                                        } catch (Throwable t) {
                                            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                                        }
                                    }
                                }
                                ChatActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(ChatActivity.this,"RUNNING",Toast.LENGTH_SHORT).show();
                                        mChatAdapter.setMessages(chatMsgs);
                                        mListView.setVisibility(View.VISIBLE);
                                        msg_box.setVisibility(View.VISIBLE);
                                        loadinglayout.setVisibility(View.GONE);
                                        mListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                                    }
                                });
                            }
                        }
                    }
                });
    }
    private void insertToChatModelFromPubnub(){
        mPubnub4.history()
                .channel(this.channel_pubnub) // where to fetch history from
                .count(100) // how many items to fetch
                .includeTimetoken(true)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        boolean isError = status.isError();
                        int statusCode = status.getStatusCode();
                        if(isError==true||statusCode!=200){
                            renderErrorView(status);
                        }else {
                            Log.d("sadfdsa", "" + result);
                            long endTime = result.getEndTimetoken();
                            String state_type = "read_chat";
                            setState(state_type, String.valueOf(endTime));
                            dbController.insertChatTimeHistory(request_id, request_type, String.valueOf(endTime));
                            List<PNHistoryItemResult> message = result.getMessages();
                            int chatSize = message.size();
                            for (int i = 0; i < chatSize; i++) {
                                PNHistoryItemResult messageItem = message.get(i);
                                JsonNode jsonNode = messageItem.getEntry();
                                if (jsonNode != null) {
                                    String json = jsonNode.toString();
                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(json);
                                        JSONObject userModelJObject = obj.getJSONObject(Constants.CHAT_USER_MODEL);
                                        String owner_id = userModelJObject.getString(Constants.CHAT_OWNERID);
                                        String name = userModelJObject.getString(Constants.CHAT_USERNAME);
                                        String msg = obj.getString(Constants.CHAT_MESSAGE);
                                        int userlevel=0;
                                        if (msg.equals("null")) {
                                            msg = null;
                                        }
                                        JSONObject fileModelJObject = obj.getJSONObject(Constants.CHAT_FILE_MODEL);
                                        String link_image = fileModelJObject.getString(Constants.CHAT_LINK_IMAGE);
                                        if (link_image.equals("null")) {
                                            link_image = null;
                                        }
                                        String link_image_small = fileModelJObject.getString(Constants.CHAT_LINK_IMAGE_SMALL);
                                        if (link_image_small.equals("null")) {
                                            link_image_small = null;
                                        }
                                        String location_image = null;
                                        if (owner_id.equals(userid)) {
                                            location_image = fileModelJObject.getString(Constants.CHAT_LOCATION_IMAGE);
                                            if (location_image.equals("null")) {
                                                location_image = null;
                                            }
                                            userlevel = MEMBER_USERLEVEL;
                                        }else{
                                            userlevel =DRIVER_USERLEVEL;
                                        }
                                        JSONObject mapModelJObject= obj.getJSONObject(Constants.CHAT_MAP_MODEL);
                                        String longitude =mapModelJObject.getString(Constants.LONGITUDE);
                                        String latitude = mapModelJObject.getString(Constants.LATITUDE);
                                        String address = mapModelJObject.getString(Constants.ADDRESS);
                                        UserChatModel userChatModel = new UserChatModel(owner_id, name, userlevel);
                                        FileModel fileModel = new FileModel(link_image, link_image_small, location_image);
                                        MapModel mapModel = new MapModel(latitude, longitude, address);
//
                                        long timeTokenInMilliseconds = messageItem.getTimetoken() / 10000;
                                        String statusChat = "success";
                                        ChatMessage chatMsg = new ChatMessage(null, timeTokenInMilliseconds, statusChat, msg, userChatModel, fileModel, null,mapModel);
                                        String chat_database_id = dbController.insertChatHistory(chatMsg, request_id, request_type);
                                        chatMsg.setChat_database_id(chat_database_id);
                                        chatMsgs.add(chatMsg);
                                    } catch (Throwable t) {
                                        Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                                    }
                                }
                            }
                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(ChatActivity.this,"RUNNING",Toast.LENGTH_SHORT).show();
                                    mChatAdapter.setMessages(chatMsgs);
                                    mListView.setVisibility(View.VISIBLE);
                                    msg_box.setVisibility(View.VISIBLE);
                                    loadinglayout.setVisibility(View.GONE);
                                    mListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * Setup the listview to scroll to bottom anytime it receives a message.
     */
//    private void setupAutoScroll(){
//        this.mChatAdapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
////                mListView.setSelection(mChatAdapter.getCount() - 1);
////                 mListView.smoothScrollToPosition(mChatAdapter.getCount()-1);
//            }
//        });
//    }
//
//    /**
//     * On message click, display the last time the user logged in.
//     */
//
//    private void setupListView(){
//        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ChatMessage chatMsg = mChatAdapter.getItem(position);
//            }
//        });
//    }

    /**
     * Publish message to current channel.
     * @param view The 'SEND' Button which is clicked to trigger a sendMessage call.
     */
    public void sendMessage(View view){
        message = mMessageET.getText().toString();
        if (!Utility.getInstance().checkIfStringIsNotNullOrEmpty(message)) return;
        mMessageET.setText("");
        String status = "pending";
        UserChatModel userChatModel = new UserChatModel(userid, username,MEMBER_USERLEVEL);
        MapModel mapModel = new MapModel();
        FileModel fileModel = new FileModel();
        ChatMessage chatMsg = new ChatMessage(null, System.currentTimeMillis(), status, message, userChatModel, fileModel, null,mapModel);
        String id = dbController.insertChatHistory(chatMsg, request_id, request_type);
        chatMsg.setChat_database_id(id);
        mChatAdapter.addMessage(chatMsg);
        publish(chatMsg);
        mListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }
    public void getCurrentAddress(String latitude_from, String longitude_from)
    {
        String origin = latitude_from+","+longitude_from;
        utilityController.getGeocoderLocationMap(utilityController.geocoderParams(origin), geocoderLocationInterface);
        return;
    }
    GeocoderLocationInterface geocoderLocationInterface = new GeocoderLocationInterface() {
        @Override
        public void onSuccessGetGeocoderLocation(GeocoderLocationGMapsCallback geocoderLocationGMapsCallback) {
            String status= geocoderLocationGMapsCallback.getStatus();
            if (status.equals("OK")) {
                List<Result> result =  geocoderLocationGMapsCallback.getResults();
                if(result.size()!=0) {
                    address = result.get(0).getFormattedAddress();

                }else{
                    Toast.makeText(ChatActivity.this, getResources().getString(R.string.cannot_determine), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChatActivity.this, getResources().getString(R.string.oops_something_went_wrong)+" status: "+status, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onErrorGetGeocoderLocation(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(ChatActivity.this, getResources().getString(R.string.relogin));
                } else {
                }
            }
        }
    };

    private void sendChatNotification()
    {
            runSetNotification=true;
            String api = Utility.getInstance().getTokenApi(ChatActivity.this);
            BookingController.getInstance(ChatActivity.this).sendChatNotification(notificationParams(), api);
            return;
    }
    public Map<String, String> notificationParams(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", this.username);
        if(avatar!=null) {
            params.put("user_avatar", this.avatar);
        }else{
            params.put("user_avatar", "");
        }
        params.put("user_phone", this.phone);
        if(message!=null) {
            params.put("message", message);
        }else{
            params.put("message","");
        }
        params.put("request_id", this.request_id);
        params.put("request_type", this.request_type);
        params.put("channel", this.channel_pubnub);
        params.put("user_id", this.driver_id);
        params.put("user_id_booking", this.userid);
        return params;
    }
    private View.OnClickListener uploadPhotoNew = new View.OnClickListener() {
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle(R.string.option_get_picture);
            builder.setSingleChoiceItems(itemoption,checkitemoption, new DialogInterface.OnClickListener() {
                // indexSelected contains the index of item (of which checkbox checked)

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(getResources().getString(R.string.gallery).equals(itemoption[which]))
                    {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE_REQUEST_CODE);
                        Log.d("intent", ""+i);
                    }
                    else if(getResources().getString(R.string.camera).equals(itemoption[which]))
                    {
                        String fileName = "new-photo-name.jpg";
                        //create parameters for Intent with filename
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
                        //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        //create new Intent
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                    }

                    dialog.dismiss();
                }
            });
            adialog = builder.create();//AlertDialog dialog; create like this outside onClick
            adialog.show();
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImageUri = data.getData();
                setImageActivityForResult(selectedImageUri);
            }
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setImageActivityForResult(selectedImageUri);
            }
        }
    }

    public void setImageActivityForResult(Uri selectedImageUri){
        String status = "pending";
        //Bitmap photo = (Bitmap) data.getData().getPath();

        imagepath = getPath(selectedImageUri);
        imageName = imagepath.substring(imagepath.lastIndexOf("/"));
        fileUpload= new File(imagepath);

        long lengthbmp = fileUpload.length();
        Log.d("LENGTHNYA", "" + lengthbmp);
        if (lengthbmp < 5000000) {
            try {
                String filepath = PicassoLoader.saveImageToFile(ChatActivity.this, imagepath, System.currentTimeMillis(), request_id, request_type);
                UserChatModel userChatModel = new UserChatModel(userid, username, MEMBER_USERLEVEL);
                FileModel fileModel = new FileModel(null,null,filepath);
                MapModel mapModel = new MapModel();
                chatImage= new ChatMessage(null, System.currentTimeMillis(), status, null, userChatModel, fileModel, null,mapModel);
                String id = dbController.insertChatHistory(chatImage, request_id, request_type);
                chatImage.setChat_database_id(id);
                mChatAdapter.addMessage(chatImage);
                mListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                doUploadPhoto(fileUpload, imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
        }
    }
    public void doUploadPhoto(File file, String imageName){
            String api = Utility.getInstance().getTokenApi(ChatActivity.this);
            showDialog(progress_bar_type);
            String MEDIA_TYPE_PNG = "image/" + imageName.substring(imageName.lastIndexOf(".") + 1);
            ProgressRequestBody test = new ProgressRequestBody(file, MEDIA_TYPE_PNG, this);
            String filename ="file\"; filename=\""+imageName.substring(1,imageName.length())+"\" ";
            onBackpress = false;
            BookingController.getInstance(ChatActivity.this).doUploadChatImage(parametersSendRequest(filename,test), this.channel_pubnub,api, uploadChatImageInterface);
            return;
    }

    UploadChatImageInterface uploadChatImageInterface = new UploadChatImageInterface() {
        @Override
        public void onSuccessUploadImage(UploadChatImageCallback response) {
            int sukses = response.getSukses();
            if(sukses==2){
                progressBarNya.dismiss();
                itemPhotoData =  response.getPath();
                String fullpathData= response.getFullpath();
                String small_full_path = response.getSmall_full_path();
                chatImage.getFileModel().setLink_image(itemPhotoData);
                chatImage.getFileModel().setLink_image_small(small_full_path);
                message =null;
                publish(chatImage);
                Log.d("path",">"+itemPhotoData);

            }
            onFinish();
        }

        @Override
        public void onErrorUploadImage(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(ChatActivity.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(ChatActivity.this, getResources().getString(R.string.relogin));
                } else {
                    progressBarNya.dismiss();
                    String statusChat = "failed";
                    mChatAdapter.updateChatAdapter(chatImage.getChat_database_id(),statusChat,System.currentTimeMillis(),chatImage.getFileModel());
                    dbController.updateChatHistoryStatus(chatImage.getChat_database_id(),statusChat);
                    Toast.makeText(ChatActivity.this, getResources().getString(R.string.upload_image_failed), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public Map<String, ProgressRequestBody> parametersSendRequest(String filename, ProgressRequestBody test) {
        Map<String, ProgressRequestBody> params = new HashMap<String, ProgressRequestBody>();
        params.put(filename, test);
        return  params;
    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = ChatActivity.this.getContentResolver().query(uri,
                projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.chat_toolbar_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.phone_driver) {
//
//            Intent i = new Intent(ChatActivity.this,LocationMapActivity.class);
//            i.putExtra("latitude",String.valueOf(latitude));
//            i.putExtra("longitude",String.valueOf(longitude));
//            startActivity (i);
//
//
////                Intent callintent = new Intent(Intent.ACTION_DIAL);
////                callintent.setData(Uri.parse("tel:" + nohp));
////                startActivity(callintent);
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    @Override
    public void onResume() {
        session.setInChat(true);
        if(runSetNotification == true){
            sendChatNotification();
        }
        if(this.mPubnub4!=null) {
            String state_type = "join";
            setState(state_type, String.valueOf(System.currentTimeMillis()));
        }
        super.onResume();
    }
    @Override
    public void onPause() {
        session.setInChat(false);
        String state_type = "leave";
        if(this.mPubnub4!=null) {
            setState(state_type, String.valueOf(System.currentTimeMillis()));
        }
        super.onPause();
    }


//    @Override
//    public void onGPSLocationReceived(Location locationData) {
//        latitude = locationData.getLatitude();
//        longitude = locationData.getLongitude();
//        getCurrentAddress(String.valueOf(latitude),String.valueOf(longitude));
//    }

    private void registerDownloadReceiver(){
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_DOWNLOAD_RECEIVER);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.BROADCAST_DOWNLOAD_RECEIVER)) {
                ArrayList<ChatMessage> chat = intent.getParcelableArrayListExtra("chat");
                mChatAdapter.setProgressDownload(intent,chat);
            }
        }
    };
    /**
     * Might want to unsubscribe from PubNub here and create background service to listen while
     *   app is not in foreground.
     * PubNub will stop subscribing when screen is turned off for this demo, messages will be loaded
     *   when app is opened through a call to history.
     * The best practice would be creating a background service in onStop to handle messages.
     * The best practice would be creating a background service in onStop to handle messages.
     */
    @Override
    protected void onStop() {
        super.onStop();

//        if (this.mPubNub != null)
//
//            this.mPubNub.unsubscribe(channel_pubnub);
    }

    /**
     * Instantiate PubNub object if it is null. Subscribe to channel and pull old messages via
     *   history.
     *

     /**
     * I remove the PubNub object in onDestroy since turning the screen off triggers onStop and
     *   I wanted PubNub to receive messages while the screen is off.
     *
     */
    @Override
    protected void onDestroy() {
        if(this.mPubnub4!=null){
            this.mPubnub4.unsubscribe()
                    .channels(Arrays.asList(channel_pubnub))
                    .execute();
        }
        super.onDestroy();
    }
    @Override
    protected Dialog onCreateDialog (int id){
        switch (id){
            case progress_bar_type: //we set this to 0
                progressBarNya = new ProgressDialog(this);
                progressBarNya.setMessage("Uploading image...");
                progressBarNya.setIndeterminate(false);
                progressBarNya.setMax(100);
                progressBarNya.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBarNya.setCancelable(false);
                progressBarNya.show();
                return progressBarNya;
            default:
                return null;
        }
    }

    @Override
    public void onProgressUpdate(int percentage, String progressNumber) {
        // set current progress
        Log.d(" percentage", ""+percentage);
        progressBarNya.setProgress(percentage);
        progressBarNya.setProgressNumberFormat(progressNumber);

    }

    @Override
    public void onError() {
        Log.d(" onError", "onError");
        // do something on error
    }

    @Override
    public void onFinish() {
        Log.d(" onFinish", "onFinish");
        // do something on upload finished
        // for example start next uploading at queue
        progressBarNya.setProgress(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                dismissDialog(progress_bar_type);
                onBackpress=true;
            }
        }, 2000);

        //
    }


    @Override
    public void clickImageChat(ArrayList<ChatMessage> chat, String type,String chat_database_id, int position, String image_path, long timestamp,String request_type, String request_id, String link_image_small) {
        startDownload(chat_database_id, position, image_path, timestamp,request_type,request_id,link_image_small,chat,type);
    }
    private void startDownload(String chat_database_id, int position, String image_path, long timestamp,String request_type, String request_id, String link_image_small, ArrayList<ChatMessage> chat, String type){
//        String type = typee;
        Intent intent = new Intent(this,DownloadService.class);
        intent.putExtra("chat_database_id", chat_database_id);
        intent.putExtra("position", position);
        intent.putExtra("image_path",image_path);
        intent.putExtra("timestamp",timestamp);
        intent.putExtra("request_id",request_id);
        intent.putExtra("request_type",request_type);
        intent.putExtra("link_image_small",link_image_small);
        intent.putExtra("chat",chat);
        intent.putExtra("type",type);
        startService(intent);
    }
    @Override
    public void clickImageChatIntent(View view, int position, String activity, String location_image) {
        Intent intent = new Intent(this,ChatImageFullscreenActivity.class);
        intent.putExtra("filePath",location_image);
        startActivity(intent);
    }

    @Override
    public void re_publish(View view, ChatMessage chatMessage) {
        publish(chatMessage);
    }

    @Override
    public void re_publish_image(View view,ChatMessage chatMessage, File fileUpload, String imageName) {
        doUploadPhoto(fileUpload, imageName);
        chatImage = chatMessage;
    }

    @Override
    public void clickImageMapChat(View view, int position, String latitude, String longitude) {

    }

    public void getCheckStatusBookingChat(String requestId, String requestType)
    {
        String api = Utility.getInstance().getTokenApi(ChatActivity.this);
        BookingController.getInstance(ChatActivity.this).getCheckStatusBooking(requestId,requestType,api, checkStatusBookingInterface);
        return;
    }

    BaseGenericInterface checkStatusBookingInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> checkStatusBookingDataBaseGenericCallback) {
            int sukses = checkStatusBookingDataBaseGenericCallback.getSukses();
            if (sukses == 2) {
                CheckStatusBookingData data = (CheckStatusBookingData) checkStatusBookingDataBaseGenericCallback.getData();
                if (data != null) {
                    init();
                } else {
                    bookinglayout.setVisibility(View.GONE);
                    nobookinglayout.setVisibility(View.VISIBLE);
                    loadinglayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(ChatActivity.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(ChatActivity.this, getResources().getString(R.string.relogin));
                } else {

                }
            }
        }
    };
    public void onBackPressed(){
        if(onBackpress==true){
            super.onBackPressed();
        }
    }

}