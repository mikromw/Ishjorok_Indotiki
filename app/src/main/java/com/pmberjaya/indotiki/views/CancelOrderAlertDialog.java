package com.pmberjaya.indotiki.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;

/**
 * Created by edwin on 17/12/2016.
 */

public class CancelOrderAlertDialog extends DialogFragment {
    public onSubmitListener mListener;

    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.order_new_cancel_adialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView bt_back_to_order = (TextView) dialog.findViewById(R.id.positive_btn);
        bt_back_to_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.setOnSubmitListener("");
                dismiss();
            }
        });
        return dialog;
    }
}

