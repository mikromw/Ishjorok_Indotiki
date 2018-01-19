package com.pmberjaya.indotiki.app.account.profile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.account.VerificationSmsActivity;
import com.pmberjaya.indotiki.callbacks.account.CompletingDataCallback;
import com.pmberjaya.indotiki.callbacks.account.UploadPhotoTempCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.interfaces.account.CompletingProfileInterface;
import com.pmberjaya.indotiki.interfaces.account.UploadImageTempInterface;
import com.pmberjaya.indotiki.models.account.CompletingData;
import com.pmberjaya.indotiki.models.account.UserModel;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.models.account.EmailData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by willy on 5/23/2017.
 */

public class CompletingProfileActivity extends BaseActivity implements ProgressRequestBody.UploadCallbacks{
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private static int RESULT_LOAD_IMAGE_REQUEST_CODE = 1;

    private UserController userController;
    private CompletingData data;
    AlertDialog dialog;
    private ProgressDialog progressDialog;
    private Uri selectedImageUri;
    int checkitemoption=-1;
    private android.app.AlertDialog adialog;
    private String imagepath=null;
    private String imageName;
    private String cover = "";
    private String temp_username;
    ProgressDialog progressBarNya;
    public static final int progress_bar_type = 0;
    List<EmailData> emailDatas;
    private EmailData emailData;

    String namePattern = "[a-zA-Z ]+";
    private EmailListAdapter completingAdapter;
    private UserModel userData;
    private EditText etFullname;
    private EditText etEmail;
    private EditText etPhone;
    private TextView tvFullnameValid;
    private TextView tvEmailValid;
    private TextView tvPhoneValid;
    private LinearLayout btnSubmit;
    private ImageView ivProfilePhoto;
    private CheckBox cbTerms;
    private TextView tvTermsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_completing_profile_activity);
        userController = UserController.getInstance(this);
        renderView();
        getIntentExtra();

        final CharSequence[] itemoption = {getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CompletingProfileActivity.this);
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
        });

        final String blockCharacterSet = "[@#~-/<>,.?~:;#^|$%&*!'`()_+=\"1-9]";
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
                String phoneNumber = "[+][0-9]*$|[0-9]*$";

                if(etFullname.getText().length()==0){
                    tvFullnameValid.setVisibility(View.VISIBLE);
                    tvFullnameValid.setText(getResources().getString(R.string.fullname_valid));
                }else{
                    tvFullnameValid.setVisibility(View.GONE);
                }

                if(etEmail == null){
                    tvEmailValid.setVisibility(View.VISIBLE);
                    tvEmailValid.setText(getResources().getString(R.string.email_valid));
                }else{
                    tvEmailValid.setVisibility(View.GONE);
                }

                if(!etEmail.getText().toString().matches(emailPattern)){
                    tvEmailValid.setVisibility(View.VISIBLE);
                    tvEmailValid.setText(getResources().getString(R.string.email_valid));
                }else{
                    tvEmailValid.setVisibility(View.GONE);
                }

                if(etPhone.length()==0){
                    tvPhoneValid.setVisibility(View.VISIBLE);
                    tvPhoneValid.setText(getResources().getString(R.string.phone_valid));
                }else{
                    tvPhoneValid.setVisibility(View.GONE);
                }

                if (etFullname.getText().toString().matches(namePattern)) {
                    tvFullnameValid.setVisibility(View.GONE);
                } else {
                    tvFullnameValid.setVisibility(View.VISIBLE);
                    tvFullnameValid.setText(getResources().getString(R.string.fullname_valid2));
                }
                if (cbTerms.isChecked()) {
                    tvTermsValid.setVisibility(View.GONE);
                } else {
                    tvTermsValid.setVisibility(View.VISIBLE);
                    tvTermsValid.setText(getResources().getString(R.string.please_agree_tc));
                }
                if(etFullname.getText().length()!=0 &&  etFullname.getText().toString().matches(namePattern) && etPhone.getText().length()!=0 && etPhone.getText().length()>10&&cbTerms.isChecked()){
                    postCompletingData();
                    progressDialog = new ProgressDialog(CompletingProfileActivity.this);
                    progressDialog.setMessage(getResources().getString(R.string.loading));
                    progressDialog.show();
                }
            }
        });

    }

    private void renderView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolbar(toolbar, getResources().getString(R.string.completing_Data), null);
        etFullname = (EditText)findViewById(R.id.etFullname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText)findViewById(R.id.etPhone);
        tvFullnameValid = (TextView) findViewById(R.id.tvFullnameValid);
        tvEmailValid = (TextView) findViewById(R.id.tvEmailValid);
        tvPhoneValid = (TextView) findViewById(R.id.tvPhoneValid);
        btnSubmit = (LinearLayout) findViewById(R.id.btnSubmit);
        ivProfilePhoto = (ImageView) findViewById(R.id.ivProfilePhoto);
        cbTerms = (CheckBox) findViewById(R.id.cbTerms);
        tvTermsValid = (TextView) findViewById(R.id.tvTermsValid);
    }
    private void getListEmail() {
        try {
            String email = "";
            String[] splitName;
            emailDatas = new ArrayList<>();
            final Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
            //Log.e("accounts","->"+accounts.length);
            for (Account account : accounts) {
                if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                    email = account.name;
                    splitName = email.split("@");
                    temp_username = splitName[0];

                    emailData = new EmailData(email, temp_username);
                    emailDatas.add(emailData);
                    Log.d("email", " > " + email);
                    Log.d("name", " > " +splitName);
                }
            }

            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CompletingProfileActivity.this);
            builder.setTitle(getResources().getString(R.string.select_email));
            completingAdapter = new EmailListAdapter(this,0,emailDatas);
            builder.setAdapter(completingAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = completingAdapter.getItem(which).getEmail();
                    etEmail.setText(email);
                    String username = completingAdapter.getItem(which).getName();
//                    String result = username.replaceAll("[ |?*<\":>+\\[\\]/']", " ");
                    etFullname.setText(username+"");
                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception e) {
            Log.i("Exception", "Exception:" + e);
        }
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        userData = intent.getParcelableExtra(Constants.USER_DATA);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getFullname())) {
            etFullname.setText(userData.getFullname());
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getAvatar())) {
            PicassoLoader.loadProfile(this, userData.getAvatar(), ivProfilePhoto, R.mipmap.img_no_avatar);
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getPhone())) {
            etPhone.setText(Utility.getInstance().phoneFormat(userData.getPhone()));
            getListEmail();
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getEmail())) {
            etEmail.setEnabled(false);
            etEmail.setText(userData.getEmail());
        }
    }

    private void postCompletingData()
    {
        String api = Utility.getInstance().getTokenApi(CompletingProfileActivity.this);
        userData.fullname = etFullname.getText().toString();
        userData.email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        userData.phone = Utility.getInstance().phoneFormat(phone);
        userController.postCompletingData(params(userData),api, completingProfileInterface);
        return;
    }

    private HashMap<String,String> params(UserModel userData){
        String imei = Utility.getInstance().getDeviceData(this).imei;
        HashMap<String,String> map = new HashMap<>();
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getFullname())){
            map.put("fullname",userData.getFullname());
        }else{
            map.put("fullname", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getEmail())){
            map.put("email",userData.getEmail());
        }else{
            map.put("email", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getPhone())){
            map.put("phone",userData.getPhone());
        }else{
            map.put("phone", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getLoginType())){
            map.put("login_type",userData.getLoginType());
        }else{
            map.put("login_type", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getSocialId())){
            map.put("social_id",userData.getSocialId());
        }else{
            map.put("social_id", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(userData.getAvatar())){
            map.put("cover",userData.getAvatar());
        }else{
            map.put("cover", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(imei)){
            map.put("imei",imei);
        }else{
            map.put("imei", "");
        }
        return map;
    }

    CompletingProfileInterface completingProfileInterface = new CompletingProfileInterface() {
        @Override
        public void onSuccessCompletingData(CompletingDataCallback completingDataCallback) {
            if(completingDataCallback.getSukses()==2){
                data = completingDataCallback.getData();
                UserModel userModel = new UserModel();
                userModel.setPhone(data.getPhone());
                userModel.setAvatar(data.getAvatar());
                userModel.setEmail(data.getEmail());
                userModel.setFullname(data.getFullname());
                userModel.setUser_id(data.getUser_id());

                Intent intent = new Intent(CompletingProfileActivity.this,VerificationSmsActivity.class);
                intent.setAction(Constants.ACCOUNT_VERIF_REGISTER);
                intent.putExtra(Constants.USER_DATA, userModel);
                intent.putExtra(Constants.TOKEN_VERIF, data.getReset_password());
                startActivityForResult(intent, Constants.STATE_LOGIN_CODE);
            }else{
                ArrayList<String> errorArray = completingDataCallback.getError_form();
                StringBuilder sb= new StringBuilder();
                for(int i = 0; i<errorArray.size(); i++){
                    String errorMsg = errorArray.get(i);
                    sb.append("-"+errorMsg+"\n");
                }
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(CompletingProfileActivity.this);
                builder.setTitle(getResources().getString(R.string.login_failed));
                builder.setMessage(sb.toString());
                builder.setIcon( R.mipmap.ic_launcher);
                // Set the action buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                progressDialog.dismiss();
            }
        }

        @Override
        public void onErrorCompletingData(APIErrorCallback apiErrorCallback) {
            initpostCompletingDataError(apiErrorCallback);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE_REQUEST_CODE) {
            //Bitmap photo = (Bitmap) data.getData().getPath();
            if (resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImageUri = data.getData();
                imagepath = getPath(selectedImageUri);
                imageName = imagepath.substring(imagepath.lastIndexOf("/"));
                File file = new File(imagepath);
                long lengthbmp = file.length();
                Log.d("LENGTHNYA", "" + lengthbmp);
                if(lengthbmp<5000000) {
                    doUploadPhoto(file);
                }
                else{
                    Toast.makeText(CompletingProfileActivity.this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ) {
            if (resultCode == Activity.RESULT_OK) {
                //Bitmap photo = (Bitmap) data.getData().getPath();
                imagepath = getPath(selectedImageUri);
                imageName = imagepath.substring(imagepath.lastIndexOf("/"));
                File file = new File(imagepath);
                long lengthbmp = file.length();
                Log.d("LENGTHNYA", "" + lengthbmp);
                if(lengthbmp<5000000) {
                    doUploadPhoto(file);
                }
                else{
                    Toast.makeText(CompletingProfileActivity.this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(CompletingProfileActivity.this, "ambil foto gagal", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == Constants.STATE_LOGIN_CODE){
            if(resultCode == CompletingProfileActivity.this.RESULT_OK) {
                Intent returnIntent = new Intent();
                CompletingProfileActivity.this.setResult(CompletingProfileActivity.this.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = CompletingProfileActivity.this.getContentResolver().query(uri,
                projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void doUploadPhoto(File file){
        showDialog(progress_bar_type);
        String MEDIA_TYPE_PNG = "image/" + imageName.substring(imageName.lastIndexOf(".") + 1);
        ProgressRequestBody test = new ProgressRequestBody(file, MEDIA_TYPE_PNG, this);
        String filename ="file\"; filename=\""+imageName.substring(1,imageName.length())+"\" ";
        String api = Utility.getInstance().getTokenApi(CompletingProfileActivity.this);
        Log.d("api",""+api);
        userController.doUploadPhotoTemp(parametersSendRequest(filename,test),api, uploadImageTempInterface);
        return;
    }
    public Map<String, ProgressRequestBody> parametersSendRequest(String filename, ProgressRequestBody test) {
        Map<String, ProgressRequestBody> params = new HashMap<String, ProgressRequestBody>();
        params.put(filename, test);

        return  params;
    }

    UploadImageTempInterface uploadImageTempInterface = new UploadImageTempInterface() {
        @Override
        public void onSuccessUploadImageTemp(UploadPhotoTempCallback response) {
            int sukses = response.getSukses();
            Log.d("test",""+sukses);
            if(sukses==2){
                progressBarNya.dismiss();
                userData.setAvatar(response.getAvatar());
                PicassoLoader.loadProfile(CompletingProfileActivity.this,response.getAvatar(),ivProfilePhoto,R.mipmap.img_no_avatar);
            }
            onFinish();
        }

        @Override
        public void onErrorUploadImageTemp(APIErrorCallback apiErrorCallback) {
            initUploadFotoTemp(apiErrorCallback);
        }
    };

    public void initpostCompletingDataError(APIErrorCallback apiErrorCallback){
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
        if(apiErrorCallback.getError()!=null) {
            if (apiErrorCallback.getError().equals("Invalid API key ")) {
                Utility.getInstance().showInvalidApiKeyAlert(this, getResources().getString(R.string.relogin));
            }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                Toast.makeText(this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }
        UserController.getInstance(this).setRegisterGcmErrorCallback(null);
    }

    public void initUploadFotoTemp(APIErrorCallback apiErrorCallback){
        if(progressBarNya!=null) {
            progressBarNya.dismiss();
        }
        if(apiErrorCallback.getError()!=null) {
            if (apiErrorCallback.getError().equals("Invalid API key ")) {
                Utility.getInstance().showInvalidApiKeyAlert(this, getResources().getString(R.string.relogin));
            }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                Toast.makeText(this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }
        UserController.getInstance(this).setRegisterGcmErrorCallback(null);
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onProgressUpdate(int percentage, String numberProgress) {
        Log.d(" percentage", ""+percentage);
        progressBarNya.setProgress(percentage);
        progressBarNya.setProgressNumberFormat(numberProgress);
    }

    @Override
    public void onError() {

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
            }
        }, 2000);
    }
}
