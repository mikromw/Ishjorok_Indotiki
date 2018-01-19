package com.pmberjaya.indotiki.interfaces.gmaps;

import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface GeocoderLocationInterface {
    void onSuccessGetGeocoderLocation(GeocoderLocationGMapsCallback data);
    void onErrorGetGeocoderLocation(APIErrorCallback data);
}
