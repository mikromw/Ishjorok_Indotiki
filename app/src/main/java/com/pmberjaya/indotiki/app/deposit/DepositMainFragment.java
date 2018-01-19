package com.pmberjaya.indotiki.app.deposit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.dao.SessionManager;


public class DepositMainFragment extends Fragment {
    private SessionManager session;
    private String deposit;

    private LinearLayout layout_top_up;
    private LinearLayout layout_couponvoucher;
    private LinearLayout layout_transaction;
    private ProgressDialog pDialog;

    private boolean runPostBonusReferal = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deposit_main_fragment, container, false);
        RenderView(rootView);

        layout_top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DepositTopUpList.class);
                startActivity(intent);
            }
        });


        layout_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DepositConfirmationListActivity.class);
                startActivity(intent);
            }
        });

        layout_couponvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CouponVoucher.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
    private void RenderView(View rootView) {
        layout_top_up = (LinearLayout) rootView.findViewById(R.id.layout_top_up);
//        layout_voucher = (LinearLayout) rootView.findViewById(R.id.layout_voucher);
        layout_transaction = (LinearLayout) rootView.findViewById(R.id.layout_transaction);
//        ed_coupon_code = (EditText) rootView.findViewById(R.id.ed_coupon_code);
//        btn_use_coupon_code = (LinearLayout) rootView.findViewById(R.id.btn_use_coupon_code);
        layout_couponvoucher = (LinearLayout)rootView.findViewById(R.id.layout_coupon);
    }

//    private void postBonusReferal() {
//        pDialog = ProgressDialog.show(getActivity(), "", "loading...");
//        String api = Utility.getInstance().getTokenApi(getActivity());
//        UserController.getInstance(getActivity()).postBonusReferal(coupon_code, api);
//        return;
//    }
//
//    @Subscribe
//    public void onData(BaseCallback baseCallback) {
//        if (baseCallback.getCallback().equals("postBonusReferal")) {
//            pDialog.dismiss();
//            int sukses = baseCallback.getSukses();
//            String pesan = baseCallback.getPesan();
//            if (sukses == 2) {
//                ((DepositTab)getActivity()).checkDeposit();
//                Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    @Subscribe
//    public void onData(APIErrorCallback apiErrorCallback) {
//        if (apiErrorCallback.getCallback().equals("postBonusReferal")) {
//            pDialog.dismiss();
//            if (apiErrorCallback.getError() != null) {
//                if (apiErrorCallback.getError().equals("Invalid API key ")) {
//                    Utility.getInstance().showInvalidApiKeyAlert(getActivity(), getResources().getString(R.string.relogin));
//                } else {
//                    Toast.makeText(getActivity(), apiErrorCallback.getError() + ", " + getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
        if (runPostBonusReferal== true) {
//            postBonusReferal();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}