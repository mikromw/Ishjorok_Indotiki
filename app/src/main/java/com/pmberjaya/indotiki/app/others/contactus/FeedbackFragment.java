package com.pmberjaya.indotiki.app.others.contactus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.controllers.UserController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.models.account.DeviceDataModel;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edwin on 19/12/2016.
 */

public class FeedbackFragment extends Fragment{
    private String fullname;
    private String phone;
    private String address;
    private String email;
    private String subject;
    private String description;

    private TextView tv_email;
    private TextView tv_description;
    private TextView tv_subject;
    private TextView tv_phone;
    private TextView tv_fullname;

    private EditText et_fullname;
    private EditText et_phone;
    private EditText et_email;
    private EditText et_subject;
    private EditText et_description;

    private Button sendbutton;
    private ProgressDialog progressDialog;
    private boolean runGetFeedback = false;
    private SessionManager session;
    private String emailData;
    private String phoneData;
    private String nameData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_us_feedback_fragment, container, false);
        session = new SessionManager(getActivity());
        HashMap<String,String> mapUser = session.getUserDetails();
        emailData = mapUser.get(SessionManager.KEY_EMAIL);
        phoneData = mapUser.get(SessionManager.KEY_PHONE);
        nameData = mapUser.get(SessionManager.KEY_NAMA);

        tv_email = (TextView) rootView.findViewById(R.id.emailvalid);
        tv_description = (TextView) rootView.findViewById(R.id.descriptionvalid);
        tv_subject = (TextView) rootView.findViewById(R.id.subjectvalid);
        tv_phone = (TextView) rootView.findViewById(R.id.phonevalid);
        tv_fullname = (TextView) rootView.findViewById(R.id.fullnamevalid);
        et_fullname = (EditText) rootView.findViewById(R.id.fullname);
        et_phone = (EditText) rootView.findViewById(R.id.phone);
        et_email = (EditText) rootView.findViewById(R.id.email);
        et_subject = (EditText) rootView.findViewById(R.id.subject);
        et_description = (EditText) rootView.findViewById(R.id.description);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);
        et_fullname.setText(nameData);
        et_email.setText(emailData);
        et_phone.setText(phoneData);

        sendbutton = (Button) rootView.findViewById(R.id.sendbutton);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = et_fullname.getText().toString();
                phone = et_phone.getText().toString();
                email = et_email.getText().toString();
                subject = et_subject.getText().toString();
                description = et_description.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
                if(fullname.length()==0)
                {
                    tv_fullname.setVisibility(View.VISIBLE);
                    tv_fullname.setText("Masukkan Nama");
                    et_fullname.requestFocus();
                }
                else
                {
                    tv_fullname.setVisibility(View.GONE);
                }
                if(phone.length()==0)
                {
                    tv_phone.setVisibility(View.VISIBLE);
                    tv_phone.setText("Periksa Nomor Anda");
                    et_phone.requestFocus();
                }
                else
                {
                    tv_phone.setVisibility(View.GONE);
                }
                if (email.length() == 0)
                {
                    tv_email.setVisibility(View.VISIBLE);
                    tv_email.setText("Masukkan Email");
                    et_description.requestFocus();
                }
                else if(email.length()!=0&&!email.matches(emailPattern)){
                    tv_email.setVisibility(View.VISIBLE);
                    tv_email.setText(getResources().getString(R.string.email_valid));
                    et_description.requestFocus();
                }
                else
                {
                    tv_email.setVisibility(View.GONE);
                }

                if(subject.length()==0)
                {
                    tv_subject.setVisibility(View.VISIBLE);
                    tv_subject.setText("Masukkan Subject");
                    et_subject.requestFocus();
                }
                else
                {
                    tv_subject.setVisibility(View.GONE);
                }

                if(description.length()==0)
                {
                    tv_description.setVisibility(View.VISIBLE);
                    tv_description.setText("Masukkan Description");
                    et_description.requestFocus();
                }
                else
                {
                    tv_description.setVisibility(View.GONE);
                }

                if(description.length()!=0 && subject.length()!=0 && email.length()!=0 && email.matches(emailPattern) && fullname.length()!=0 && phone.length()!=0) {
                    getFeedback();


                }
            }

        });
        return rootView;
    }
    private void getFeedback() {
            runGetFeedback = true;
            Log.d("feedback", "JALAN");
            progressDialog= ProgressDialog.show(getActivity(), "", "loading...");
            String api = Utility.getInstance().getTokenApi(getActivity());
            UserController.getInstance(getActivity()).postFeedback(feedbackParameters(),progressDialog, api, feedbackInterface);
            return;
    }
    BaseInterface feedbackInterface = new BaseInterface() {
        @Override
        public void onSuccess(BaseCallback feedbackCallback) {
            runGetFeedback = false;
            String pesan = feedbackCallback.getPesan();
            progressDialog.dismiss();
            Log.d("CALLBACK", "JALAN");
            if (feedbackCallback.getSukses() == 2) {
                Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            progressDialog.dismiss();
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
                } else {
                    Toast.makeText(getActivity(), apiErrorCallback.getError() + ", " + getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public Map<String, String> feedbackParameters() {
        DeviceDataModel deviceDataModel = Utility.getInstance().getDeviceData(getActivity());
        String appVersion = Utility.getInstance().getAppVersionName(getActivity());
        String deviceModel= deviceDataModel.deviceName;
        String imei = deviceDataModel.imei;
        String os = deviceDataModel.os;
        TelephonyManager tManager = (TelephonyManager) getActivity().getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String provider = tManager.getSimOperatorName();

        Map<String, String> params = new HashMap<String, String>();
        params.put("fullname", fullname);
        params.put("phone", phone);
        params.put("email", email);
        params.put("subject", subject);
        params.put("message", description);
        params.put("app_version", appVersion);
        params.put("provider", provider);
        params.put("os", os);
        params.put("imei", imei);
        params.put("mobile_version", deviceModel);
        return  params;
    }

    public void onResume() {
        super.onResume();
        if(runGetFeedback == true){
            getFeedback();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
