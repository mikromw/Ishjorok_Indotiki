package com.pmberjaya.indotiki.app.account;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.callbacks.account.VerificationCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.interfaces.account.RegisterFirebaseInterface;
import com.pmberjaya.indotiki.interfaces.account.VerificationSmsInterface;
import com.pmberjaya.indotiki.models.account.UserData;
import com.pmberjaya.indotiki.models.account.UserModel;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.callbacks.account.RegisterFirebaseCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.account.VerificationData;
import com.pmberjaya.indotiki.dao.SessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willy on 11/14/2016.
 */

public class VerificationSmsActivity extends BaseActivity {
    //    private Button back;
    private EditText et1,et2,et3,et4,et5;
    private String verification_code;
    private ProgressDialog pDialog;
    private LinearLayout resendCode;
    private TextView send_phone_number;
    private String token;
    private SessionManager session;
    private UserModel userData;
    private String activity;
    private String imeiGCM;
    private String uniqueId;
    private VerificationData data;
    private UserController userController;
    private String sim_card;
    private String phonenumber;
    private String district_id;
    private ImageView layoutVerificationBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_verification_sms);
        userController = UserController.getInstance(VerificationSmsActivity.this);
        session = new SessionManager(this);
        LocationSessionManager locationSessionManager = new LocationSessionManager(this);
        district_id = locationSessionManager.getUserDistrictIdCentral();
        init();
        getIntentExtra();
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationSms();
            }
        });
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==1) {
                    et2.setFocusableInTouchMode(true);
                    et2.setFocusable(true);
                    et2.requestFocus();
                    getCode();
                }
            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==1) {
                    et3.setFocusableInTouchMode(true);
                    et3.setFocusable(true);
                    et3.requestFocus();
                    getCode();
                }
            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==1) {

                    et4.setFocusableInTouchMode(true);
                    et4.setFocusable(true);
                    et4.requestFocus();
                    getCode();
                }
            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==1) {

                    et5.setFocusableInTouchMode(true);
                    et5.setFocusable(true);
                    et5.requestFocus();
                    getCode();
                }
            }
        });

        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==1) {
                    getCode();
//                    et3.setFocusableInTouchMode(true);
//                    et3.setFocusable(true);
//                    et3.requestFocus();

                }
            }
        });

        et2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i==KeyEvent.KEYCODE_DEL && et2.getText().toString().length()==0){
                        et1.setText("");
                        et1.setFocusableInTouchMode(true);
                        et1.setFocusable(true);
                        et1.requestFocus();
                    }
                }
                return false;
            }
        });

        et3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i==KeyEvent.KEYCODE_DEL && et3.getText().toString().length()==0){
                        et2.setText("");
                        et2.setFocusableInTouchMode(true);
                        et2.setFocusable(true);
                        et2.requestFocus();
                    }
                }
                return false;
            }
        });

        et4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i==KeyEvent.KEYCODE_DEL && et4.getText().toString().length()==0){
                        et3.setText("");
                        et3.setFocusableInTouchMode(true);
                        et3.setFocusable(true);
                        et3.requestFocus();
                    }
                }
                return false;
            }
        });

        et5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i==KeyEvent.KEYCODE_DEL && et5.getText().toString().length()==0){
                        et4.setText("");
                        et4.setFocusableInTouchMode(true);
                        et4.setFocusable(true);
                        et4.requestFocus();
                    }
                }
                return false;
            }
        });


    }

    private void getIntentExtra() {
        Intent i = getIntent();
        activity = i.getAction();
        token = i.getStringExtra(Constants.TOKEN_VERIF);
        userData = i.getParcelableExtra(Constants.USER_DATA);
        send_phone_number.setText(getResources().getString(R.string.send_verification_info)+"\n"+userData.getPhone());
    }

    public void getCode(){
        String code1 = et1.getText().toString();
        String code2 = et2.getText().toString();
        String code3 = et3.getText().toString();
        String code4 = et4.getText().toString();
        String code5 = et5.getText().toString();
        if(code1.length()!=0&&code2.length()!=0&&code3.length()!=0&&code4.length()!=0&&code5.length()!=0) {
            verification_code = et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString()+ et5.getText().toString();
            if(activity!=null&&(activity.equals(Constants.ACCOUNT_VERIF_REGISTER)||activity.equals(Constants.ACCOUNT_VERIF_LOGIN))){
                doVerificationSmsLogin(verification_code);
            }else if(activity!=null&&activity.equals(Constants.ACCOUNT_VERIF_EDIT_PROFILE)){
                doVerificationSmsEdit(verification_code);
            }
        }
    }

    private void init(){
        resendCode = findViewById(R.id.resend_code);
        layoutVerificationBackground = findViewById(R.id.layoutVerificationBackground);
        send_phone_number = findViewById(R.id.send_phone_number);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
//        back = (Button)findViewById(R.id.back);
        Glide.with(this).load(R.drawable.bg_verification)
                .into(layoutVerificationBackground);
        send_phone_number = (TextView)findViewById(R.id.send_phone_number);
    }

    public BroadcastReceiver verificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                et1 = findViewById(R.id.et1);
                et2 = findViewById(R.id.et2);
                et3 = findViewById(R.id.et3);
                et4 = findViewById(R.id.et4);
                et5 = findViewById(R.id.et5);
                verification_code = bundle.getString("verification_code");
                char code1 = verification_code.charAt(0);
                char code2 = verification_code.charAt(1);
                char code3 = verification_code.charAt(2);
                char code4 = verification_code.charAt(3);
                char code5 = verification_code.charAt(4);

                et1.setText(String.valueOf(code1));
                et2.setText(String.valueOf(code2));
                et3.setText(String.valueOf(code3));
                et4.setText(String.valueOf(code4));
                et5.setText(String.valueOf(code5));
            }
            else{
                Toast.makeText(context, "Broadcast data is null", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void doVerificationSmsLogin(String code)
    {
        pDialog = ProgressDialog.show(VerificationSmsActivity.this, "", "loading...");
        String api = Utility.getInstance().getTokenApi(VerificationSmsActivity.this);
        UserController.getInstance(this).postVerificationLogin(token, code ,api, verificationLoginInterface);
        return;
    }

    private String promoMessage;
    VerificationSmsInterface verificationLoginInterface = new VerificationSmsInterface() {
        @Override
        public void onSuccessVerificationSms(VerificationCallback verificationCallback) {
            int sukses = verificationCallback.getSukses();
            String pesan = verificationCallback.getPesan();
            if(sukses==2){
                successVerificationMessage = pesan;
                VerificationData verificationData = verificationCallback.getData();
                UserData userDataServer = verificationData.getUser();
                userData = new UserModel(userDataServer.getUser_id(), userDataServer.getFullname(), userDataServer.getEmail(),
                        userDataServer.getAvatar(), userDataServer.getPhone(), userDataServer.getDeposit(), null, null,null);
                promoMessage = verificationData.getPromo();
                registerDevice();
            } else{
                Toast.makeText(VerificationSmsActivity.this, verificationCallback.getPesan(), Toast.LENGTH_SHORT).show();
                Utility.getInstance().dismissProgressDialog(pDialog);
            }
        }

        @Override
        public void onErrorVerificationSms(APIErrorCallback apiErrorCallback) {
            Utility.getInstance().dismissProgressDialog(pDialog);
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(VerificationSmsActivity.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(VerificationSmsActivity.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VerificationSmsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void doVerificationSmsEdit(String code)
    {
        pDialog = ProgressDialog.show(VerificationSmsActivity.this, "", "loading...");
        String api = Utility.getInstance().getTokenApi(VerificationSmsActivity.this);
        UserController.getInstance(this).postVerificationProfile(token, verificationParams(code),api,verificationEditProfileInterface);
        return;
    }
    public Map<String, String> verificationParams(String code) {
        Map<String, String> params = new HashMap<String, String>();
        if(userData.getFullname()!=null) {
            params.put("fullname", userData.getFullname());
        }else{
            params.put("fullname", "");
        }
        if(userData.getEmail()!=null) {
            params.put("email", userData.getEmail());
        }else{
            params.put("email", "");
        }
        if(userData.getAvatar()!=null) {
            params.put("avatar", userData.getAvatar());
        }else{
            params.put("avatar", "");
        }
        if(userData.getPhone()!=null) {
            params.put("phone",userData.getPhone());
        }else{
            params.put("phone","");
        }

        params.put("birthdate","");

        if( userData.getHide_number()!=null) {
            params.put("hide_phone", userData.getHide_number());
        }else{
            params.put("hide_phone","");
        }

        params.put("verification_code", code);
        return  params;
    }

    private String successVerificationMessage;
    VerificationSmsInterface verificationEditProfileInterface = new VerificationSmsInterface() {
        @Override
        public void onSuccessVerificationSms(VerificationCallback verificationCallback) {
            int sukses = verificationCallback.getSukses();
            String pesan = verificationCallback.getPesan();
            Utility.getInstance().dismissProgressDialog(pDialog);
            if(sukses==2){
                successVerificationMessage = pesan;
                session.createLoginSession(userData.getUser_id(), userData.getFullname(), userData.getPhone(), userData.getAvatar(), userData.getEmail(),userData.getDeposit(),null);
                Utility.getInstance().showSimpleAlertDialog(VerificationSmsActivity.this, getResources().getString(R.string.verification_code_true),pesan,
                        getResources().getString(R.string.ok) , verificationPositiveButton, null,null, false );
            }
            else{
                showToast(pesan);
            }
        }

        @Override
        public void onErrorVerificationSms(APIErrorCallback apiErrorCallback) {
            Utility.getInstance().dismissProgressDialog(pDialog);
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(VerificationSmsActivity.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(VerificationSmsActivity.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VerificationSmsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private DialogInterface.OnClickListener verificationPositiveButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    };
    private void registerDevice() {
        pDialog = ProgressDialog.show(VerificationSmsActivity.this, "", "loading...");
        //Creating a firebase object
        Firebase firebase = new Firebase(Config.FIREBASE_APP);
        firebase.authWithCustomToken(Config.FINAL_TOKEN, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticationError(FirebaseError error) {
                System.err.println("LoginActivity Failed! " + error.getMessage());
            }
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("LoginActivity Succeeded!");
            }
        });
        Firebase newFirebase = firebase.push();

        uniqueId = newFirebase.getKey();

        registerFcmId(uniqueId);
    }
    private void registerFcmId(String registrationId)
    {
        String api = Utility.getInstance().getTokenApi(VerificationSmsActivity.this);
        userController.postRegisterFcm(userController.registerFcmParams(Utility.getInstance().getDeviceData(this), registrationId, district_id),api, registerFirebaseInterface);
        return;
    }
    RegisterFirebaseInterface registerFirebaseInterface = new RegisterFirebaseInterface() {
        @Override
        public void onSuccessRegisterFirebase(RegisterFirebaseCallback registerGcmCallback) {
            int sukses = registerGcmCallback.getSukses();
            if(sukses==2){
                Utility.getInstance().dismissProgressDialog(pDialog);
                SessionManager session = new SessionManager(VerificationSmsActivity.this);
                session.createLoginSession( userData.getUser_id(), userData.getFullname(),  userData.getPhone(), userData.getAvatar(), userData.getEmail(),null, uniqueId);
                if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(promoMessage)) {
                    Utility.getInstance().showSimpleAlertDialog(VerificationSmsActivity.this, getResources().getString(R.string.verification_code_true), promoMessage,
                            getResources().getString(R.string.ok), verificationPositiveButton, null, null, false);
                }else{
                    Utility.getInstance().showSimpleAlertDialog(VerificationSmsActivity.this, getResources().getString(R.string.verification_code_true), successVerificationMessage,
                            getResources().getString(R.string.ok), verificationPositiveButton, null, null, false);
                }
            }
            else{
            }
        }

        @Override
        public void onErrorRegisterFirebase(APIErrorCallback apiErrorCallback) {
            Utility.getInstance().dismissProgressDialog(pDialog);
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(VerificationSmsActivity.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(VerificationSmsActivity.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VerificationSmsActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
            UserController.getInstance(VerificationSmsActivity.this).setRegisterGcmErrorCallback(null);
        }
    };

    private void resendVerificationSms()
    {
        String api = Utility.getInstance().getTokenApi(VerificationSmsActivity.this);
        pDialog = ProgressDialog.show(VerificationSmsActivity.this, "", "loading...");
        UserController.getInstance(this).postSendBackVerificationProfile(token,parameters(userData.getPhone()), pDialog,api, baseInterface);
        return;
    }
    public Map<String, String> parameters(String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        return  params;
    }
    BaseInterface baseInterface = new BaseInterface() {
        @Override
        public void onSuccess(BaseCallback baseCallback) {
            pDialog.dismiss();
            String pesan = baseCallback.getPesan();
            Toast.makeText(VerificationSmsActivity.this, pesan, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(VerificationSmsActivity.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(VerificationSmsActivity.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VerificationSmsActivity.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(verificationReceiver, new IntentFilter(Constants.BROADCAST_VERIFICATION_SMS));
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(verificationReceiver);
    }

    @Override
    public void onBackPressed(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationSmsActivity.this);
        builder.setTitle(null);
        builder.setMessage(R.string.verification_confirm);
        builder.setIcon( R.mipmap.ic_launcher);
        // Set the action buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(VerificationSmsActivity.this, MainActivityTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}
