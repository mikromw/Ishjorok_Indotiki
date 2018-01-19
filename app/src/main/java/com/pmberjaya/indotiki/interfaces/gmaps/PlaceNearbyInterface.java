package com.pmberjaya.indotiki.interfaces.gmaps;

import com.pmberjaya.indotiki.callbacks.gmaps.PlaceNearbyGmapsCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface PlaceNearbyInterface {
    void onSuccessGetPlaceNearby(PlaceNearbyGmapsCallback data);
    void onErrorGetPlaceNearby(APIErrorCallback data);
}
