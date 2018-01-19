package com.pmberjaya.indotiki.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by edwin on 17/12/2016.
 */

public class EventPromoCustomDialog extends DialogFragment {
    public onSubmitListener mListener;
    public String eventPromoDescription = "";
    public String eventPromoTitle = "";
    public String eventPromoPhoto= "";
    public String eventPromoCode= "";
    private LinearLayout btnPositive;
    private ImageView ivEventPromo;
    private TextView tvDescription;
    private TextView tvPositiveBtn;
    private TextView tvTitle;

    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.view_event_promo_pop_up_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        btnPositive = (LinearLayout)dialog.findViewById(R.id.btnPositive);
        ivEventPromo = (ImageView) dialog.findViewById(R.id.ivEventPromo);
        tvDescription = (TextView) dialog.findViewById(R.id.tvDescription);
        tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvPositiveBtn = (TextView) dialog.findViewById(R.id.tvPositiveBtn);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(eventPromoCode)){
            tvPositiveBtn.setText(getResources().getString(R.string.get_promo));
        }else{
            tvPositiveBtn.setText(getResources().getString(R.string.ok));
        }
        tvDescription.setText(Html.fromHtml(eventPromoDescription));
        tvTitle.setText(eventPromoTitle);
        PicassoLoader.loadImage(getActivity(), eventPromoPhoto, ivEventPromo);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.setOnSubmitListener(eventPromoCode);
                dismiss();
            }
        });

        return dialog;
    }
}

