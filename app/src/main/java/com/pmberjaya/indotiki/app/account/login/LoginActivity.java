package com.pmberjaya.indotiki.app.account.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.account.VerificationSmsActivity;
import com.pmberjaya.indotiki.app.account.profile.CompletingProfileActivity;
import com.pmberjaya.indotiki.callbacks.account.LoginCallback;
import com.pmberjaya.indotiki.callbacks.account.RegisterFirebaseCallback;
import com.pmberjaya.indotiki.callbacks.account.YahooProfileCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.interfaces.account.LoginInterface;
import com.pmberjaya.indotiki.interfaces.account.RegisterFirebaseInterface;
import com.pmberjaya.indotiki.interfaces.account.YahooProfileInterface;
import com.pmberjaya.indotiki.models.account.DeviceDataModel;
import com.pmberjaya.indotiki.models.account.LoginData;
import com.pmberjaya.indotiki.models.account.UserModel;
import com.pmberjaya.indotiki.models.account.VerificationData;
import com.pmberjaya.indotiki.models.account.YahooProfile.Email;
import com.pmberjaya.indotiki.models.account.YahooProfile.Image;
import com.pmberjaya.indotiki.models.account.YahooProfile.Phone;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.models.account.YahooProfile.Profile;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.SlideAnimation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edwin on 13/01/2017.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private int logoHeight;
    private SessionManager session;

    private GoogleApiClient mGoogleApiClient;
    private UserModel userData;

    private AccessTokenTracker profileTracker;
    private String firebaseId;
    private ProgressDialog pDialog;
    private String gplusId;
    private String facebookId;
    private String yahooId;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    private static final int RC_SIGN_IN = 0;
    private static final int YAHOO_SIGN_IN = 1;
    private int STATE_LOG_IN = 106;

    Animation loginPhoneAnimFadeIn;
    Animation loginPhoneAnimFadeOut;
    Animation loginEmailAnimFadeIn;
    Animation loginEmailAnimFadeOut;
    private RadioGroup rgLoginVia;
    private UserController userController;
    private TextView tvPhoneNumberHead;
    private LinearLayout btnNext;
    private EditText etPhoneNumber;
    private TextView tvPhoneNumberValid;
    private LinearLayout layoutLoginPhone;
    private LinearLayout layoutLoginEmail;
    private TableRow btnLoginGoogle;
    private TableRow btnLoginYahoo;
    private LoginButton btnLoginFacebookDev;
    private TableRow btnLoginFacebook;
    private LinearLayout layoutIndotikiLogoAnimation;
    private LinearLayout layoutIndotikiLogo;
    private LinearLayout layoutLoginVia;
    private float slideHeight;
    private String district_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userController = UserController.getInstance(LoginActivity.this);
        session = new SessionManager(LoginActivity.this);
        LocationSessionManager locationSessionManager = new LocationSessionManager(this);
        district_id = locationSessionManager.getUserDistrictIdCentral();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.account_login_activity);
        layoutIndotikiLogoAnimation = findViewById(R.id.layoutIndotikiLogoAnimation);
        layoutIndotikiLogo = findViewById(R.id.layoutIndotikiLogo);
        layoutLoginVia = findViewById(R.id.layoutLoginVia);
        rgLoginVia = findViewById(R.id.rgLoginVia);
        layoutLoginVia.setVisibility(View.INVISIBLE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        initAnimationSetting();
        initLoginWithPhoneLayout();
        initLoginWithEmailLayout();

        initRadioButton();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layoutIndotikiLogo.getLayoutParams();
        slideHeight = layoutIndotikiLogoAnimation.getY();
        if(layoutIndotikiLogo.getVisibility()==View.INVISIBLE){
            final TranslateAnimation slideAnim = new TranslateAnimation(0,0,0, -slideHeight);
            slideAnim.setDuration(1000L);
            slideAnim.setInterpolator(new LinearInterpolator());
            slideAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) { }

                @Override
                public void onAnimationRepeat(Animation arg0) { }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    Animation animFade  = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_animation_fade);
                    layoutIndotikiLogoAnimation.setVisibility(View.GONE);
                    layoutIndotikiLogo.setVisibility(View.VISIBLE);
                    layoutLoginPhone.startAnimation(loginPhoneAnimFadeIn);
                    layoutLoginVia.setVisibility(View.VISIBLE);
                    layoutLoginVia.startAnimation(animFade);
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutIndotikiLogoAnimation.startAnimation(slideAnim);
                }
            },200);

        }
    }

    public void initAnimationSetting(){
        loginPhoneAnimFadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_animation_fade);
        loginPhoneAnimFadeIn.setAnimationListener(loginWithPhoneAnimationVisible);
        loginPhoneAnimFadeOut= AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_animation_fade_out);
        loginPhoneAnimFadeOut.setAnimationListener(loginWithPhoneAnimationGone);

        loginEmailAnimFadeIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_animation_fade);
        loginEmailAnimFadeIn.setAnimationListener(loginWithEmailAnimationVisible);
        loginEmailAnimFadeOut = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_animation_fade_out);
        loginEmailAnimFadeOut.setAnimationListener(loginWithEmailAnimationGone);
    }

    public void initLoginWithPhoneLayout(){
        btnNext = (LinearLayout)findViewById(R.id.btnNext);
        layoutLoginPhone = (LinearLayout)findViewById(R.id.layoutLoginPhone);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        tvPhoneNumberValid = (TextView) findViewById(R.id.tvPhoneNumberValid);
        tvPhoneNumberHead = (TextView) findViewById(R.id.tvPhoneNumberHead);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPhoneValidation(etPhoneNumber.getText().toString())==true){
                    doLoginPhone();
                }
            }
        });
    }

    private void initLoginWithEmailLayout() {
        layoutLoginEmail = (LinearLayout)findViewById(R.id.layoutLoginEmail);
        btnLoginGoogle = (TableRow) findViewById(R.id.btnLoginGoogle);
        btnLoginYahoo= (TableRow) findViewById(R.id.btnLoginYahoo);
        btnLoginFacebookDev = (LoginButton)findViewById(R.id.btnLoginFacebookDev);
        btnLoginFacebook = (TableRow) findViewById(R.id.btnLoginFacebook);

        btnLoginGoogle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onLoginGoogle();
            }
        });

        initFacebook();
        btnLoginYahoo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,YahooWebViewActivity.class);
                startActivityForResult(i, YAHOO_SIGN_IN);
            }
        });
        initGoogle();

    }

    private boolean checkPhoneValidation(String phone_number){
        if(phone_number.length()==0){
            tvPhoneNumberValid.setText(getResources().getString(R.string.phone_valid));
            tvPhoneNumberValid.setVisibility(View.VISIBLE);
            return false;
        }else if(phone_number.length()<10){
            tvPhoneNumberValid.setText(getResources().getString(R.string.phone_valid2));
            tvPhoneNumberValid.setVisibility(View.VISIBLE);
            return false;
        }else{
            tvPhoneNumberValid.setVisibility(View.GONE);
            return true;
        }
    }

    private void doLoginPhone()
    {
        btnNext.setEnabled(false);
        String loginPhone = tvPhoneNumberHead.getText().toString()+etPhoneNumber.getText().toString();
        loginPhone = Utility.getInstance().phoneFormat(loginPhone);
        String api = Utility.getInstance().getTokenApi(LoginActivity.this);
        pDialog = ProgressDialog.show(LoginActivity.this, "", "loading...");
        UserController.getInstance(this).doLoginPhone(loginPhone,api, loginPhoneInterface);
        return;
    }

    private LoginInterface loginPhoneInterface = new LoginInterface() {
        @Override
        public void onSuccessLogin(LoginCallback loginCallback) {
            btnNext.setEnabled(true);
            Utility.getInstance().dismissProgressDialog(pDialog);
            int sukses = loginCallback.getSukses();
            String pesan = loginCallback.getPesan();
            LoginData data = loginCallback.getLoginData();
            if(sukses==2){
                userData = new UserModel(data.getUserId(), data.getFullname(), data.getEmail(), null, data.getPhone(), data.getDeposit(), null, null,null);
                Intent intent = new Intent(LoginActivity.this,VerificationSmsActivity.class);
                intent.setAction(Constants.ACCOUNT_VERIF_LOGIN);
                intent.putExtra(Constants.USER_DATA, userData);
                intent.putExtra(Constants.TOKEN_VERIF,loginCallback.getToken());
                startActivityForResult(intent, STATE_LOG_IN);
            }else if(sukses==1){
                userData = new UserModel(null, null, null, null, tvPhoneNumberHead.getText().toString()+etPhoneNumber.getText().toString(), null, null, null, null);
                Intent intent = new Intent(LoginActivity.this,CompletingProfileActivity.class);
                intent.setAction(Constants.ACCOUNT_VERIF_LOGIN);
                intent.putExtra(Constants.USER_DATA,userData);
                startActivityForResult(intent, STATE_LOG_IN);
            }
            else{
                Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onErrorLogin(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                btnNext.setEnabled(true);
                Utility.getInstance().dismissProgressDialog(pDialog);
                if (apiErrorCallback.getError() != null && apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(LoginActivity.this, getResources().getString(R.string.relogin));
                } else {
                    if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
    private LoginInterface loginEmailInterface = new LoginInterface() {
        @Override
        public void onSuccessLogin(LoginCallback loginCallback) {
            int sukses = loginCallback.getSukses();
            String pesan = loginCallback.getPesan();
            if(sukses==2){
                if(loginCallback.isStatus()) {
                    LoginData data = loginCallback.getLoginData();
                    if (data.getStatus().equals("1")) {
                        userData = new UserModel(data.getUserId(), data.getFullname(), data.getEmail(), null, data.getPhone(), data.getDeposit(), null, null,null);
                        registerDevice();
                        Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_LONG).show();
                    } else {
                        Utility.getInstance().dismissProgressDialog(pDialog);
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.unactive), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Utility.getInstance().dismissProgressDialog(pDialog);
                    Intent intent = new Intent(LoginActivity.this,CompletingProfileActivity.class);
                    intent.setAction(Constants.ACCOUNT_VERIF_LOGIN);
                    intent.putExtra(Constants.USER_DATA, userData);
                    startActivityForResult(intent, STATE_LOG_IN);
                }
            }
            else{
                showErrorFormAlertDialog(loginCallback);
            }
        }

        @Override
        public void onErrorLogin(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(LoginActivity.this, getResources().getString(R.string.relogin));
                } else {
                    pDialog.dismiss();
                    if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    private void showErrorFormAlertDialog(LoginCallback loginCallback){
        ArrayList<String> errorArray = loginCallback.getError_form();
        StringBuilder sb= new StringBuilder();
        for(int i = 0; i<errorArray.size(); i++){
            String errorMsg = errorArray.get(i);
            sb.append("-"+errorMsg+"\n");
        }
        Utility.getInstance().showSimpleAlertDialog(LoginActivity.this, getResources().getString(R.string.login_failed), sb.toString(),
                getResources().getString(R.string.ok), loginFailedPositiveButton, null, null, false);
        Utility.getInstance().dismissProgressDialog(pDialog);
    }
    private DialogInterface.OnClickListener loginFailedPositiveButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    private void registerDevice() {
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
        firebaseId = newFirebase.getKey();
        registerFcmId(firebaseId);
    }

    private void registerFcmId(String firebaseId)
    {
        DeviceDataModel deviceDataModel = Utility.getInstance().getDeviceData(this);
        String api = Utility.getInstance().getTokenApi(LoginActivity.this);
        userController.postRegisterFcm(userController.registerFcmParams(deviceDataModel, firebaseId, district_id),api,registerFirebaseInterface);
        return;
    }

    RegisterFirebaseInterface registerFirebaseInterface = new RegisterFirebaseInterface() {
        @Override
        public void onSuccessRegisterFirebase(RegisterFirebaseCallback registerGcmCallback) {
            int sukses = registerGcmCallback.getSukses();
            if(sukses==2){
                Utility.getInstance().dismissProgressDialog(pDialog);
                session.createLoginSession(userData.getUser_id(), userData.getFullname(), userData.getPhone(), userData.getAvatar(), userData.getEmail(), userData.getDeposit(), firebaseId);
                Intent returnIntent = new Intent();
                LoginActivity.this.setResult(LoginActivity.this.RESULT_OK, returnIntent);
                finish();
            }
        }
        @Override
        public void onErrorRegisterFirebase(APIErrorCallback apiErrorCallback) {
            Utility.getInstance().dismissProgressDialog(pDialog);
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(LoginActivity.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
            UserController.getInstance(LoginActivity.this).setRegisterGcmErrorCallback(null);
        }
    };

    //------------------------------------------------------GOOGLELOGIN---------------------------------------------------------------------------------------------------
    public void initGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    public void onLoginGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOutGoogle(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            userData = new UserModel(null, acct.getGivenName()+" "+acct.getFamilyName(), acct.getEmail(), String.valueOf(acct.getPhotoUrl()), null, null,
                    "gplus", acct.getId(), null);
            updateUI(true);
        } else {
            updateUI(false);
        }
    }
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            doLoginGoogle();
        } else {
            Toast.makeText(LoginActivity.this, "LoginActivity Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void doLoginGoogle()
    {
        String api = Utility.getInstance().getTokenApi(LoginActivity.this);
        pDialog = ProgressDialog.show(LoginActivity.this, "", "loading...");
        userController.postLoginGoogle(userData,api, loginEmailInterface);
        if(mGoogleApiClient!=null&&mGoogleApiClient.isConnected()) {
            signOutGoogle();
        }
    }
    //------------------------------------------------------GOOGLELOGIN---------------------------------------------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if(requestCode == YAHOO_SIGN_IN){
            if(resultCode == LoginActivity.this.RESULT_OK) {
                String yahooAccessToken = data.getStringExtra("access_token");
                getYahooProfile(yahooAccessToken);
            }
        }else if(requestCode == STATE_LOG_IN){
            if(resultCode == LoginActivity.this.RESULT_OK) {
                Intent returnIntent = new Intent();
                LoginActivity.this.setResult(LoginActivity.this.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    //------------------------------------------------------FACEBOOKLOGIN---------------------------------------------------------------------------------------------------
    private void initFacebook(){
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
        btnLoginFacebookDev.setReadPermissions(Arrays.asList("email"));
        btnLoginFacebookDev.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject userDataObject, GraphResponse response) {
                        try {
                            if(userDataObject==null||userDataObject.getString("email") == null){
                                showInvalidFacebookEmailADialog();
                            }else {
                                facebookId = userDataObject.getString("id");
                                userData = new UserModel(null, userDataObject.getString("name"), userDataObject.getString("email"),
                                        userDataObject.getString("link"),null,null,"facebook",facebookId, null);
                                doLoginFacebook();
                            }
                            LoginManager.getInstance().logOut();
                        } catch (Exception e) {
                            e.printStackTrace();
                            LoginManager.getInstance().logOut();
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    showInvalidFacebookEmailADialog();
                                }
                            });
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email,name,link, first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "Error:"+exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
            }
        });

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                btnLoginFacebookDev.performClick();
            }
        });
    }
    private void showInvalidFacebookEmailADialog() {
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(null);
        builder.setMessage(getResources().getString(R.string.email_fb_invalid));
        builder.setIcon(null);
        // Set the action buttons
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    private void doLoginFacebook()
    {
        String api = Utility.getInstance().getTokenApi(LoginActivity.this);
        pDialog = ProgressDialog.show(LoginActivity.this, "", "loading...");
        userController.postLoginFacebook(userData, api, loginEmailInterface);
    }
    //------------------------------------------------------FACEBOOKLOGIN--------------------------------------------------------------------------------------------------

    //------------------------------------------------------YAHOOLOGIN---------------------------------------------------------------------------------------------------
    private void getYahooProfile(String access_token)
    {
            pDialog = ProgressDialog.show(LoginActivity.this, "", "loading...");
            userController.getYahooProfile(access_token,yahooProfileInterface);
            return;
    }
    private YahooProfileInterface yahooProfileInterface = new YahooProfileInterface() {
        @Override
        public void onSuccessYahooProfile(YahooProfileCallback yahooProfileCallback) {
            Profile profile = yahooProfileCallback.getProfile();
            Image imageName = profile.getImageName();
            List<Email> emailsArray = profile.getEmails();
            List<Phone> phonesArray = profile.getPhones();
            String fullnameYahoo;
            String emailYahoo = null;
            String phoneYahoo = null;
            String profilePhotoYahoo;
            if(emailsArray!=null&&emailsArray.size()!=0){
                Email emails = emailsArray.get(0);
                emailYahoo = emails.getHandle();
            }
            if(phonesArray!=null&&phonesArray.size()!=0){
                Phone phones = phonesArray.get(0);
                phoneYahoo = phones.getNumber();
                String[] phoneArrays = phoneYahoo.split("-");
                String countryCode = phoneArrays[0];
                String numberPhone = phoneArrays[1];
                phoneYahoo = "+"+countryCode+numberPhone;
            }
            yahooId = profile.getGuid();
            profilePhotoYahoo = imageName.getImageUrl();
            String givenName = profile.getGivenName();
            String familyName = profile.getFamilyName();

            if(familyName!=null){
                fullnameYahoo =givenName+" "+familyName;
            }
            else{
                fullnameYahoo =givenName;
            }
            userData = new UserModel(null, fullnameYahoo, emailYahoo, profilePhotoYahoo, phoneYahoo, null, "yahoo",yahooId, null);
            doLoginYahoo();
        }

        @Override
        public void onErrorYahooProfile(APIErrorCallback apiErrorCallback) {

        }
    };

    private void doLoginYahoo()
    {
        String api = Utility.getInstance().getTokenApi(LoginActivity.this);
        userController.postLoginYahoo(userData, api, loginEmailInterface);
        return;
    }

    @Override
    protected void onDestroy()
    {
        if(profileTracker!=null){
            profileTracker.stopTracking();
        }
        if(accessTokenTracker!=null){
            accessTokenTracker.stopTracking();
        }
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                signOutGoogle();
                mGoogleApiClient.stopAutoManage(LoginActivity.this);
                mGoogleApiClient.disconnect();
            }
            mGoogleApiClient = null;
        }
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Firebase.setAndroidContext(getApplicationContext());
    }
    public void initRadioButton(){
        rgLoginVia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.fabLoginViaPhone) {
                    if(layoutLoginEmail.getVisibility()==View.VISIBLE) {
                        layoutLoginEmail.startAnimation(loginEmailAnimFadeOut);
                    }
                    layoutLoginPhone.startAnimation(loginPhoneAnimFadeIn);
                } else if(checkedId == R.id.fabLoginViaEmail) {
                    if(layoutLoginPhone.getVisibility()==View.VISIBLE) {
                        layoutLoginPhone.startAnimation(loginPhoneAnimFadeOut);
                    }
                    layoutLoginEmail.startAnimation(loginEmailAnimFadeIn);
                }
            }
        });
    }

    public Animation.AnimationListener loginWithPhoneAnimationVisible = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation arg0) { }

        @Override
        public void onAnimationRepeat(Animation arg0) { }

        @Override
        public void onAnimationEnd(Animation arg0) {
            layoutLoginPhone.setVisibility(View.VISIBLE);
        }
    };
    public Animation.AnimationListener loginWithPhoneAnimationGone= new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) { }

        @Override
        public void onAnimationRepeat(Animation arg0) { }

        @Override
        public void onAnimationEnd(Animation arg0) {
            layoutLoginPhone.setVisibility(View.GONE);
        }
    };

    public Animation.AnimationListener loginWithEmailAnimationVisible = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) { }

        @Override
        public void onAnimationRepeat(Animation arg0) { }

        @Override
        public void onAnimationEnd(Animation arg0) {
            layoutLoginEmail.setVisibility(View.VISIBLE);
        }
    };
    public Animation.AnimationListener loginWithEmailAnimationGone= new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) { }

        @Override
        public void onAnimationRepeat(Animation arg0) { }

        @Override
        public void onAnimationEnd(Animation arg0) {
            layoutLoginEmail.setVisibility(View.GONE);
        }
    };
}
