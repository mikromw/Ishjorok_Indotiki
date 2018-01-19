package com.pmberjaya.indotiki.app.account.profile;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.pmberjaya.indotiki.app.account.VerificationSmsActivity;
import com.pmberjaya.indotiki.app.account.login.LoginActivity;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.account.EditProfilCallback;
import com.pmberjaya.indotiki.callbacks.account.UploadProfilePhotoCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.interfaces.account.EditProfileInterface;
import com.pmberjaya.indotiki.interfaces.account.UploadImageProfileInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.models.account.UserModel;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends BaseActivity implements ProgressRequestBody.UploadCallbacks{
    SessionManager session;
    private LinearLayout updateLayout;
    private LinearLayout loadinglayout;
    private LinearLayout layoutEditProfile;
    private EditText ed_fullname;
    private EditText ed_email;
    private TextView tv_email;
    private TextView tv_namevalid;
    private TextView tv_emailvalid;
    private TextView tv_fullname;
    private EditText et_phone;
    private String phone_data;
    private TextView nohpvalid;
    private ImageView iv_fotoprofil;
    private String fullnameData;
    private String emailData;
    protected Toolbar toolbar;
    private ProgressDialog pDialog;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private static int RESULT_LOAD_IMAGE_REQUEST_CODE = 1;
    private Uri selectedImageUri;
    int checkitemoption=-1;
    int checkitemactionoption=-1;
    private AlertDialog adialog;
    private String imagepath=null;
    ProgressDialog progressBarNya;
    public static final int progress_bar_type = 0;
    private String imageName;
    private String profilePhotoData;
    private CheckBox block_call_feature;
    private String  blockCallFeature;
    private boolean runDoEditProfil = false;
    private boolean runGetUserData = true;
    private CharSequence[] itemActionPhotoOption ;
    private CharSequence[] itemoption;
    private PercentRelativeLayout layoutFormEditProfile;

    /************************************************************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMainActivity();
    }

    private void loadMainActivity() {
        session = new SessionManager(this);
        if(!session.isLogin()){
            Intent i = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivityForResult(i, Constants.STATE_LOGIN_CODE);
        }
        setContentView(R.layout.account_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        initToolbar(toolbar, getResources().getString(R.string.edit_profile), null);
        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        layoutEditProfile = (LinearLayout) findViewById(R.id.layoutEditProfile);
        layoutFormEditProfile = (PercentRelativeLayout) findViewById(R.id.layoutFormEditProfile);
        nohpvalid = (TextView) findViewById(R.id.nohpvalid);
        tv_namevalid = (TextView) findViewById(R.id.namevalid);
        tv_emailvalid = (TextView) findViewById(R.id.emailvalid);
        et_phone = (EditText) findViewById(R.id.nohp);
        ed_fullname = (EditText) findViewById(R.id.fullname);
        ed_email = (EditText) findViewById(R.id.ed_email);
        block_call_feature = (CheckBox) findViewById(R.id.block_call_feature);
        itemActionPhotoOption = new CharSequence[]{getResources().getString(R.string.edit_photo), getResources().getString(R.string.delete_photo)};
        itemoption = new CharSequence[]{getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
        iv_fotoprofil = (ImageView) findViewById(R.id.fotoprofil);
        iv_fotoprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(profilePhotoData)){
                    showActionProfilePhotoAlertDialog();
                }
                else {
                    showWayEditPhotoAlertDialog();
                }
            }
        });
        updateLayout = (LinearLayout ) findViewById(R.id.updateLayout);
        updateLayout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                isValidForm();
            }

        });
        block_call_feature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    blockCallFeature = "true";
                }else{
                    blockCallFeature = "false";
                }
            }
        });
        //---------------------------------------------------------------------------------------------------
    }
    public void showActionProfilePhotoAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(R.string.select_action);
        builder.setSingleChoiceItems(itemActionPhotoOption,checkitemactionoption, new DialogInterface.OnClickListener() {
            // indexSelected contains the index of item (of which checkbox checked)

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(getResources().getString(R.string.edit_photo).equals(itemActionPhotoOption[which]))
                {
                    showWayEditPhotoAlertDialog();
                }
                else if(getResources().getString(R.string.delete_photo).equals(itemActionPhotoOption[which]))
                {
                    doDeletePhoto();
                }

                dialog.dismiss();
            }
        });
        adialog = builder.create();//AlertDialog dialog; create like this outside onClick
        adialog.show();
    }
    public void showWayEditPhotoAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
    public OnClickListener try_again_listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            renderloadingView();
            getUserData();
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
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(EditProfileActivity.this, "ambil foto gagal", Toast.LENGTH_SHORT).show();
            }
        }
        else  if (requestCode == Constants.STATE_LOGIN_CODE) {
            if(resultCode == RESULT_OK){
                String phone = session.getUserDetails().get(session.KEY_PHONE);
                if(phone==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
//						builder.setView(alertView);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle(getResources().getString(R.string.validation_profile));
//						final EditText editText = (EditText) alertView.findViewById(R.id.phone);

                    builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
                loadMainActivity();
            }
            else if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                this.finish();
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = EditProfileActivity.this.getContentResolver().query(uri,
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
        String api = Utility.getInstance().getTokenApi(EditProfileActivity.this);
        Log.d("api",""+api);
        UserController.getInstance(EditProfileActivity.this).doUploadProfilePhoto(parametersSendRequest(filename,test),api, uploadImageProfileInterface);
        return;
    }
    UploadImageProfileInterface uploadImageProfileInterface = new UploadImageProfileInterface() {
        @Override
        public void onSuccessUploadImageProfile(UploadProfilePhotoCallback response) {
            //	dialogupload.dismiss();
            //onFinish();
            int sukses = response.getSukses();
            Log.d("test",""+sukses);
            if(sukses==2){
                progressBarNya.dismiss();
                profilePhotoData = response.getAvatar();
                PicassoLoader.loadProfile(EditProfileActivity.this,profilePhotoData,iv_fotoprofil,R.mipmap.img_no_avatar);
                session.createLoginSession(null,null,null,profilePhotoData,null, null,null);
            }
            onFinish();
        }

        @Override
        public void onErrorUploadImageProfile(APIErrorCallback apiErrorCallback) {
            progressBarNya.dismiss();
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(EditProfileActivity.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(EditProfileActivity.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public void doDeletePhoto(){
        pDialog = ProgressDialog.show(EditProfileActivity.this, "", "loading...");
        String api = Utility.getInstance().getTokenApi(EditProfileActivity.this);
        UserController.getInstance(EditProfileActivity.this).doDeleteProfilePhoto(api, deletePhotoInterface);
        return;
    }
    public Map<String, ProgressRequestBody> parametersSendRequest(String filename, ProgressRequestBody test) {
        Map<String, ProgressRequestBody> params = new HashMap<String, ProgressRequestBody>();
        params.put(filename, test);

        return  params;
    }

    public Map<String, String> profileParameters(String fullnameData, String emailData, String profilePhotoData,String phone_data) {
        Map<String, String> params = new HashMap<String, String>();
        if(fullnameData!=null) {
            params.put("fullname", fullnameData);
        }else{
            params.put("fullname", "");
        }
        if(emailData!=null) {
            params.put("email", emailData);
        }else{
            params.put("email", "");
        }
        if(profilePhotoData!=null) {
            params.put("avatar", profilePhotoData);
        }else{
            params.put("avatar", "");
        }
        if(phone_data!=null) {
            params.put("phone",phone_data);
        }else{
            params.put("phone","");
        }
        params.put("birthdate","");
        if(blockCallFeature!=null) {
            params.put("hide_phone",blockCallFeature);
        }else{
            params.put("hide_phone","");
        }
        return  params;
    }
    BaseInterface deletePhotoInterface = new BaseInterface() {
        @Override
        public void onSuccess(BaseCallback baseCallback) {
            int sukses = baseCallback.getSukses();
            String pesan = baseCallback.getPesan();
            pDialog.dismiss();
            if(sukses==2){
                PicassoLoader.loadProfileFail(EditProfileActivity.this, iv_fotoprofil, R.mipmap.img_no_avatar);
            }
            session.deleteAvatarSession();
            Toast.makeText(EditProfileActivity.this, ""+pesan, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(EditProfileActivity.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(EditProfileActivity.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private void getUserData()
    {
        renderloadingView();
        String api = Utility.getInstance().getTokenApi(EditProfileActivity.this);
        UserController.getInstance(this).getUserData(api, getUserDataInterface);
        return;
    }

    View errorLayout;
    public void renderloadingView(){
        loadinglayout.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEditProfile.removeView(errorLayout);
        }
    }
    public void renderSuccessView(){
        loadinglayout.setVisibility(View.GONE);
        layoutFormEditProfile.setVisibility(View.VISIBLE);
        if(errorLayout!=null) {
            layoutEditProfile.removeView(errorLayout);
        }
        layoutEditProfile.setVisibility(View.VISIBLE);
    }
    public void renderErrorView(){
        loadinglayout.setVisibility(View.GONE);
        layoutFormEditProfile.setVisibility(View.GONE);
        layoutEditProfile.setVisibility(View.VISIBLE);
        errorLayout = Utility.getInstance().showErrorLayout(EditProfileActivity.this, layoutEditProfile, getResources().getString(R.string.oops_something_went_wrong), try_again_listener);
    }
    private void doEditProfil()
    {
        pDialog = ProgressDialog.show(EditProfileActivity.this, "", "loading...");
        runDoEditProfil = true;
        if(ed_fullname.getVisibility()==View.VISIBLE) {
            fullnameData = ed_fullname.getText().toString();
        }
        else if(ed_fullname.getVisibility()==View.GONE){
            fullnameData = tv_fullname.getText().toString();
        }
        phone_data = Utility.getInstance().phoneFormat(et_phone.getText().toString());
        if(ed_email.getVisibility()==View.VISIBLE){
            emailData = ed_email.getText().toString();
        }
        else{
            emailData = tv_email.getText().toString();
        }
        String api = Utility.getInstance().getTokenApi(EditProfileActivity.this);
        UserController.getInstance(this).postEditProfil(profileParameters(fullnameData, emailData, profilePhotoData,phone_data),pDialog,api,editProfilInterface);
        return;
    }
    EditProfileInterface editProfilInterface = new EditProfileInterface() {
        @Override
        public void onSuccessEditProfile(EditProfilCallback editProfilCallback) {
            int sukses = editProfilCallback.getSukses();
            String pesan = editProfilCallback.getPesan();
            if(sukses==4){
                tokenVerification = editProfilCallback.getToken();
                Utility.getInstance().showSimpleAlertDialog(EditProfileActivity.this, null, pesan,
                        getResources().getString(R.string.ok),intentVerification, null,null, false);
                pDialog.dismiss();
            }
            else if(sukses==2){
                Utility.getInstance().showSimpleAlertDialog(EditProfileActivity.this, getResources().getString(R.string.update_profil_success), pesan,
                        getResources().getString(R.string.ok),intentMainMenu, null,null, false);
                pDialog.dismiss();
            }
            else if(sukses==3){
                ArrayList<String> error =editProfilCallback.getError();
                StringBuilder sb= new StringBuilder();
                for(int i = 0; i<error.size(); i++){
                    String errorMsg = error.get(i);
                    sb.append("-"+errorMsg+"\n");
                }
                Utility.getInstance().showSimpleAlertDialog(EditProfileActivity.this, pesan, sb.toString(),
                        getResources().getString(R.string.ok),dismissDialogListener, null,null, false);
                pDialog.dismiss();
            }
        }

        @Override
        public void onErrorEditProfile(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(EditProfileActivity.this, getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(EditProfileActivity.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private void isValidForm()
    {
        //---------------------------------------CHECK VALIDATION----------------------------------------------
        String fullnameData = ed_fullname.getText().toString();
        String emailData = ed_email.getText().toString();
        String phone = et_phone.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
        String phoneNumber = "[+][0-9]*$|[0-9]*$";
        if(phone.length() == 0){
            nohpvalid.setVisibility(View.VISIBLE);
            nohpvalid.setText(getResources().getString(R.string.phone_valid));
            et_phone.requestFocus();
        }
        else if(phone.length() < 10){
            nohpvalid.setVisibility(View.VISIBLE);
            nohpvalid.setText(getResources().getString(R.string.phone_valid2));
            et_phone.requestFocus();
        }
        else if(!phone.matches(phoneNumber)){
            nohpvalid.setVisibility(View.VISIBLE);
            nohpvalid.setText(getResources().getString(R.string.phone_valid3));
            et_phone.requestFocus();
        }
        else{
            nohpvalid.setVisibility(View.GONE);
        }
        if(ed_email.getVisibility()==View.VISIBLE) {
            if (emailData.length() == 0) {
                tv_emailvalid.setVisibility(View.VISIBLE);
                tv_emailvalid.setText(getResources().getString(R.string.email_valid2));
                ed_email.requestFocus();
            }
            else if (emailData.length() != 0 && !emailData.matches(emailPattern)) {
                tv_emailvalid.setVisibility(View.VISIBLE);
                tv_emailvalid.setText(getResources().getString(R.string.email_valid));
                ed_email.requestFocus();
            } else {
                tv_emailvalid.setVisibility(View.GONE);
            }
        }
        if(ed_fullname.getVisibility()==View.VISIBLE) {
            if (fullnameData.length() == 0) {
                tv_namevalid.setVisibility(View.VISIBLE);
                tv_namevalid.setText(getResources().getString(R.string.fullname_valid));
                ed_fullname.requestFocus();
            } else {
                tv_namevalid.setVisibility(View.GONE);
            }
            if (phone.length() >= 10&&phone.matches(phoneNumber)&&(emailData.length()==0||emailData.matches(emailPattern))&&fullnameData.length() != 0) {
                doEditProfil();
            }
        }
        else{
            if (phone.length() >= 10&&phone.matches(phoneNumber)&&(emailData.length()==0||emailData.matches(emailPattern))) {
                doEditProfil();
            }
        }
    }

    String tokenVerification;

    public DialogInterface.OnClickListener dismissDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();

        }
    };
    public DialogInterface.OnClickListener intentVerification = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(EditProfileActivity.this, VerificationSmsActivity.class);
            UserModel userData = new UserModel(null, fullnameData, emailData, profilePhotoData, phone_data, null, blockCallFeature,null,null);
            intent.setAction(Constants.ACCOUNT_VERIF_EDIT_PROFILE);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.USER_DATA, userData);
            intent.putExtra(Constants.TOKEN_VERIF, tokenVerification);
            finish();
            startActivity(intent);
            dialogInterface.dismiss();
        }
    };
    public DialogInterface.OnClickListener intentMainMenu= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(EditProfileActivity.this, MainActivityTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            dialogInterface.dismiss();
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(runDoEditProfil == true){
            doEditProfil();
        }
        if(runGetUserData == true){
            getUserData();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(progressBarNya!=null) {
            dismissDialog(progress_bar_type);
        }
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            LinearLayout fotoprofil_layout = (LinearLayout) findViewById(R.id.fotoprofil_layout);
//            iv_fotoprofil = (ImageView) findViewById(R.id.fotoprofil);
//            int bookingHeaderHeight = iv_fotoprofil.getMeasuredHeight()/2;
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)fotoprofil_layout.getLayoutParams();
//            params.setMargins(0, -bookingHeaderHeight, 0, 0);
//            fotoprofil_layout.setLayoutParams(params);
//        }
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
        //
    }

    BaseGenericInterface getUserDataInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> getUserDataCallback) {
            renderSuccessView();
            int sukses = getUserDataCallback.getSukses();
            String pesan = getUserDataCallback.getPesan();
            UserModel userModel = (UserModel) getUserDataCallback.getData();
            if(sukses==2){
                blockCallFeature= userModel.getHide_number();
                if(blockCallFeature!=null&&blockCallFeature.equals("true")){
                    block_call_feature.setChecked(true);
                }else{
                    block_call_feature.setChecked(false);
                }

                String fullname = userModel.getFullname();
                String email = userModel.getEmail();
                profilePhotoData = userModel.getAvatar();
                String phone = userModel.getPhone();
//                String change_name = userModel.getChangeName();
//                Log.d("changename", change_name+"");
//                if(change_name!=null||change_name.equals("0")){
                if(fullname!=null){
                    ed_fullname.setText(fullname);

                }
//                    ed_fullname.setVisibility(View.VISIBLE);
//                }
//                else{
//                    tv_fullname.setVisibility(View.VISIBLE);
//                    tv_fullname.setText(fullname);
//                    ed_fullname.setVisibility(View.GONE);
//                }
                if (phone!=null){
                    et_phone.setText(phone);
                }

                if(email!=null){
                    ed_email.setVisibility(View.VISIBLE);
                    ed_email.setText(email);
//                    tv_email.setVisibility(View.GONE);
                }
//                else {
//                    tv_email.setText(email);
//                }
                Log.d("URL_IMAGENYA", profilePhotoData+"");
                PicassoLoader.loadProfile(EditProfileActivity.this,profilePhotoData,iv_fotoprofil,R.mipmap.img_no_avatar);
            }
            else{
                Toast.makeText(EditProfileActivity.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(EditProfileActivity.this, getResources().getString(R.string.relogin));
                } else {
                    renderErrorView();
//                    Toast.makeText(this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}

