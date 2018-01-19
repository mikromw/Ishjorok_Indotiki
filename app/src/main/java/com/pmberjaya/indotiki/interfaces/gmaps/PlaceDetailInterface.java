package com.pmberjaya.indotiki.interfaces.gmaps;

import com.pmberjaya.indotiki.callbacks.gmaps.PlaceDetailGmapsCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface PlaceDetailInterface {
    void onSuccessGetPlaceDetail(PlaceDetailGmapsCallback data);
    void onErrorGetPlaceDetail(APIErrorCallback data);
}

