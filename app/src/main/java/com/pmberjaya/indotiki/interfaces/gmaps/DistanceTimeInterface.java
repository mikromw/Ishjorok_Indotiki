package com.pmberjaya.indotiki.interfaces.gmaps;

import com.pmberjaya.indotiki.callbacks.gmaps.DistanceTimeGMapsCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface DistanceTimeInterface {
    void onSuccessGetDistanceTime(DistanceTimeGMapsCallback data);
    void onErrorGetDistanceTime(APIErrorCallback data);
}
