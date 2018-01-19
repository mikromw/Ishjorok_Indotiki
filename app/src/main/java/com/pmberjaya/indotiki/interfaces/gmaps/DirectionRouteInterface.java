package com.pmberjaya.indotiki.interfaces.gmaps;

import com.pmberjaya.indotiki.callbacks.gmaps.DirectionRouteGMapsCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface DirectionRouteInterface {
    void onSuccessGetDirectionRoute(DirectionRouteGMapsCallback data);
    void onErrorGetDirectionRoute(APIErrorCallback data);
}
