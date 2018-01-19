package com.pmberjaya.indotiki.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pmberjaya.indotiki.R;

/**
 * Created by edwin on 17/12/2016.
 */

public class NoDriverCustomDialog extends DialogFragment {

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
        dialog.setContentView(R.layout.order_new_no_driver_adialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button bt_back_to_order = (Button) dialog.findViewById(R.id.bt_back_to_order);
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

