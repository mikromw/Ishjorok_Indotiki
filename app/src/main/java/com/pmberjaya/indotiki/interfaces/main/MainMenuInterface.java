package com.pmberjaya.indotiki.interfaces.main;

import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.main.MainMenuItemData;

import java.util.ArrayList;

/**
 * Created by edwin on 01/04/2017.
 */

public interface MainMenuInterface {
    void onSuccesGetMainMenuItem(BaseGenericCallback<ArrayList<MainMenuItemData>> getMainMenuItemCallback);
    void onErrorGetMainMenuItem(APIErrorCallback apiErrorCallback);
}

