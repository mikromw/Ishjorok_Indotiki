package com.pmberjaya.indotiki.app.deposit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.UploadPhotoCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.UploadImageInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationData;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by Gilbert on 4/13/2017.
 */

public class DepositConfirmationDetail extends BaseActivity implements ProgressRequestBody.UploadCallbacks {
    private LinearLayout layout_order_code;
    private LinearLayout layout_your_bank_name;

    private TextView tv_order_code;
    private ImageView iv_total_warning;
    private TextView tv_total_deposit;
    private TextView tv_name_of_your_bank;

    private TextView tv_time;
    private TextView tv_status;
    private LinearLayout layout_time_confirmation;
    private LinearLayout layout_nominal_deposit;
    private TextView tv_nominal_deposit;
    private LinearLayout layout_kode_unik;
    private TextView tv_kode_unik;
    private TextView tv_pay_with;
    private TextView tv_no_rekening;
    private ImageView iv_payment_evidence;
    private LinearLayout btn_upload_payment_receipt;
    private Button btn_finish;

    private AlertDialog levelDialog;

    /*** Upload Photo ***/
    private android.app.AlertDialog adialog;
    private ProgressDialog progressBarNya;
    private UserController userController;
    private int checkitemoption = 1;
    private int RESULT_LOAD_IMAGE_REQUEST_CODE = 1;
    private Uri selectedImageUri;
    private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private String imagepath = null;
    private String imageName;
    private File file;
    public static final int progress_bar_type = 0;
    private boolean isClicked = true;
    private String receipt_send_time;
    private String receipt_image;
    private LinearLayout loadinglayout;
    private RelativeLayout errorlayout;
    private LinearLayout btn_try_again;
    private String transaction_id;
    private String avatar;
    private RelativeLayout depositconfirmation_layout;

    private String deposit_order_code;
    private String kode_unik;
    private String ammount_deposit;
    private String bank_type;
    private String confirm_time;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_confirmation_detail);
        userController = UserController.getInstance(this);
        RenderView();
        initToolbar();
        setOnClickListener();
        GetIntent();
    }

    private void RenderView() {
        layout_order_code = (LinearLayout) findViewById(R.id.layout_order_code);
        layout_your_bank_name = (LinearLayout) findViewById(R.id.layout_your_bank_name);

        tv_order_code = (TextView) findViewById(R.id.tv_order_code);
        tv_total_deposit = (TextView) findViewById(R.id.tv_total_deposit);
        tv_name_of_your_bank = (TextView) findViewById(R.id.tv_name_of_your_bank);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_status = (TextView) findViewById(R.id.tv_status);
        layout_time_confirmation = (LinearLayout) findViewById(R.id.layout_time_confirmation);
        layout_nominal_deposit = (LinearLayout) findViewById(R.id.layout_nominal_deposit);
        tv_nominal_deposit = (TextView) findViewById(R.id.tv_nominal_deposit);
        layout_kode_unik = (LinearLayout) findViewById(R.id.layout_kode_unik);
        tv_kode_unik = (TextView) findViewById(R.id.tv_kode_unik);
        tv_pay_with = (TextView) findViewById(R.id.tv_pay_with);
        tv_no_rekening = (TextView) findViewById(R.id.tv_no_rekening);
        iv_payment_evidence = (ImageView) findViewById(R.id.iv_payment_evidence);
        btn_upload_payment_receipt = (LinearLayout) findViewById(R.id.btn_upload_payment_receipt);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        iv_total_warning = (ImageView) findViewById(R.id.iv_total_warning);

        loadinglayout = (LinearLayout) findViewById(R.id.layout_loading);
        errorlayout = (RelativeLayout) findViewById(R.id.layoutError);
        depositconfirmation_layout= (RelativeLayout)findViewById(R.id.deposit_confirmation_layout);
        btn_try_again = (LinearLayout) findViewById(R.id.btnError);

    }

    private void GetIntent() {
        Intent intent = getIntent();
        transaction_id = intent.getStringExtra("transaction_id");
        getDepositConfirmationDetail(transaction_id);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Balance Additions");
        }
    }

    private void setOnClickListener() {
        iv_total_warning.setOnClickListener(showToolTip);
        btn_upload_payment_receipt.setOnClickListener(uploadPayment);
        iv_payment_evidence.setOnClickListener(seeDetailPaymentReceipt);
        btn_finish.setOnClickListener(finish);
    }

    private View.OnClickListener showToolTip = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isClicked) {
                isClicked = false;
                s();
                Tooltip.make(DepositConfirmationDetail.this,
                        new Tooltip.Builder(101)
                                .anchor(iv_total_warning, Tooltip.Gravity.BOTTOM)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 3000)
                                .activateDelay(800)
                                .showDelay(500)
                                .text(getResources().getString(R.string.payment_attention))
                                .maxWidth(500)
                                .withArrow(true)
                                .withOverlay(true)
//                        .typeface(Typeface.defaultFromStyle(R.style.ToolTipLayoutDefaultStyle))
                                .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                                .build()
                ).show();
            }
        }
    };

    private View.OnClickListener uploadPayment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DepositConfirmationDetail.this);
            builder.setTitle(R.string.option_get_picture);

            final CharSequence[] itemoption = {getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
            builder.setSingleChoiceItems(itemoption, checkitemoption, new DialogInterface.OnClickListener() {
                // indexSelected contains the index of item (of which checkbox checked)

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (getResources().getString(R.string.gallery).equals(itemoption[which])) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE_REQUEST_CODE);
                        Log.d("intent", "" + i);
                    } else if (getResources().getString(R.string.camera).equals(itemoption[which])) {
                        String fileName = "new-photo-name.jpg";
                        //create parameters for Intent with filename
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, fileName);
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
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

    private View.OnClickListener seeDetailPaymentReceipt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (receipt_image != null) {
                Intent intent = new Intent(DepositConfirmationDetail.this, DepositConfirmationEvidenceImageDetail.class);
                intent.putExtra("payment_receipt", receipt_image);
                startActivity(intent);
            }
        }
    };

    private void senddepositStatus() {
        String api = Utility.getInstance().getTokenApi(DepositConfirmationDetail.this);
        UserController.getInstance(DepositConfirmationDetail.this).sendDepositConfirmStatus(transaction_id, api, sendDepositStatusInterface);
    }

    BaseInterface sendDepositStatusInterface = new BaseInterface() {
        @Override
        public void onSuccess(BaseCallback baseCallback) {
            int sukses = baseCallback.getSukses();
            String pesan = baseCallback.getPesan();
            if (sukses == 2) {
                Intent intent = new Intent(DepositConfirmationDetail.this, DepositConfirmationListActivity.class);
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(DepositConfirmationDetail.this, pesan, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(DepositConfirmationDetail.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(DepositConfirmationDetail.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DepositConfirmationDetail.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private View.OnClickListener finish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (receipt_image != null && !receipt_image.equals("")) {
                senddepositStatus();
            } else {
                Toast.makeText(DepositConfirmationDetail.this, R.string.receipt_not_uploaded, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void s() {
        CountDownTimer countDownTimer = new CountDownTimer(800, 800) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                isClicked = true;
            }
        };
        countDownTimer.start();
    }


    public void doUploadPaymentInvidencePhoto(File file) {
        String api = Utility.getInstance().getTokenApi(DepositConfirmationDetail.this);
        showDialog(progress_bar_type);
        String MEDIA_TYPE_PNG = "image/" + imageName.substring(imageName.lastIndexOf(".") + 1);
        ProgressRequestBody test = new ProgressRequestBody(file, MEDIA_TYPE_PNG, this);
        String filename = "file\"; filename=\"" + imageName.substring(1, imageName.length()) + "\" ";
        userController.doUploadPaymentEvidencePhoto(String.valueOf(transaction_id), parametersSendRequest(filename, test), api, uploadPaymentEvidenceInterface);
        return;
    }

    public Map<String, ProgressRequestBody> parametersSendRequest(String filename, ProgressRequestBody test) {
        Map<String, ProgressRequestBody> params = new HashMap<String, ProgressRequestBody>();
        params.put(filename, test);
        return params;
    }
    UploadImageInterface uploadPaymentEvidenceInterface = new UploadImageInterface() {
        @Override
        public void onSuccessUploadImage(UploadPhotoCallback data) {
            int sukses = data.getSukses();
            String temp_status = data.getStatus();
            Log.d("status", " > " + temp_status);
            if (temp_status.equals("2")) {
                progressBarNya.dismiss();
                avatar = data.getAvatar();
                receipt_image = avatar;
                PicassoLoader.loadImage(DepositConfirmationDetail.this, receipt_image, iv_payment_evidence);

            } else if (temp_status.equals("3")) {
                String pesan = data.getPath();
                Toast.makeText(DepositConfirmationDetail.this, pesan, Toast.LENGTH_SHORT).show();
            }
            onFinish();
        }

        @Override
        public void onErrorUploadImage(APIErrorCallback data) {
            progressBarNya.dismiss();
            if (data.getError() != null) {
                if (data.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(DepositConfirmationDetail.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(DepositConfirmationDetail.this, getResources().getString(R.string.relogin));
                }else if(data.getError().equals("Error: Internal Server Error")){
                    onFinish();
                    Toast.makeText(DepositConfirmationDetail.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    onFinish();
                    Toast.makeText(DepositConfirmationDetail.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
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
//                Log.d("LENGTHNYA", "" + lengthbmp);
                if (lengthbmp < 5000000) {
                    iv_payment_evidence.setImageURI(selectedImageUri);
                    doUploadPaymentInvidencePhoto(file);
                } else {
                    Toast.makeText(DepositConfirmationDetail.this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Bitmap photo = (Bitmap) data.getData().getPath();
                imagepath = getPath(selectedImageUri);
                imageName = imagepath.substring(imagepath.lastIndexOf("/"));
                File file = new File(imagepath);
                long lengthbmp = file.length();
                Log.d("LENGTHNYA", "" + lengthbmp);
                if (lengthbmp < 5000000) {
                    iv_payment_evidence.setImageURI(selectedImageUri);
                    doUploadPaymentInvidencePhoto(file);
                } else {
                    Toast.makeText(DepositConfirmationDetail.this, getResources().getString(R.string.large_file_size), Toast.LENGTH_SHORT).show();
                }
            } else {
//                Toast.makeText(DepositConfirmationDetail.this, "ambil foto gagal", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
            this.finish();
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = DepositConfirmationDetail.this.getContentResolver().query(uri,
                projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(DepositConfirmationDetail.this, DepositConfirmationListActivity.class);
        finish();
//        startActivity(intent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
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
        Log.d(" percentage", "" + percentage);
        progressBarNya.setProgress(percentage);
        progressBarNya.setProgressNumberFormat(progressNumber);
    }

    @Override
    public void onError() {
        Log.d(" onError", "onError");
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
                try {
                    dismissDialog(progress_bar_type);
                } catch (Exception e2) {
                }
            }
        }, 1000);
    }



    public void getDepositConfirmationDetail(String transaction_id) {
        renderLoadingView();
        String api = Utility.getInstance().getTokenApi(DepositConfirmationDetail.this);
        userController.getDepositConfirmationDetail(transaction_id, api, getDepositConfirmationDetailInterface);
        return;
    }
    BaseGenericInterface getDepositConfirmationDetailInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            depositconfirmation_layout.setVisibility(View.VISIBLE);
            DepositConfirmationData depositConfirmationData =(DepositConfirmationData) baseGenericCallback.getData();

            deposit_order_code = depositConfirmationData.getDeposit_order_code();
            kode_unik = depositConfirmationData.getDeposit_kode_unik();
            ammount_deposit = depositConfirmationData.getAmount();
            bank_type = depositConfirmationData.getBank();
            confirm_time = depositConfirmationData.getConfirm_time();
            receipt_image = depositConfirmationData.getReceipt_image();


            tv_order_code.setText(deposit_order_code);
            int total_ammount = (Integer.parseInt(kode_unik) + Integer.parseInt(ammount_deposit));
            tv_total_deposit.setText("Rp. " + Utility.getInstance().convertPrice(total_ammount));
            tv_pay_with.setText(bank_type);
            if (bank_type.equals("BCA")) {
                tv_no_rekening.setText("8210436333");
            } else {
                tv_no_rekening.setText("1090015598972");
            }
            tv_time.setText(confirm_time);
            layout_your_bank_name.setVisibility(View.VISIBLE);
            PicassoLoader.loadImage(DepositConfirmationDetail.this, receipt_image, iv_payment_evidence);

            if (depositConfirmationData.status.equals("0")) {
                tv_status.setText(getResources().getString(R.string.status_deposit_confirmation0));
                btn_finish.setVisibility(View.GONE);
                btn_upload_payment_receipt.setVisibility(View.GONE);
            } else if (depositConfirmationData.status.equals("1")) {
                tv_status.setText(getResources().getString(R.string.status_deposit_confirmation1));
                btn_finish.setVisibility(View.GONE);
                btn_upload_payment_receipt.setVisibility(View.GONE);
            } else if (depositConfirmationData.status.equals("2")) {
                tv_status.setText(getResources().getString(R.string.status_deposit_confirmation2));
            } else if (depositConfirmationData.status.equals("3")) {
                tv_status.setText(getResources().getString(R.string.status_deposit_confirmation3));
                btn_finish.setVisibility(View.GONE);
                btn_upload_payment_receipt.setVisibility(View.GONE);
            }
            loadinglayout.setVisibility(View.GONE);
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            renderErrorView(errorGetDepositConfirmationDetail);
        }
    };

    private View.OnClickListener errorGetDepositConfirmationDetail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDepositConfirmationDetail(transaction_id);
        }
    };

    private void renderErrorView(View.OnClickListener onClickListener) {
        loadinglayout.setVisibility(View.GONE);
        errorlayout.setVisibility(View.VISIBLE);
        depositconfirmation_layout.setVisibility(View.GONE);
        btn_try_again.setOnClickListener(onClickListener);
    }

    private void renderLoadingView() {
        loadinglayout.setVisibility(View.VISIBLE);
        depositconfirmation_layout.setVisibility(View.GONE);
        errorlayout.setVisibility(View.GONE);
    }
}
