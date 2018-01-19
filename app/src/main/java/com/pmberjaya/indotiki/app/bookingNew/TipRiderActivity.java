package com.pmberjaya.indotiki.app.bookingNew;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.utilities.CurrencyTextWatcher;
import com.pmberjaya.indotiki.utilities.Utility;

/**
 * Created by edwin on 22/03/2017.
 */

public class TipRiderActivity extends BaseActivity {
    private TipRiderAdapter adapter;
    private RecyclerView tip_recycler_view;
    private LinearLayout bt_submit;
    private ScrollView tipScrollLayout;
    private int duration= 2000;
    private boolean hasAnimate = false;
    public EditText ed_tip_amount;
    private CurrencyTextWatcher currencyTip;
    public TextView tv_tip_ammount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_for_rider_fragments);
        initToolbar();
        renderView();
        Intent i = getIntent();
        String tip_value = i.getStringExtra("tip_rider");
        if(tip_value==null){
            tip_value="0";
        }
        adapter = new TipRiderAdapter(this, tip_value);
        GridLayoutManager lLayout = new GridLayoutManager(this , 3);
        tip_recycler_view.setHasFixedSize(true);
        tip_recycler_view.setNestedScrollingEnabled(false);
        tip_recycler_view.setLayoutManager(lLayout);
        tip_recycler_view.setAdapter(adapter);
        tipScrollLayout.setSmoothScrollingEnabled(true);
        tipScrollLayout.setOnTouchListener(onTouchListener);
        bt_submit.setOnClickListener(submitListener);
    }

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(adapter!=null){
                if(ed_tip_amount.getVisibility()==View.VISIBLE){
                    if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(ed_tip_amount.getText().toString())){
                        String tip_value = tv_tip_ammount.getText().toString();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("tip_rider", tip_value);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }else{
                        Toast.makeText(TipRiderActivity.this, getResources().getString(R.string.please_insert_tip), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    String tip_value = adapter.getTipValue();
                    if (tip_value != null) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("tip_rider", tip_value);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(TipRiderActivity.this, getResources().getString(R.string.please_choose_tip), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
    public void hideInputManualTipAmount(){
        ed_tip_amount.setVisibility(View.GONE);
    }
    public void showInputManualTipAmount(){
        ed_tip_amount.setVisibility(View.VISIBLE);
    }
    public void setTipAmount(String amountTip){
        ed_tip_amount.setText(amountTip);
        ed_tip_amount.setSelection(ed_tip_amount.getText().length());
    }
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(objectAnimator!=null&&objectAnimator.isRunning()){
                objectAnimator.cancel();
            }
            return false;
        }
    };
    public void showPrice(){
        ed_tip_amount.setVisibility(View.VISIBLE);
    }
    private void renderView(){
        tip_recycler_view = (RecyclerView) findViewById(R.id.tip_recycler_view);
        bt_submit = (LinearLayout) findViewById(R.id.bt_submit);
        tipScrollLayout = (ScrollView) findViewById(R.id.tipScrollLayout);
        ed_tip_amount = (EditText) findViewById(R.id.ed_tip_amount);
        tv_tip_ammount = (TextView) findViewById(R.id.tv_tip_ammount);

        currencyTip = new CurrencyTextWatcher(ed_tip_amount, tv_tip_ammount);
        ed_tip_amount.addTextChangedListener(currencyTip);

    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tip_for_rider));
        }
    }
    ObjectAnimator objectAnimator;
    @Override
    public void onWindowFocusChanged (boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus&&!hasAnimate){
            hasAnimate = true;
            System.out.println("onWindowFocusChanged");
            int scrollHeight =  tipScrollLayout.getBottom();
            objectAnimator = ObjectAnimator.ofInt(tipScrollLayout, "scrollY", 0, scrollHeight).setDuration(duration);
            objectAnimator.start();
        }
    }
}
