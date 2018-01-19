package com.pmberjaya.indotiki.interfaces.bookingNew;

import android.text.Editable;

/**
 * Created by edwin on 29/03/2017.
 */

public interface SearchPlaceInterface {

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after);
    public void onTextChanged(CharSequence s, int start, int before,
                              int count);
    public void afterTextChanged(final Editable thelocation);
}
