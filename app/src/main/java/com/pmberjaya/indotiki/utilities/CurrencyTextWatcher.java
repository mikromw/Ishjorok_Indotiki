package com.pmberjaya.indotiki.utilities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class CurrencyTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText et;
    private TextView tv;
    DecimalFormatSymbols otherSymbols;
    public CurrencyTextWatcher(EditText et, TextView tv)
    {
        otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#,###.##", otherSymbols);
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###", otherSymbols);
        this.et = et;
        this.tv = tv;
        hasFractionalPart = false;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            String textprice;
            inilen = et.getText().length();


            String v = s.toString().replace(String.valueOf(otherSymbols.getGroupingSeparator()), "");
            Number n = df.parse(v);
            Number nd = dfnd.parse(v);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(df.format(n));
                textprice =  et.getText().toString();
                String theprice =textprice.replace("." , "");
                Log.d("textprice", textprice+"");
                Log.d("theprice", theprice+"");
                tv.setText(theprice);
            } else {
                et.setText(dfnd.format(nd));
                textprice =  et.getText().toString();
                String theprice =textprice.replace("." , "");
                Log.d("textprice2", textprice+"");
                Log.d("theprice2", theprice+"");
                Log.d("format", String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator())+"");
                Log.d("format2", String.valueOf(otherSymbols.getGroupingSeparator())+"");
                tv.setText(theprice);
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
        {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}